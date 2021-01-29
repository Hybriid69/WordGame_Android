package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMenu extends AppCompatActivity {
    Button synonym, animal, colour, country, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        //link xml
        synonym=(Button)findViewById(R.id.buttonSynonym);
        animal=(Button)findViewById(R.id.buttonAnimal);
        colour=(Button)findViewById(R.id.buttonColour);
        country=(Button)findViewById(R.id.buttonCountry);
        logout=(Button)findViewById(R.id.buttonLogout);

        // button click
        synonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMenu.this,AdminSynonym.class);
                startActivity(i);
            }
        });

        // button click
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMenu.this,AdminAnimal.class);
                startActivity(i);
            }
        });

        // button click
        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMenu.this,AdminColour.class);
                startActivity(i);
            }
        });

        // button click
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMenu.this,AdminCountry.class);
                startActivity(i);
            }
        });

        // button click
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMenu.this,MainActivity.class);
                startActivity(i);

                //clear shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

            }
        });


    }
}