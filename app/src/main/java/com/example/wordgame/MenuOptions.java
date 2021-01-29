package com.example.wordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class MenuOptions extends AppCompatActivity
{

    // global variables
    Button synonym, animal, country, colour, help, exit;
    TextView head;
    ImageView toolbarImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // enable transition between activities
        TransitionClass transitionClass = new TransitionClass();
        // Check for Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(false);

            getWindow().setSharedElementEnterTransition(transitionClass.startTransition());
            getWindow().setSharedElementReturnTransition(transitionClass.exitTransition());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);
        head=findViewById(R.id.textViewhead);
        toolbarImage=findViewById(R.id.imageView6);

        // link to xml
        synonym=findViewById(R.id.buttonSynonymOption);
        animal=findViewById(R.id.buttonAnimalsOption);
        country=findViewById(R.id.buttonCountryOption);
        colour=findViewById(R.id.buttonColourOption);
        help=findViewById(R.id.buttonHelp);
        exit=findViewById(R.id.buttonLogout);

        // bring button before gif
        head.bringToFront();
        toolbarImage.bringToFront();
        synonym.bringToFront();
        animal.bringToFront();
        country.bringToFront();
        colour.bringToFront();
        help.bringToFront();
        exit.bringToFront();

        // button click listeners
        synonym.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnSyn();
        }
    });

        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animals();
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Countries();
            }
        });

        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Colours();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHelp();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // display background image
        try {
            ConstraintLayout rlMain = findViewById(R.id.optionsLayout);
            final ImageView imageView = new ImageView(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );

            imageView.setLayoutParams(params);
            rlMain.addView(imageView);

            Glide.with(this)
                    .load(R.raw.background_play)
                    .into(new ImageViewTarget<Drawable>(imageView) {
                        @Override
                        protected void setResource(@Nullable Drawable resource) {
                            imageView.setBackground(resource);

                            // bring button before gif
                            head.bringToFront();
                            toolbarImage.bringToFront();
                            head.bringToFront();
                            toolbarImage.bringToFront();
                            synonym.bringToFront();
                            animal.bringToFront();
                            country.bringToFront();
                            colour.bringToFront();
                            help.bringToFront();
                            exit.bringToFront();
                        }
                    });
            }
        catch (Exception ex)
        {
            Log.e("Main background", ex.toString());
        }
    }


    //Button click method
    public void btnSyn()
    {
        Intent start = new Intent(getApplicationContext(),matchthesynonyms.class);
        startActivity(start);
    }
    //Button click method
    public void btnHelp()
    {
        Intent help = new Intent(getApplicationContext(),Help.class);
        startActivity(help);
    }
    //Button click method
    //Links to Animals.java
    public void Animals()
    {
        Intent an = new Intent(this,Animals.class);
        startActivity(an);
    }
    //Button click method
    //Links to Colous.java
    public void Colours()
    {
        Intent an = new Intent(this,Colours.class);
        startActivity(an);
    }
    //Button click method
    //Links to Countries.java
    public void Countries()
    {
        Intent an = new Intent(this,Countries.class);
        startActivity(an);
    }
    //Button click method
    public void logout()
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

        // clear shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    // start activity using transition
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ChangeBounds startTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(2000);
        return bounds;
    }

    // start activity using transition
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ChangeBounds exitTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(2000);

        return bounds;
    }
}
