package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminColour extends AppCompatActivity {
    DbContext DB;
    Button Add;
    EditText colourname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_colour);

        DB = new DbContext(this);
        Add = (Button) findViewById(R.id.buttonAdd);
        colourname= (EditText) findViewById(R.id.textcolourname);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateColourName()) {
                    addData();
                }
            }
        });
    }

    ////////// check if the colour already exists
    private Boolean validateColourName() {
        String Colour = colourname.getText().toString();
        DB = new DbContext(AdminColour.this);
        Cursor res = DB.checkColourName(Colour);
        if (colourname.getText().toString().isEmpty()) {
            colourname.setError("Field cannot be empty");
            return false;
        }
        if (res.getCount() > 0) {
            colourname.setError("Colour already in use");
            return false;
        } else {
            colourname.setError(null);
            return true;
        }
    }

    ///add colour info to db
    public void addData() {
        boolean isInserted = DB.insertColour(colourname.getText().toString(), );

        if (isInserted == true) {
            Toast.makeText(AdminColour.this, " Colour Data Successfully Added", Toast.LENGTH_LONG).show();
            colourname.getText().clear();

        } else
            Toast.makeText(AdminColour.this, "Insert Failed", Toast.LENGTH_LONG).show();

    }
}