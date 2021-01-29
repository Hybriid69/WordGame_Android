package com.example.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class matchthesynonyms extends Activity
{
    //global variables
    DbContext DB;
    TextView txWord;
    TextView txSync;
    int TotalMoveSyn;
    int TotalMoveActualWord;
    int checkCount;
    Button Exit,checkMatch,nextMatch;
    MediaPlayer mediaPlayer;
    //score
    int score = 0;
    int incorrect=0;
    TextView txtScore;

    //synonym array
    List<String> synonymArray = new ArrayList<String>();
    //actual word array
    List<String> actualWordArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchthesynonyms);
        //link xml
        Exit = (Button)findViewById(R.id.exitButtonCountry);
        checkMatch = (Button)findViewById(R.id.check);
        nextMatch = (Button)findViewById(R.id.next);
        txtScore=(TextView)findViewById(R.id.textScore2);

        //display word in xml control
        try {
            getSynonyms();
            // display word
            txWord =(TextView)findViewById(R.id.txtWor);
            txWord.setText(actualWordArray.get(TotalMoveActualWord));
            //display synonym
            txSync = (TextView)findViewById(R.id.txtSynonym);
            txSync.setText(synonymArray.get(TotalMoveSyn));
        }
        catch (Exception ex)
        {
            Log.e("Display word", ex.toString());
        }

        nextMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveString();
            }
        });
        checkMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCheck();
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
    // get synonyms for DB
    public void getSynonyms()
    {
        DB = new DbContext(this);
        Cursor res = DB.getSynonym();
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                actualWordArray.add(res.getString(1));
                synonymArray.add(res.getString(2));
            }
        }
        else
        {
            Log.e("Synonyms error","Nothing Found");
        }
    }
    // display next synonym
    public void moveString()
    {
        try
        {
            // set synonym word
            TotalMoveSyn++;
            if(TotalMoveSyn < synonymArray.size())
            {
                txSync.setText(synonymArray.get(TotalMoveSyn));
            }
            else
            {
                TotalMoveSyn = 0;
                txSync.setText(synonymArray.get(TotalMoveSyn));
            }
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            Log.e("move string",ex.toString());
        }
    }

    // validate if guess is correct
    public void btnCheck()
    {
    try {

    // if match is correct
        if (TotalMoveActualWord == TotalMoveSyn ) {
            if (incorrect + score <= TotalMoveActualWord) {
                //Play correct sound
                mediaPlayer = MediaPlayer.create(matchthesynonyms.this, R.raw.correct_answer);
                mediaPlayer.start();

                // to increment for word
                TotalMoveActualWord++;
                //score
                score++;
                txtScore.setText("Current Score: " + score);

                Toast.makeText(getBaseContext(), "Correct! The synonym matches..", Toast.LENGTH_SHORT).show();

                // set next actual word
                if (TotalMoveActualWord < actualWordArray.size()) {
                    txWord.setText(actualWordArray.get(TotalMoveActualWord));
                }
                // all matches correct
                else if (score >= TotalMoveActualWord) {
                    // add score to db
                    if (score > 0) {
                        insertScore();
                    }
                    // play end media
                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.applause);
                    mediaPlayer.start();

                    //set end image
                    ConstraintLayout rlMain = findViewById(R.id.synLayout);
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

                    //TotalMoveActualWord=0;
                    Toast.makeText(getBaseContext(), "You have successfully completed the game", Toast.LENGTH_SHORT).show();
                    int secondsDelay = 3;
                    final Intent mnu = new Intent(this, MenuOptions.class);

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(mnu);
                            finish();
                        }
                    }, secondsDelay * 1000);
                }
                else {
                    //if some were guessed correctly
                    int nextGuess = actualWordArray.size();
                    if ((score + incorrect) == nextGuess) {
                        Toast.makeText(getBaseContext(), "Correct match! Congratulations. You've " +
                                "reached the end", Toast.LENGTH_SHORT).show();
                        // add score to db
                        if(score>0)
                        {
                            insertScore();
                        }
                        // navigate to next activity
                        int secondsDelay = 1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                finish();
                                Intent menu = new Intent(getBaseContext(), MenuOptions.class);
                                startActivity(menu);
                            }
                        }, secondsDelay * 1000);
                    }

                }
            }
        }
    else
    {
            // incorrect guess
            //play incorrect sound
            MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
            mediaPlayer.start();

            Toast.makeText(getBaseContext(), "Incorrect match!", Toast.LENGTH_SHORT).show();

            //increase incorrect guesses count
            incorrect++;
            TotalMoveActualWord++;
            if (TotalMoveActualWord < actualWordArray.size()) {
                txWord.setText(actualWordArray.get(TotalMoveActualWord));
            }

            //Display next Guess
            int nextGuess=actualWordArray.size();
            //Display next Guess Picture
            if((score + incorrect) == nextGuess) {
                Toast.makeText(getBaseContext(), "Incorrect match!.Congratulations. You've reached the end", Toast.LENGTH_LONG).show();
                int secondsDelay = 1;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        finish();
                        Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                        startActivity(menu);
                    }
                }, secondsDelay * 1000);
            }
        }
        }
        catch (Exception e)
        {
            Log.e("button check method",e.toString());
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

}
