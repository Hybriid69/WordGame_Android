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

public class AdminColour extends AppCompatActivity {
    ImageView imageView;
    SQLiteDatabase db;
    EditText image_uri, colourname;
    private static int SELECT_PICTURE = 1;
    Button add_image1, insert;
    DbContext DB;
    public Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_colour);
        add_image1 = findViewById(R.id.buttongetcolourImage);
        insert = findViewById(R.id.buttonAdminAddColour);
        colourname=findViewById(R.id.textcolourname);

        //initialize views
        imageView  = findViewById(R.id.imageViewcolour);
        image_uri = findViewById(R.id.colourNameUri);

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
        if (colourname.getText().toString().isEmpty()) {
            colourname.setError("Field cannot be empty");
            return false;
        }
        if (image_uri.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please add image",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void insertimg()
    {
        String name= colourname.getText().toString();
        DB = new DbContext(this);
        imageView.setDrawingCacheEnabled(true);

        //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        boolean isInserted = DB.insertColour(name,byteArray);

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageURI = data.getData();

                image_uri.setText(selectedImageURI.toString());
                Picasso.with(getBaseContext()).load(selectedImageURI).noPlaceholder().centerCrop().fit()
                        .into((ImageView) findViewById(R.id.imageViewcolour));

            }
        }
    }
}