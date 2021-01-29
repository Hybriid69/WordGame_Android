package com.example.wordgame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.SQLData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Variables
    TransitionClass transitionClass;
    DbContext DB;
    EditText Username;
    private String checkpassword;
    boolean checkHandled;
    LinearLayout linearLayout;
    ScrollView scrollView;
    Button btn;
    TextView scoreView, previousUsers, txtHead;
    ImageView homeHead;
    static String retrievedName;
    final int backcolor=Color.rgb(205, 209, 237);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // enable transition betweeen activities
        transitionClass = new TransitionClass();
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(false);

            getWindow().setSharedElementExitTransition(transitionClass.closeTransition());
            getWindow().setSharedElementReenterTransition(transitionClass.reEnterTransition());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialize controls
        Username=(EditText)findViewById(R.id.txtUsername);
        linearLayout=(LinearLayout)findViewById(R.id.l1);
        scrollView=(ScrollView) findViewById(R.id.scrollView2);
        scoreView=(TextView)findViewById(R.id.txtScore);
        previousUsers=(TextView)findViewById(R.id.textViewPrevious);
        txtHead=(TextView)findViewById(R.id.txtHeadMain);
        homeHead=(ImageView) findViewById(R.id.imageViewHome);

        //Display highest score
        setScoreData();

        //get user details and display as buttons
        try {
            // set background colour
            linearLayout.setBackgroundColor(backcolor);

            // get previous users
            DB = new DbContext(getBaseContext());
            Cursor res = DB.getUsers();
            int i = 1;
            // res.moveToFirst();
            if (res.getCount() > 0) {
                while (res.moveToNext()) {

                    // add users to buttons
                    retrievedName=res.getColumnName(1);
                    btn = new Button(this);
                    btn .setText(res.getString(1));
                    btn .setTextSize(16);
                    btn .setId(R.id.buttontest+i);
                    //btn .setBackgroundColor(color);
                    btn.setOnClickListener(this);
                    linearLayout.addView(btn);
                    btn.bringToFront();
                    btn.setBackgroundColor(backcolor);
                    linearLayout.setBackgroundColor(backcolor);
                    linearLayout.bringToFront();
                    i++;
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("user details error: ",ex.toString());
        }

        //Edit text click listener
        Username.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                String checkName = Username.getText().toString();
                if (EditorInfo.IME_ACTION_DONE == actionId)
                {
                    if (checkName.equals("Admin")||checkName.equals("admin"))
                    {
                        // admin display
                        dialogMethod();
                        handled= true;
                        checkHandled=true;
                    }
                    else
                    {
                        // user display
                        if (validateUserText())
                        {
                            addUser();
                            handled= true;
                        }
                    }
                }
                return handled;
            }
        });

        try {
            // background image
            ConstraintLayout rlMain = findViewById(R.id.homeLayout);
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
                            Username.bringToFront();
                            scoreView.bringToFront();
                            previousUsers.bringToFront();
                            txtHead.bringToFront();
                            homeHead.bringToFront();
                            if (btn != null)
                            {
                                btn.bringToFront();
                                btn.bringToFront();
                                btn.setBackgroundColor(backcolor);
                                linearLayout.setBackgroundColor(backcolor);
                                linearLayout.bringToFront();
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.e("Main background", ex.toString());
        }
    }

    // get highest score
    public void setScoreData()
    {
    DB = new DbContext(getBaseContext());
    Cursor res = DB.getHighScore();
    if (res.getCount() >0) {
        while (res.moveToNext()) {
            //check if null
           String scoreCheck= String.valueOf(res.getString(0));
            if(!scoreCheck.equals("null"))
            {
                scoreView.setText("Highest Score: " + res.getString(0));
            }
            else
            {
                scoreView.setText("");
            }
        }
    }
    else
        {
            // if score is null
            scoreView.setText("");
        }
    }


    //Validate Admin Credentials
    public boolean validateUserText()
    {
        if (Username.getText().toString().isEmpty())
        {
            Username.setError("Username cannot be empty!");
            return false;
        }
        return true;
    }
    //Validate Admin Credentials
    public boolean validateAdmin(String password) {
        boolean isValid = false;
        DB = new DbContext(getBaseContext());
        Cursor res = DB.getAdmin(password);
        if (res.getCount() > 0) {
            isValid = true;
        }
        return isValid;
    }

    // Add user to database
    public void addUser()
    {
        try{
        int score = 0;
        DB = new DbContext(this);
        String userData = Username.getText().toString();
        boolean isInserted = DB.insertRegistrationData(userData,score);

        if (isInserted == true) {
            Toast.makeText(getBaseContext(), "User added Successfully", Toast.LENGTH_LONG).show();
            // save user shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Username", userData);
            editor.commit();

            //take new user to menu activity
            Intent i = new Intent(getBaseContext(), MenuOptions.class);
            startActivity(i);
        } else {
            Toast.makeText(getBaseContext(), "Registration Failed", Toast.LENGTH_LONG).show();
        }
        }
        catch (Exception ex)
        {
            Log.e("adding users:",ex.toString());
        }
    }

    //create admin password dialog
public void dialogMethod()
{
    // set properties of admin password control
    final TextInputEditText adminPassword = new TextInputEditText(this);
    adminPassword.setMaxLines(1);
    adminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    adminPassword.setTextColor(Color.BLACK);

    // create password dialog
    new AlertDialog.Builder(this,R.style.dialogcolour)
            .setTitle("Admin")
            .setIcon(R.drawable.image3)
            .setMessage("Enter Admin Password")
            .setView(adminPassword)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    checkpassword = adminPassword.getText().toString();
                    if(validateAdmin(checkpassword))
                    {
                        Toast.makeText(getBaseContext(), "Admin Login Successful", Toast.LENGTH_LONG).show();
                        // take user to admin menu
                        Intent intent = new Intent(getBaseContext(),AdminMenu.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Invalid Attempt", Toast.LENGTH_LONG).show();
                    }
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .show();

    }

    //previous users button click event
    public void onClick(View v) {
        //get button name
        Button button = (Button)findViewById(v.getId());
        String name=button.getText().toString();

        // save shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", name);
        editor.commit();

        // Check if Android 5.0 or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            Intent i = new Intent(getBaseContext(), MenuOptions.class);
            startActivity(i,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            // Swap without transition
            Intent i = new Intent(getBaseContext(), MenuOptions.class);
            startActivity(i);
        }
        Toast.makeText(getBaseContext(), name+" selected", Toast.LENGTH_LONG).show();
    }
}
