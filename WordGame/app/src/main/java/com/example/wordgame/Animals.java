package com.example.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Animals extends Activity
{
    //Global variables declared to be used for checking and pulling data
    private EditText Name;
    ImageView Picture;
    Button Next,Exit;
    String Answer;
    int Count = 0;
    Boolean Check = false;

    //score
    int score = 0;
    EditText Score;

    //Animal array receiving all the pictures from the drawable folder
    int [] animals = {R.drawable.ant,R.drawable.bird,R.drawable.cat,R.drawable.dog,R.drawable.mouse};

    //Array of answers to check against input
    String  [] answers = {"ant","bird","cat","dog","rat"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        //Getting the text from the xml textbox
        //Button click next to change image
        Name = (EditText) findViewById(R.id.etxtAnswer);
        Next = (Button)findViewById(R.id.btnNext);
        Exit = (Button)findViewById(R.id.btnNext3);
        Picture = (ImageView)findViewById(R.id.imageGrid);
        Picture.setImageResource(animals[Count]);
        //Setting the action bar for a menu
     //   ActionBar actionBar = getActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);

        //Score
        //Score = (EditText) findViewById(R.id.etScore);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                startActivity(menu);
            }
        });
    }

    /*Validating the user input against the answer array and switching to the
    *next image where the word needs to be guessed again
    */
    public void Next (View view)
    {
        Answer = Name.getText().toString().trim();
        if(Answer != null)
        {
            if(Answer.equalsIgnoreCase(answers[Count]))
            {
                MediaPlayer mediaPlayer= MediaPlayer.create(Animals.this,R.raw.correct_answer);
                mediaPlayer.start();

                Toast.makeText(getBaseContext(), "Guess Correct", Toast.LENGTH_LONG).show();
                //score
//                score++;
//                Score.setText(score+ " ");

                NextPic();
                Name.setText("");

                if(Count == answers.length)
                {
                    Toast.makeText(getBaseContext(), "You have completed guess the words.", Toast.LENGTH_LONG).show();
                    //Intent back = new Intent(this,Category.class);
                    //startActivity(back);
                }
            }
            else
            {
                MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
                mediaPlayer.start();
                Toast.makeText(getBaseContext(), "Incorrect, Guess again", Toast.LENGTH_LONG).show();
                Name.setText("");
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Guess The word!", Toast.LENGTH_LONG).show();
        }

    }

    public void NextPic()
    {
        Count++;
        if(Count == answers.length)
        {
            Count=0;

            MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.applause);
            mediaPlayer.start();

            Toast.makeText(getBaseContext(),"Congratulations. All the animals have been guessed.",Toast.LENGTH_LONG).show();

            int secondsDelay = 1;
            final Intent mnu = new Intent(this,Category.class);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(mnu);
                    finish();
                }
            }, secondsDelay * 1000);

        }
        else
        {
            Picture.setImageResource(animals[Count]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return MenuChoice(item);
    }

    //added
    private void CreateMenu(Menu menu)
    {
        MenuItem mnu1 = menu.add(0, 0, 0, "category");
        {
            mnu1.setIcon(R.drawable.koala);
            mnu1.setShowAsAction(
                    MenuItem.SHOW_AS_ACTION_IF_ROOM |
                            MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        //
        MenuItem mnu2 = menu.add(0, 1, 1, "Item 2");
        {
            mnu2.setIcon(R.drawable.flag);
            mnu2.setShowAsAction(
                    MenuItem.SHOW_AS_ACTION_IF_ROOM |
                            MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        MenuItem mnu3 = menu.add(0, 2, 2, "Item 3");
        {
            mnu3.setIcon(R.drawable.colours);
            mnu3.setShowAsAction(
                    MenuItem.SHOW_AS_ACTION_IF_ROOM |
                            MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
    }

    private boolean MenuChoice(MenuItem item)
    {
        switch (item.getItemId())
        {
            case  android.R.id.home:
                Toast.makeText(this,"Countries",Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, Category.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                return true;
            case 0:
                Toast.makeText(this, "You have choosen to guess animals",
                        Toast.LENGTH_LONG).show();

                Intent back = new Intent(this,Animals.class);
                startActivity(back);
                return true;
            case 1:
                Toast.makeText(this, "You choose to guess countries",
                        Toast.LENGTH_LONG).show();

                Intent cou = new Intent(this,Countries.class);
                startActivity(cou);
                return true;
            case 2:
                Toast.makeText(this, "You have choose to guess Colours",
                        Toast.LENGTH_LONG).show();

                Intent col = new Intent(this,Colours.class);
                startActivity(col);
                return true;
        }
        return false;
    }
}