package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminSynonym extends AppCompatActivity {
    DbContext DB;
    Button Add;
    EditText actualword, synonym ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_synonym);
        DB = new DbContext(this);
        Add = (Button) findViewById(R.id.buttonAdd);
        actualword= (EditText) findViewById(R.id.textactualword);
        synonym = (EditText) findViewById(R.id.textsynonym);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateActualWord() && validateSynonym()) {
                    addData();
                }
            }
        });
    }

    ////////// check if the actual word already exists
    private Boolean validateActualWord() {
        String ActualWord = actualword.getText().toString();
        DB = new DbContext(AdminSynonym.this);
        Cursor res = DB.checkWord(ActualWord);
        if (actualword.getText().toString().isEmpty()) {
            actualword.setError("Field cannot be empty");
            return false;
        }
        if (res.getCount() > 0) {
            actualword.setError("Word already in use");
            return false;
        } else {
            actualword.setError(null);
            return true;
        }
    }

    ////////// check if the synonym already exists
    private Boolean validateSynonym() {
        String Synonym = synonym.getText().toString();
        DB = new DbContext(AdminSynonym.this);
        Cursor res = DB.checkSynonym(Synonym);
        if (synonym.getText().toString().isEmpty()) {
            synonym.setError("Field cannot be empty");
            return false;
        }
        if (res.getCount() > 0) {
            synonym.setError("Synonym already in use");
            return false;
        } else {
            synonym.setError(null);
            return true;
        }
    }

    ///add synonym info to db
    public void addData() {
        boolean isInserted = DB.insertSynonym(actualword.getText().toString(), synonym.getText().toString() );

        if (isInserted == true) {
            Toast.makeText(AdminSynonym.this, " Synonym Data Successfully Added", Toast.LENGTH_LONG).show();
            actualword.getText().clear();
            synonym.getText().clear();
        } else
            Toast.makeText(AdminSynonym.this, "Insert Failed", Toast.LENGTH_LONG).show();

    }
}