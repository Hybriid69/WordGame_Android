package com.example.wordgame;

import android.os.Build;
import android.transition.ChangeBounds;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class TransitionClass extends AppCompatActivity {
    //////////////////////////////////////////////////////
    //Parent Activity
    //////////////////////////////////////////////////////
    // open activity using transition
    android.transition.Transition closeTransition() {
        // Check for Android 5.0 or higher
        ChangeBounds bounds = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Apply activity transition
            bounds = new ChangeBounds();
            bounds.setInterpolator(new BounceInterpolator());
            bounds.setDuration(2000);
        }
        return bounds;
    }
    // open activity using transition
    ChangeBounds reEnterTransition() {
        // Check for Android 5.0 or higher
        ChangeBounds bounds = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Apply activity transition
            bounds = new ChangeBounds();
            bounds.setInterpolator(new OvershootInterpolator());
            bounds.setDuration(2000);
        }
        return bounds;
    }

    /////////////////////////////////////////////////////// Second Activity

    // start activity using transition
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    ChangeBounds startTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(2000);
        return bounds;
    }

    // start activity using transition
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    ChangeBounds exitTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(2000);

        return bounds;
    }

}
