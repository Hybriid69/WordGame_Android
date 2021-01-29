package com.example.wordgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        performAnimation(R.anim.spin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performAnimation(int animationResourceID)
    {
        // play opening audio
        MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.start);
        mediaPlayer.start();
        // We will animate the imageview
        ImageView reusableImageView = (ImageView) findViewById(R.id.splashscreen);
        reusableImageView.setImageResource(R.drawable.logo2);
        reusableImageView.setVisibility(View.VISIBLE);

        // Load the appropriate animation
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        // Register a listener, so we can disable and re-enable buttons
        an.setAnimationListener(new MyAnimationListener());
        // Start the animation
        reusableImageView.startAnimation(an);
    }

    /*private void toggleButtons(boolean clickableState)
    {
        final Button spinButton = (Button) findViewById(R.id.btnWelcome);
        spinButton.setClickable(clickableState);
    }*/

    class MyAnimationListener implements Animation.AnimationListener
    {

        public void onAnimationEnd(Animation animation)
        {
            // Hide our ImageView
            ImageView reusableImageView = (ImageView) findViewById(R.id.splashscreen);
            reusableImageView.setVisibility(View.INVISIBLE);

            // Enable all buttons once animation is over
            //toggleButtons(true);

            Intent res = new Intent(getApplicationContext(),MenuOptions.class);
            startActivity(res);

        }

        public void onAnimationRepeat(Animation animation)
        {
            // what to do when animation loops
        }

        public void onAnimationStart(Animation animation)
        {
            // Disable all buttons while animation is running
            //toggleButtons(false);


        }
    }
}
