package com.example.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Animals extends Activity
{
    //Global variables declared to be used for checking and pulling data
    private EditText Name;
    ImageView Picture;
    Button Next,Exit;
    String Answer;
    int Count = 0;
    Boolean Check = false;
    TextView txtScore;
    DbContext DB;

    //score
    int score = 0;
    int incorrect=0;
    EditText Score;

    //Animal array receiving all the pictures
    List<byte[]> animalsArray = new ArrayList<byte[]>();

    //Array of answers to check against input
    List<String> animalsNames = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        //link xml
        Name = (EditText) findViewById(R.id.animalnameAnswer);
        txtScore = (TextView) findViewById(R.id.textScore);
        Next = (Button)findViewById(R.id.btnNext);
        Exit = (Button)findViewById(R.id.btnNext3);
        Picture = (ImageView)findViewById(R.id.imageGridcolours);

        // display image in image view
        try{
            retrieveImages();
            Picture.setImageBitmap(BitmapFactory.decodeByteArray( animalsArray.get(Count),
                    0,animalsArray.get(Count).length));
            int i=0;
            while (animalsNames.size()>i)
            {
                Log.i("animalsNames "+i, animalsNames.get(i));
                i++;
            }
        }catch (Exception ex){
            Log.e("Image retrieval",ex.toString());
        }

        //button click event
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next();
            }
        });

        //button click
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                startActivity(menu);
            }
        });
    }

    // validate guess image and add score
    public void Next ()
    {
        Answer = Name.getText().toString().trim();
        if(!Answer.equals(""))
        {
            if(Answer.equalsIgnoreCase(animalsNames.get(Count)))
            {
                //Play correct sound
                MediaPlayer mediaPlayer= MediaPlayer.create(Animals.this,R.raw.correct_answer);
                mediaPlayer.start();

                Toast.makeText(getBaseContext(), "Guess Correct", Toast.LENGTH_LONG).show();

                //score
                score++;
                txtScore.setText("Current Score: "+score);

                //Display next Guess Picture
                NextPic();
                Name.setText("");

            }
            else
            {
                //Play incorrect sound
                MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
                mediaPlayer.start();
                Toast.makeText(getBaseContext(), "Incorrect, Guess again", Toast.LENGTH_LONG).show();

                //reset text field
                Name.setText("");

                //increase incorrect guesses count
                incorrect++;

                //Display next Guess Picture
                if(Count+incorrect == animalsNames.size()) {
                    int secondsDelay = 1;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            NextPic();
                            finish();
                        }
                    }, secondsDelay * 1000);
                }
                else
                {
                    // display next image
                    NextPic();
                }
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Guess what you see!", Toast.LENGTH_LONG).show();
        }
    }

    //Display next Guess Picture
    public void NextPic()
    {
        Count++;
        if(score == animalsNames.size()) {

            //Play completed sound
            MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.applause);
            mediaPlayer.start();

            Toast.makeText(getBaseContext(), "Congratulations. You guessed all correctly.",
                    Toast.LENGTH_LONG).show();
            // add score to db
            if(score>0)
            {
                insertScore();
            }

            // If all guesses correct, display this pic
            ConstraintLayout rlMain = findViewById(R.id.animalLayout);
            final ImageView imageView = new ImageView(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );

            imageView.setLayoutParams(params);
            rlMain.addView(imageView);
            try {

                Glide.with(this)
                        .load(R.raw.firework)
                        .into(new ImageViewTarget<Drawable>(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                imageView.setImageDrawable(resource);

                            }
                        });

                // go to menu
                int secondsDelay = 3;
                final Intent mnu = new Intent(this, MenuOptions.class);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        startActivity(mnu);
                        finish();
                    }
                }, secondsDelay * 1000);

            } catch (Exception ex) {
                Log.e("gif Animals", ex.toString());
            }
        }
        //if some guesses were correct
        else if(Count ==  animalsNames.size())
        {
            Count = 0;
            // add score to db
            insertScore();
            Toast.makeText(getBaseContext(), "Congratulations. You've reached the end",
                    Toast.LENGTH_LONG).show();
            // go to menu
            int secondsDelay = 1;
            final Intent mnu = new Intent(this, MenuOptions.class);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(mnu);
                    finish();
                }
            }, secondsDelay * 1000);
        }
        else
            {
                // display next image if incorrect
                Picture.setImageBitmap(BitmapFactory.decodeByteArray(animalsArray.get(Count),
                        0, animalsArray.get(Count).length));
            }
        }
    // add score to db
    public void insertScore() {
        try {
            // Retrieve shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
            String username = sharedPreferences.getString("Username", "");

            Boolean res = DB.updateCurrentScore(username, score);
            if (res) {
                Log.i("score", "score updated successfully");
            } else {
                Log.e("score", "Could not update score");
            }
        } catch (Exception e) {
            Log.e("Score exception", e.toString());
        }
    }
    // get animal image
    public void retrieveImages()
    {
        DB = new DbContext(getBaseContext());
        Cursor cursor = DB.getAnimal();

        while (cursor.moveToNext())
        {
            animalsNames.add(cursor.getString(1));
            animalsArray.add(cursor.getBlob(2));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        DB.close();
    }
}