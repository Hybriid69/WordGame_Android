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

public class Colours extends Activity
{
    //Global variables
    private EditText Colour;
    ImageView Picture;
    Button Next,Exit;
    String Answer;
    int Count = 0;
    Boolean Check = false;

    //Array of pictures
    int [] colours = {R.drawable.blue,R.drawable.green,R.drawable.grey,R.drawable.purple,R.drawable.yellow};

    //Answers for input
    String  [] colourAnswers = {"blue","green","grey","purple","yellow"};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colours);

        //Variables getting values from then input fields
        Colour = (EditText) findViewById(R.id.etxtAnswer);
        Next = (Button)findViewById(R.id.btnNext);
        Exit = (Button)findViewById(R.id.btnNext4);
        Picture = (ImageView)findViewById(R.id.imageGrid);
        Picture.setImageResource(colours[Count]);
        //Setting the action bar for a menu
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                startActivity(menu);
            }
        });
    }

    //Next method
    public void Next (View view)
    {
        Answer = Colour.getText().toString().trim();
        if(Answer != null)
        {
            if(Answer.equalsIgnoreCase(colourAnswers[Count]))
            {
                MediaPlayer mediaPlayer= MediaPlayer.create(Colours.this,R.raw.correct_answer);
                mediaPlayer.start();
                Toast.makeText(getBaseContext(), "Guess Correct", Toast.LENGTH_LONG).show();
                NextPic();
                Colour.setText("");

                if(Count == colours.length)
                {
                    Toast.makeText(getBaseContext(), "You have completed guessing the words.", Toast.LENGTH_LONG).show();
                    Intent back = new Intent(this,Category.class);
                    startActivity(back);
                }
            }
            else
            {
                MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
                mediaPlayer.start();
                Toast.makeText(getBaseContext(), "Incorrect, Guess again", Toast.LENGTH_LONG).show();
                Colour.setText("");
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Guess a word!", Toast.LENGTH_LONG).show();
        }

    }

    //NextPic method to change picture
    public void NextPic()
    {
        Count++;
        if(Count == colourAnswers.length)
        {
            Count=0;

            MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.applause);
            mediaPlayer.start();

            Toast.makeText(getBaseContext(),"Congratulations. All the colours have been guessed.",Toast.LENGTH_LONG).show();

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
            Picture.setImageResource(colours[Count]);
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
