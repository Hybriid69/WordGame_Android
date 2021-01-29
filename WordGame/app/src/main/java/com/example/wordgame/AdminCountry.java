package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminCountry extends AppCompatActivity {
    DbContext DB;
    Button Add;
    EditText countryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_country);

        DB = new DbContext(this);
        Add = (Button) findViewById(R.id.buttonAdd);
        countryname= (EditText) findViewById(R.id.textcountryname);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCountryName()) {
                    addData();
                }
            }
        });
    }

    ////////// check if the country already exists
    private Boolean validateCountryName() {
        String Country = countryname.getText().toString();
        DB = new DbContext(AdminCountry.this);
        Cursor res = DB.checkCountryName(Country);
        if (countryname.getText().toString().isEmpty()) {
            countryname.setError("Field cannot be empty");
            return false;
        }
        if (res.getCount() > 0) {
            countryname.setError("Country already in use");
            return false;
        } else {
            countryname.setError(null);
            return true;
        }
    }

    ///add country info to db
    public void addData() {
        boolean isInserted = DB.insertCountry(countryname.getText().toString(), );

        if (isInserted == true) {
            Toast.makeText(AdminCountry.this, " Country Data Successfully Added", Toast.LENGTH_LONG).show();
            countryname.getText().clear();

        } else
            Toast.makeText(AdminCountry.this, "Insert Failed", Toast.LENGTH_LONG).show();

    }
}