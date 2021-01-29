package com.example.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class matchthesynonyms extends Activity
{
    static final int READ_BLOCK_SIZE = 100;
    int currentSynString;
    int currentWrdString;

    TextView txWord;
    TextView txSync;

    String [] arrSyn = new String[10];
    String [] arrWord = new String[10];
    int TotalMoveSyn;
    int TotalMoveWord;
    int checkCount;
    Button Exit;
    boolean Flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchthesynonyms);

        writeSynonymFile();
        writeFile();

        ClickReadSynonym(arrSyn);
        ClickRead(arrWord);

        //currWord();
        txWord =(TextView)findViewById(R.id.txtWor);
        txWord.setText(arrWord[TotalMoveWord]);

        txSync = (TextView)findViewById(R.id.txtSynonym);
        txSync.setText(arrSyn[TotalMoveSyn]);

        Exit = (Button)findViewById(R.id.btnmenu);

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getBaseContext(),MenuOptions.class);
                startActivity(menu);
            }
        });

    }

    public void ClickRead(String[] arrPr)
    {

        // ---reading from files---
        try
        {
            FileInputStream fIn = openFileInput("words.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fIn));

            String line;
            int count = 0;
            while ((line = br.readLine()) != null)
            {
                arrPr[count] = line;
                count++;
            }

            br.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public void ClickReadSynonym(String[] arrPr)
    {
        String line="";
        // ---reading from files---
        try
        {
            FileInputStream fIn = openFileInput("synonym.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fIn));


            int count = 0;
            while ((line = br.readLine()) != null)
            {
                arrPr[count] = line;
                count++;
            }

            br.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void writeFile()
    {

        // ---writing to files---

        // This resource object created pulls the
        // array that has being created and declared in
        // the string xml file so that we can write the
        // contents of the array to the file
        //Resources res = getResources();
        String [] word = {"about", "abstract","accomplish","accumulate","administer","admit" ,"almost" ,"animated","annoy","answer"};

        //String[] utensils = res.getStringArray(R.array.arrUtensils);
        try {

            FileOutputStream fOut = openFileOutput("words.txt", MODE_PRIVATE);
            File f = new File("words.txt");

            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            if (f.exists())
            {
                Toast.makeText(getBaseContext(),"Files exists",Toast.LENGTH_LONG);
            }
            else
            {
                for (int wrt = 0; wrt < word.length; wrt++)
                {
                    osw.write(word[wrt]+"\r\n");
                    Toast.makeText(getBaseContext(), word[wrt], Toast.LENGTH_LONG);
                }
                // ---display file saved message---
            }
            osw.close();
            // ---write the string to the file---

        }
        catch (IOException ioe)
        {
            Toast.makeText(getBaseContext(),ioe.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void writeSynonymFile()
    {

        // ---writing to files---

        // This resource object created pulls the
        // array that has being created and declared in
        // the string xml file so that we can write the
        // contents of the array to the file
        //Resources res = getResources();
        String [] word = {"approximately","summary","achieve","build up","manage","confess","nearly","lively","irritate","reply"};

        //String[] utensils = res.getStringArray(R.array.arrUtensils);
        try {

            FileOutputStream fOut = openFileOutput("synonym.txt", MODE_PRIVATE);
            File f = new File("synonym.txt");

            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            if (f.exists())
            {
                Toast.makeText(getBaseContext(),"Files exists",Toast.LENGTH_LONG);
            }
            else
            {
                for (int wrt = 0; wrt < word.length; wrt++)
                {
                    osw.write(word[wrt]+"\r\n");
                    Toast.makeText(getBaseContext(), word[wrt], Toast.LENGTH_LONG);
                }
                // ---display file saved message---
            }
            osw.close();
            // ---write the string to the file---

        }
        catch (IOException ioe)
        {
            Toast.makeText(getBaseContext(),ioe.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void btnNext(View view)
    {
        moveString();
        //Log.v("asd",TotalMoveSyn+",");
    }

    public void moveString()
    {
    try
   {
       TotalMoveSyn++;
       if(TotalMoveSyn < arrSyn.length)
       {
           txSync.setText(arrSyn[TotalMoveSyn]);
       }
       else
       {
           TotalMoveSyn = 0;
           txSync.setText(arrSyn[TotalMoveSyn]);
       }

}
catch (ArrayIndexOutOfBoundsException ex)
{
    Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
}
    }

    public void btnCheck(View view)
    {

try {
    //Checks whether the index match each other
    // if the counter is more than 5 the app restart

    Log.v("synonym", TotalMoveWord + ",");
    Log.v("word", TotalMoveSyn + ",");
    Log.v("Check:",checkCount+"");
    if (TotalMoveWord == TotalMoveSyn)
    {
        checkCount=0;
        TotalMoveWord++;
        if (TotalMoveWord < arrWord.length)
        {
            txWord.setText(arrWord[TotalMoveWord]);
        }
        else if (TotalMoveWord == arrWord.length)
        {

            MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.applause);
            mediaPlayer.start();
            //TotalMoveWord=0;
            Toast.makeText(getBaseContext(), "You have successfully completed the game", Toast.LENGTH_SHORT).show();
            int secondsDelay = 1;
            final Intent mnu = new Intent(this,Category.class);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(mnu);
                    finish();
                }
            }, secondsDelay * 1000);
        }

        MediaPlayer mediaPlayer= MediaPlayer.create(matchthesynonyms.this,R.raw.correct_answer);
        mediaPlayer.start();

        Toast.makeText(getBaseContext(), "Correct! The synonym matches, continue...", Toast.LENGTH_SHORT).show();
    }
    else
    {
        checkCount++;
        if(checkCount==5)
        {
            Toast.makeText(getBaseContext(), "You have tried too many times", Toast.LENGTH_LONG).show();
            Intent in = new Intent(this,MenuOptions.class);
            startActivity(in);
        }
        else
        {
            MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.wrong);
            mediaPlayer.start();
            Toast.makeText(getBaseContext(), "Incorrect:", Toast.LENGTH_LONG).show();
        }
    }
}
catch (Exception e)
{
    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
}

    }
}
