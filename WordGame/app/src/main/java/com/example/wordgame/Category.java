package com.example.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Category extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }
    //Links to Animals.java
    public void Animals(View view)
    {
        Intent an = new Intent(this,Animals.class);
        startActivity(an);
    }

    //Links to Colous.java
    public void Colours(View view)
    {
        Intent an = new Intent(this,Colours.class);
        startActivity(an);
    }

    //Links to Countries.java
    public void Countries(View view)
    {
        Intent an = new Intent(this,Countries.class);
        startActivity(an);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Here lies the problem
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void exitGame(View view)
    {
        Intent menu = new Intent(this,MenuOptions.class);
        startActivity(menu);
    }
    /*public void exitGame(View view)
    {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit this game?").setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                //Category.this.finish();
                System.exit(0);
            }
        })
                .setNegativeButton("No",null)
                .show();
    }*/
}