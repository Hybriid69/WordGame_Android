package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AdminCountry extends AppCompatActivity {
    ImageView imageView;
    EditText image_uri, countryname;
    private static int SELECT_PICTURE = 1;
    Button add_image1, insert;
    DbContext DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_country);
        add_image1 = findViewById(R.id.buttongetcountryImage);
        insert = findViewById(R.id.buttonAdminAddCountry);
        countryname=findViewById(R.id.textcountryname);

        //initialize views
        imageView  = findViewById(R.id.imageViewCountry);
        image_uri = findViewById(R.id.imageCountryuri);
        // select image from file explorer
        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        //button click event
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
        if (countryname.getText().toString().isEmpty()) {
            countryname.setError("Field cannot be empty");
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
        String name= countryname.getText().toString();
        DB = new DbContext(this);
        imageView.setDrawingCacheEnabled(true);

        //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        boolean isInserted = DB.insertCountry(name,byteArray);

        if (isInserted == true) {
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
                        .into((ImageView) findViewById(R.id.imageViewCountry));

            }
        }
    }
}