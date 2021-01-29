package com.example.wordgame;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbContext extends SQLiteOpenHelper {
    //Db Name
    public static final String DATABASE_NAME = "WordWiz";

    //Db Tables and Properties
    public static final String ADMIN_TABLE = "AdminDetails";
    public static final String ADMIN_COL1 = "UserID";
    public static final String ADMIN_COL2 = "UserName";
    public static final String ADMIN_COL3 = "Password";

    public static final String USER_TABLE = "UserDetails";
    public static final String USER_COL1 = "UserID";
    public static final String USER_COL2 = "UserName";
    public static final String USER_COL3 = "Score";


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


    public DbContext(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase(); // create db
        //check if admin credentials exist in DB
        Cursor Admincheck = checkAdmin();
        if(Admincheck.getCount()  < 1)
        {
            admin(); // if not- insert them
        }

    }

    //create db tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ADMIN_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, UserName TEXT, Password Text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, UserName TEXT, Score INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ANIMAL_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, AnimalName TEXT, AnimalImage BLOB )");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + COLOUR_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ColourName TEXT, ColourImage BLOB)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + COUNTRY_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CountryName TEXT, CountryImage BLOB)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + WORD_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ActualWord TEXT, Synonym TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ADMIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ANIMAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLOUR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WORD_TABLE);
        onCreate(db);
    }


    // Add users to the database to track their scores
    public boolean insertRegistrationData(String username, Integer score) {
        SQLiteDatabase db = this.getWritableDatabase(); // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL2, username);
        contentValues.put(USER_COL3, score);
        long result = db.insert(USER_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Animal to DB
    public boolean insertAnimal(String name, byte[] location)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // check  db
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANIMAL_COL2, name);
        contentValues.put(ANIMAL_COL3, location);
        long result = db.insert(ANIMAL_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //Insert Colour to DB
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

    //Insert Country to DB
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

    //Insert Synonym to DB
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

    //////////////////////////////////////// Retrieve   /////////////////////////////////////////////
    // get Admin Credentials for seeding
    public Cursor checkAdmin() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select * from " + ADMIN_TABLE+" where "+ ADMIN_COL2 + " = " +
                "'Admin'", null);
        return result;
    }
    // get Admin Credentials
    public Cursor getAdmin(String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result =
                db.rawQuery("select * from " + ADMIN_TABLE+" where "+ ADMIN_COL3 + " = '"+password+"'",
                        null);
        return result;
    }

    //Get users from DB
    public Cursor getUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select * from " + USER_TABLE, null);
        return result;
    }
    //get current score
    public Cursor getCurrentScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select currentscore from " + USER_TABLE, null);
        return result;
    }
    //get highest score
    public Cursor getHighScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select MAX (Score) from " + USER_TABLE, null);
        return result;
    }

    //get Animal data
    public Cursor getAnimal() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql ="select * from " + ANIMAL_TABLE ;
        Cursor result = db.rawQuery(sql, new String[] {});
        return result;

    }

    //get Colour data
    public Cursor getColour() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql ="select * from " + COLOUR_TABLE ;
        Cursor result = db.rawQuery(sql, new String[] {});
        return result;
    }

    //get Country data
    public Cursor getCountry() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql ="select * from " + COUNTRY_TABLE ;
        Cursor result = db.rawQuery(sql, new String[] {});
        return result;
    }

    //get Synonym data
    public Cursor getSynonym() {
        SQLiteDatabase db = getReadableDatabase();
        String sql ="select * from " + WORD_TABLE ;
        Cursor result = db.rawQuery(sql,null);

        return result;
    }
    // get actual word
    public Cursor checkWord(String actualword) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("select ActualWord from " + WORD_TABLE +" where "+ WORD_COL2 + " = '" + actualword + "'", null);
        return result;
    }
    // get synonym equivalent
    public Cursor checkSynonym(String synonym) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result =
                db.rawQuery("select Synonym from " + WORD_TABLE +" where "+ WORD_COL3 + " = '" + synonym + "'", null);
        return result;
    }


    ////////////////////////////////////////////// Update /////////////////////////////////////////
//Update User score
public Boolean updateCurrentScore(String Username, Integer score) {
    SQLiteDatabase db = getReadableDatabase();  // check db
    ContentValues contentValues = new ContentValues();
    contentValues.put(USER_COL3, score);
    long result = db.update(USER_TABLE, contentValues, USER_COL2+" = '" + Username + "'", null);

    if (result == -1) {
        return false;
    } else {
        return true;
    }
}
    //////////////////////////////////////////// Update/////////////////////////////////////////////
    // Update User score
    public Boolean updateUserScore(String UserName, int Score) {
        SQLiteDatabase db = getReadableDatabase();  // check db
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL3, Score);
        long result = db.update(USER_TABLE,contentValues,USER_COL2+" = '" + UserName + "'",null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
////////////////////////////////////////////// Seed database ////////////////////////////////////
//Insert Admin Credentials
    public boolean admin() {
        SQLiteDatabase db = this.getWritableDatabase(); // check  db
        ContentValues contentValues = new ContentValues();
        contentValues.put(ADMIN_COL2, "Admin");
        contentValues.put(ADMIN_COL3, "Password01");
        long result = db.insert(ADMIN_TABLE, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
        }

}
