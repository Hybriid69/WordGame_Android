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

public class Countries extends Activity
{
    //global variables
    private EditText Name;
    ImageView Picture;
    TextView txtScore;
    Button Next, Exit;
    String Guess;
    int Counter = 0;
    int Score = 0;
    int Incorrect = 0;
    DbContext DB;
    // country image array
    List<byte[]> countryArray = new ArrayList<byte[]>();
    // country names array
    List<String> countryNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        // link to xml
        Name = (EditText) findViewById(R.id.edtxtAnswer);
        txtScore = (TextView) findViewById(R.id.textScore2);
        Next = (Button)findViewById(R.id.btnNext);
        Exit = (Button)findViewById(R.id.btnNext2);
        Picture = (ImageView)findViewById(R.id.imageGridCountry);

        // display image in imageview
        try{
            retrieveImages();
            Picture.setImageBitmap(BitmapFactory.decodeByteArray(countryArray.get(Counter),
                    0,countryArray.get(Counter).length));
            int i=0;
            while (countryNames.size()>i)
            {
                Log.i("country names "+i, countryNames.get(i));
                i++;
            }
        }catch (Exception ex){
            Log.e("Image retrieval main",ex.toString());
        }


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next();
            }
        });
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                startActivity(menu);
            }
        });
    }

    // validate guess
    public void Next ()
    {
        Guess = Name.getText().toString().trim();
        Log.e("country",countryNames.get(Counter));
        Log.e("guess",Guess);
        if(!Guess.equals(""))
        {
            if(Guess.equalsIgnoreCase(countryNames.get(Counter)))
            {
                // increment score
                Score++;
                txtScore.setText("Current Score: "+Score);

                // play correct sound
                MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.correct_answer);
                mediaPlayer.start();
                Toast.makeText(getBaseContext(), "Guess Correct", Toast.LENGTH_LONG).show();
                // display next picture
                NextPic();
                Name.setText("");
            }
            else
            {
                //play incorrect media
                MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
                mediaPlayer.start();

                //reset text field
                Name.setText("");

                //increase incorrect guesses count
                Incorrect++;

                Toast.makeText(getBaseContext(), "Guess incorrect", Toast.LENGTH_LONG).show();

                //Display next Guess Picture
                if(Counter+Incorrect == countryNames.size()) {
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
            Toast.makeText(getBaseContext(), "Guess what you see", Toast.LENGTH_LONG).show();
        }
    }

    // show next image
    public void NextPic()
    {
        Counter++;
        if(Score == countryNames.size())
        {
            // if all guesses were correct
            MediaPlayer mediaPlayer= MediaPlayer.create(Countries.this,R.raw.applause);
            mediaPlayer.start();
            Toast.makeText(getBaseContext(),"Congratulations. You guessed all correctly!.",
                    Toast.LENGTH_LONG).show();

            //add score to db
            if(Score>0)
            {
                insertScore();
            }

            //display picture when all guesse were correct
            ConstraintLayout rlMain = findViewById(R.id.countryLayout);
            final ImageView imageView = new ImageView(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );

            imageView.setLayoutParams(params);
            rlMain.addView(imageView);

            Glide.with(this)
                    .load(R.raw.firework)
                    .into(new ImageViewTarget<Drawable>(imageView) {
                        @Override
                        protected void setResource(@Nullable Drawable resource) {
                            imageView.setImageDrawable(resource);
                        }
                    });

            int secondsDelay = 3;
            final Intent mnu = new Intent(this,MenuOptions.class);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(mnu);
                    finish();
                }
            }, secondsDelay * 1000);
        }
        // if some guesses were correct
        else if(Counter ==  countryNames.size())
        {
            Counter=0;
            // add score to db
            if(Score>0)
            {
                insertScore();
            }

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
            // if incorrect display next image
            Picture.setImageBitmap(BitmapFactory.decodeByteArray(countryArray.get(Counter),
                    0, countryArray.get(Counter).length));
        }
    }
    // add score to db
    public void insertScore() {
        try {
            // Retrieve shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
            String username = sharedPreferences.getString("Username", "");

            Boolean res = DB.updateCurrentScore(username, Score);
            if (res) {
                Log.i("score", "score updated successfully");
            } else {
                Log.e("score", "Could not update score");
            }
        } catch (Exception e) {
            Log.e("Score exception", e.toString());
        }
    }
    // get country image
    public void retrieveImages()
    {
        DB = new DbContext(getBaseContext());
        Cursor cursor = DB.getCountry();

        while (cursor.moveToNext())
        {
            countryNames.add(cursor.getString(1));
            countryArray.add(cursor.getBlob(2));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        DB.close();
    }
}
