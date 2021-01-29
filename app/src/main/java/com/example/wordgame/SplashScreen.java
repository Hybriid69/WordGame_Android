package com.example.wordgame;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity
{
    DbContext DB;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageview=findViewById(R.id.imageViewSeed);

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
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // to perform animation of landing screen
    private void performAnimation(int animationResourceID)
    {
        // play opening audio
        MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.start);
        mediaPlayer.start();

        //  animate the imageview
        ImageView reusableImageView = (ImageView) findViewById(R.id.imgSplash);
        reusableImageView.setImageResource(R.drawable.image1);
        reusableImageView.setVisibility(View.VISIBLE);

        // Load the appropriate animation
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        // Register a listener, so we can disable and re-enable buttons
        an.setAnimationListener(new MyAnimationListener());
        // Start the animation
        reusableImageView.startAnimation(an);
    }

    class MyAnimationListener implements Animation.AnimationListener
    {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onAnimationEnd(Animation animation)
        {
            // Hide ImageView
            ImageView reusableImageView = (ImageView) findViewById(R.id.imgSplash);
            reusableImageView.setVisibility(View.INVISIBLE);
            // to seed db
            insertAnimalimg();
            insertColoursimg();
            insertCountyimg();
            insertSynonyms();

            //open activity after animation
            Intent res = new Intent(getApplicationContext(),MainActivity.class);
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
    // seed animal data
    public boolean checkAnimal()
    {
        DB = new DbContext(this);
        Cursor check = DB.getAnimal();
        if(check.getCount()  < 1)
        {
            return true;
        }
        return false;
    }
    // seed animal data
    public void insertAnimalimg() {
        int names = 0;
        if (checkAnimal()) {
            String[] animalsName = {"rat", "dog", "bird", "ant", "cat", "koala"};
            Integer[] animalsArray = {R.drawable.mouse, R.drawable.dog, R.drawable.bird,
                    R.drawable.ant, R.drawable.cat, R.drawable.koala};

            while (names < animalsName.length && names > -1) {

                Glide.with(this)
                        .load(animalsArray[names])
                        .into(new ImageViewTarget<Drawable>(imageview) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                imageview.setImageDrawable(resource);

                            }
                        });

                DB = new DbContext(this);

                //convert image to bytes
                imageview.setDrawingCacheEnabled(true);
                //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Bitmap bitmap = imageview.getDrawingCache();

                bitmap = BitmapFactory.decodeResource(this.getResources(), animalsArray[names]);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                if (byteArray != null && byteArray.length > 0) {
                    boolean isInserted = DB.insertAnimal(animalsName[names], byteArray);

                    if (isInserted) {
                        names++;
                        Log.e("animal image seed", "Image Successful");
                    } else {
                        Log.e("animal image seed", "Image Unsuccessful");
                    }
                } else {
                    Log.e("byte", "empty");
                    names = -1;
                }
                DB.close();
            }
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    ////////////////////////////////////// colours ////////////////////////////////////
// seed colour data
    public boolean checkColours()
    {
        DB = new DbContext(this);
        Cursor check = DB.getColour();
        if(check.getCount()  < 1)
        {
            return true;
        }
        return false;

    }
    // seed colour data
    public void insertColoursimg() {
        int names = 0;
        if (checkColours()) {
            String[] colourName = {"blue", "green", "grey", "purple", "red", "yellow"};
            Integer[] colourArray = {R.drawable.blue, R.drawable.green, R.drawable.grey,
                    R.drawable.purple, R.drawable.red, R.drawable.yellow};

            while (names < colourName.length && names > -1) {

                Glide.with(this)
                        .load(colourArray[names])
                        .into(new ImageViewTarget<Drawable>(imageview) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                imageview.setImageDrawable(resource);

                            }
                        });

                DB = new DbContext(this);

                //convert image to bytes
                imageview.setDrawingCacheEnabled(true);
                //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Bitmap bitmap = imageview.getDrawingCache();

                bitmap = BitmapFactory.decodeResource(this.getResources(), colourArray[names]);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                if (byteArray != null && byteArray.length > 0) {
                    boolean isInserted = DB.insertColour(colourName[names], byteArray);

                    if (isInserted) {
                        names++;
                        Log.e("colour image seed", "Image Successful");
                    } else {
                        Log.e("colour image seed", "Image Unsuccessful");
                    }
                } else {
                    Log.e("byte", "empty");
                    names = -1;
                }
                DB.close();
            }
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    ////////////////////////////////////// countries ////////////////////////////////////
// seed country data
    public boolean checkCountries()
    {
        DB = new DbContext(this);
        Cursor check = DB.getCountry();
        if(check.getCount()  < 1)
        {
            return true;
        }
        return false;
    }
    // seed country data
    public void insertCountyimg() {
        int names = 0;
        if (checkCountries()) {
         String[] countryName = {"australia", "brazil", "germany", "india", "japan", "south africa"};
            Integer[] countryArray = {R.drawable.australia, R.drawable.brazil, R.drawable.german,
                    R.drawable.india, R.drawable.japan, R.drawable.south_africa};

            while (names < countryName.length && names > -1) {

                Glide.with(this)
                        .load(countryArray[names])
                        .into(new ImageViewTarget<Drawable>(imageview) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                imageview.setImageDrawable(resource);

                            }
                        });

                DB = new DbContext(this);

                //convert image to bytes
                imageview.setDrawingCacheEnabled(true);
                //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Bitmap bitmap = imageview.getDrawingCache();

                bitmap = BitmapFactory.decodeResource(this.getResources(), countryArray[names]);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                if (byteArray != null && byteArray.length > 0) {
                    boolean isInserted = DB.insertCountry(countryName[names], byteArray);

                    if (isInserted) {
                        names++;
                        Log.e("country image seed", "Image Successful");
                    } else {
                        Log.e("country image seed", "Image Unsuccessful");
                    }
                } else {
                    Log.e("byte", "empty");
                    names = -1;
                }
                DB.close();
            }
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    ////////////////////////////////////// Synonyms ////////////////////////////////////
// seed synonyms data
    public boolean checkSynonyms()
    {
        DB = new DbContext(this);
        Cursor check = DB.getSynonym();
        if(check.getCount()  < 1)
        {
            return true;
        }
        return false;
    }
    // seed synonyms data
    public void insertSynonyms() {
        int names = 0;
        if (checkSynonyms()) {
            String[] actualName = {"Answer", "Ask", "Cry", "Crooked", "Execute",
                    "End","Good"};
            String[] synonymName = {"Reply", "Question","Weep", "Bent", "Do",
                    "Stop","Excellent"};

            while (names < actualName.length && names > -1) {

                DB = new DbContext(this);

                if (actualName .length>0) {
                    boolean isInserted = DB.insertSynonym(synonymName[names],actualName[names]);

                    if (isInserted) {
                        names++;
                        Log.e("synonym seed", "synonym Successful");
                    } else {
                        Log.e("synonym seed", "synonym Unsuccessful");
                    }
                } else {
                    Log.e("synonym", "empty");
                    names = -1;
                }
                DB.close();
            }
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

