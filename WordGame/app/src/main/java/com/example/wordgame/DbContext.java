package com.example.wordgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Blob;

public class DbContext extends SQLiteOpenHelper {

    //Db Name
    public static final String DATABASE_NAME = "WordWiz";

    //Db Tables and Properties
    public static final String USER_TABLE = "UserDetails";
    public static final String USER_COL1 = "UserID";
    public static final String USER_COL2 = "UserName";

    public static final String ANIMAL_TABLE = "Animal";
    public static final String ANIMAL_COL1= "AnimalID";
    public static final String ANIMAL_COL2= "AnimalName";
    public static final String ANIMAL_COL3= "AnimalImage";

    public static final String COLOUR_TABLE = "Colour";
    public static final String COLOUR_COL1 = "ColourID";
    public static final String COLOUR_COL2 = "ColourName";
    public static final String COLOUR_COL3 = "ColourImage";

    public static final String COUNTRY_TABLE = "Country";
    public static final String COUNTRY_COL1 = "CountryID";
    public static final String COUNTRY_COL2 = "CountryName";
    public static final String COUNTRY_COL3 = "CountryImage";

    public static final String WORD_TABLE = "Word";
    public static final String WORD_COL1 = "WordID";
    public static final String WORD_COL2 = "ActualWord";
    public static final String WORD_COL3 = "Synonym";

    public static final String SCORE_TABLE = "Score";
    public static final String SCORE_COL1 = "ScoreID";
    public static final String SCORE_COL2 = "Current Score";
    public static final String SCORE_COL3 = "Highest Score";



    public DbContext(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase(); // create db
        //check if admin credentials exist in DB
        Cursor check = checkAdmin();
        if(check.getCount() <= 1)
        {
            admin(); // if not- insert them
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, UserName TEXT)");
        db.execSQL("create table " + ANIMAL_TABLE + "(AnimalID INTEGER PRIMARY KEY AUTOINCREMENT, AnimalName TEXT, AnimalImage BLOB )");
        db.execSQL("create table " + COLOUR_TABLE + "(ColourID INTEGER PRIMARY KEY AUTOINCREMENT, ColourName TEXT, ColourImage BLOB)");
        db.execSQL("create table " + COUNTRY_TABLE + "(CountryID INTEGER PRIMARY KEY AUTOINCREMENT, CountryName TEXT, CountryImage BLOB)");
        db.execSQL("create table " + WORD_TABLE + "(WordID INTEGER PRIMARY KEY AUTOINCREMENT, ActualWord TEXT, Synonym TEXT)");
        db.execSQL("create table " + SCORE_TABLE + "(ScoreID INTEGER PRIMARY KEY AUTOINCREMENT, CurrentScore INTEGER, HighestScore INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ANIMAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLOUR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WORD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);
        onCreate(db);
    }

    public boolean admin() {
        SQLiteDatabase db = this.getWritableDatabase(); // check  db
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL2, "Admin");
        long result = db.insert(USER_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertRegistrationData(String username) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL2, username);
        long result = db.insert(USER_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Animal
    public boolean insertAnimal(String animalname, byte[] animalimage) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANIMAL_COL2, animalname);
        contentValues.put(ANIMAL_COL3, animalimage);
        long result = db.insert(ANIMAL_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Colour
    public boolean insertColour(String colourname, byte[] colourimage) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOUR_COL2, colourname);
        contentValues.put(COLOUR_COL3, colourimage);
        long result = db.insert(COLOUR_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Country
    public boolean insertCountry(String countryname, byte[]countryimage) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTRY_COL2, countryname);
        contentValues.put(COUNTRY_COL3, countryimage);
        long result = db.insert(COUNTRY_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Synonym
    public boolean insertSynonym(String ActualWord, String Synonym) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(WORD_COL2, ActualWord);
        contentValues.put(WORD_COL3, Synonym);
        long result = db.insert(WORD_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Score
    public boolean insertScore(Integer currentscore, Integer highestscore) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCORE_COL2, currentscore);
        contentValues.put(SCORE_COL3, highestscore);
        long result = db.insert(SCORE_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }


    //////////////////////////////////////// Retrieve   /////////////////////////////////////////////
    public Cursor checkAdmin() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select * from " + USER_TABLE+" where "+ USER_COL2 + " = 'Admin'", null);
        return result;
    }

    public Cursor checkCredentials(String username) {
        SQLiteDatabase db = getReadableDatabase();  // check  db
        Cursor result = db.rawQuery("select * from " + USER_TABLE+" where "+ USER_COL2 + " = '" + username+ "'", null);
        return result;
    }

    public Cursor getCurrentScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select currentscore from " + SCORE_TABLE, null);
        return result;
    }

    public Cursor getHighScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select Highestscore from " + SCORE_TABLE, null);
        return result;
    }

    public Cursor getAnimal( String animalname, Blob animalimage) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select animal from " + ANIMAL_TABLE + " where " + ANIMAL_COL2 + " = '" + animalname + "'"+" and " +ANIMAL_COL3+ " = '" + animalimage+ "'", null);
        return result;
    }

    public Cursor getColour( String colourname, Blob colourimage) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select colour from " + COLOUR_TABLE + " where " + COLOUR_COL2 + " = '" + colourname + "'"+" and " +COLOUR_COL3+ " = '" + colourimage+ "'", null);
        return result;
    }

    public Cursor getCountry( String countryname, Blob countryimage) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select country from " + COUNTRY_TABLE + " where " + COUNTRY_COL2 + " = '" + countryname + "'"+" and " +COUNTRY_COL3+ " = '" + countryimage+ "'", null);
        return result;
    }

    public Cursor getSynonym( String actualword, String synonym) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select synonym from " + WORD_TABLE + " where " + WORD_COL2 + " = '" + actualword + "'"+" and " + WORD_COL3+ " = '" + synonym+ "'", null);
        return result;
    }

    public Cursor checkWord(String actualword) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select ActualWord from " + WORD_TABLE +" where "+ WORD_COL2 + " = '" + actualword + "'", null);
        return result;
    }

    public Cursor checkSynonym(String synonym) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select Synonym from " + WORD_TABLE +" where "+ WORD_COL3 + " = '" + synonym + "'", null);
        return result;
    }

    public Cursor checkAnimalName(String animalname) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select AnimalName from " + ANIMAL_TABLE +" where "+ ANIMAL_COL2 + " = '" + animalname + "'", null);
        return result;
    }

    public Cursor checkColourName(String colourname) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select ColourName from " + COLOUR_TABLE +" where "+ COLOUR_COL2 + " = '" + colourname + "'", null);
        return result;
    }

    public Cursor checkCountryName(String countryname) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select CountryName from " + COUNTRY_TABLE +" where "+ COUNTRY_COL2 + " = '" + countryname + "'", null);
        return result;
    }

}
