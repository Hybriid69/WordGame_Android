package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminAnimal extends AppCompatActivity {
    DbContext DB;
    Button Add;
    EditText animalname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_animal);
        DB = new DbContext(this);
        Add = (Button) findViewById(R.id.buttonAdd);
        animalname= (EditText) findViewById(R.id.textanimalname);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAnimalName() ) {
                    addData();
                }
            }
        });
    }

    ////////// check if the animal already exists
    private Boolean validateAnimalName() {
        String Animal = animalname.getText().toString();
        DB = new DbContext(AdminAnimal.this);
        Cursor res = DB.checkAnimalName(Animal);
        if (animalname.getText().toString().isEmpty()) {
            animalname.setError("Field cannot be empty");
            return false;
        }
        if (res.getCount() > 0) {
            animalname.setError("Animal already in use");
            return false;
        } else {
            animalname.setError(null);
            return true;
        }
    }

    ///add animal info to db
    public void addData() {
        boolean isInserted = DB.insertAnimal(animalname.getText().toString(), );

        if (isInserted == true) {
            Toast.makeText(AdminAnimal.this, " Animal Data Successfully Added", Toast.LENGTH_LONG).show();
            animalname.getText().clear();

        } else
            Toast.makeText(AdminAnimal.this, "Insert Failed", Toast.LENGTH_LONG).show();

    }
}