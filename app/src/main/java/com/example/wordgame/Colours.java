package com.example.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Colours extends Activity
{
    //Global variables
    private EditText Colour;
    ImageView Picture;
    Button Next,Exit;
    String Answer;
    int Count = 0;
    int score = 0;
    int incorrect=0;
    TextView txtScore;
    Boolean Check = false;
    DbContext DB;

    //Array of pictures
    List<byte[]> colourArray = new ArrayList<byte[]>();

    //Answers for input
    List<String> colourNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colours);

        //Variables getting values from then input fields
        Colour = (EditText) findViewById(R.id.colournameAnswer);
        Next = (Button)findViewById(R.id.btnNextColour);
        Exit = (Button)findViewById(R.id.exitButton);
        txtScore = (TextView) findViewById(R.id.textScore3);
        Picture = (ImageView)findViewById(R.id.imageGridcolours);

        try{
            retrieveImages();
            Picture.setImageBitmap(BitmapFactory.decodeByteArray( colourArray.get(Count),
                    0,colourArray.get(Count).length));
        }catch (Exception ex){
            Log.e("Image retrieval",ex.toString());
        }


        // Next image Button
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next();
            }
        });

        //Exit Button
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                startActivity(menu);
            }
        });
    }

    //validate image guess
    public void Next ()
    {
        // check if text field not empty
        Answer = Colour.getText().toString().trim();
        if(!Answer.equals(""))
        {
            if(Answer.equalsIgnoreCase(colourNames.get(Count)))
            {
                //Play correct sound
                MediaPlayer mediaPlayer= MediaPlayer.create(Colours.this,R.raw.correct_answer);
                mediaPlayer.start();

                Toast.makeText(getBaseContext(), "Guess Correct", Toast.LENGTH_LONG).show();

                //score
                score++;
                txtScore.setText("Current Score: "+score);

                //Display next Guess Picture
                NextPic();
                Colour.setText("");
            }
            else
            {
                //Play incorrect sound
                MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
                mediaPlayer.start();
                Toast.makeText(getBaseContext(), "Incorrect, Guess again", Toast.LENGTH_LONG).show();

                //Reset Textfield
                Colour.setText("");

                //increase incorrect guesses count
                incorrect++;

                //Display next Guess Picture
                if(Count+incorrect == colourNames.size()) {
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
                    // display next pic if incorrect
                    NextPic();
                }
            }
        }
        else
        {
            // if guess is empty
            Toast.makeText(getBaseContext(), "Guess a word!", Toast.LENGTH_LONG).show();
        }

    }

    //NextPic method to change picture if guess correct
    public void NextPic()
    {
        Count++;

        if(score == colourNames.size())
        {
            // ending sound
            MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.applause);
            mediaPlayer.start();

            Toast.makeText(getBaseContext(),"Congratulations. All the colours have been guessed" +
                ".",Toast.LENGTH_LONG).show();

            // add score to db
            if(score>0)
            {
                insertScore();
            }

            // display if all guesses are correct
            ConstraintLayout rlMain = findViewById(R.id.layoutColours);
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
        else if(Count ==  colourNames.size())
        {
            Count=0;
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
            Picture.setImageBitmap(BitmapFactory.decodeByteArray( colourArray.get(Count),
                    0,colourArray.get(Count).length));
        }
    }

    // get image from db
    public void retrieveImages()
    {
        DB = new DbContext(getBaseContext());
        Cursor cursor = DB.getColour();

        while (cursor.moveToNext())
        {
            colourNames.add(cursor.getString(1));
            colourArray.add(cursor.getBlob(2));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        DB.close();
    }

    //add score to db
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
}
