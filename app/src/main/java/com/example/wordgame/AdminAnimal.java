package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AdminAnimal extends AppCompatActivity {
    // variables
    ImageView imageView;
    SQLiteDatabase db;
    EditText image_uri;
    private static int SELECT_PICTURE = 1;
    Button add_image1, insert;
    DbContext DB;
    public Bitmap bit;
    EditText animalname;
    TransitionClass transitionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // enable transition between activities
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
        setContentView(R.layout.activity_admin_animal);
        //link xml
        add_image1 = findViewById(R.id.buttonAnimalGetImage);
        insert = findViewById(R.id.buttonAdminAddAnimal);
        animalname = findViewById(R.id.textanimalname);

        //initialize views
        imageView = findViewById(R.id.imageViewAnimal);
        image_uri = findViewById(R.id.image_name);

        // get image from file explorer
        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                {
                    insertimg();
                }
            }
        });

    }

    public boolean validateFields() {
            if (animalname.getText().toString().isEmpty()) {
                animalname.setError("Field cannot be empty");
                return false;
            }
            if (image_uri.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please add image",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
    }




    // add image to db
    public void insertimg()
    {
        String name= animalname.getText().toString();
        DB = new DbContext(this);

        //convert image to bytes
        imageView.setDrawingCacheEnabled(true);
        //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // insert data
        boolean isInserted = DB.insertAnimal(name,byteArray);

        if (isInserted) {
            Toast.makeText(getApplicationContext(),"Image Successfully Added",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Image Insert Unsuccessful",Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(getBaseContext(),AdminMenu.class);
        startActivity(intent);
    }
    // display image in image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageURI = data.getData();

                image_uri.setText(selectedImageURI.toString());
                Picasso.with(getBaseContext()).load(selectedImageURI).noPlaceholder().centerCrop().fit()
                        .into((ImageView) findViewById(R.id.imageViewAnimal));

            }
        }
    }
}