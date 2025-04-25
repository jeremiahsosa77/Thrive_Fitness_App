package com.example.thriveapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Calendar;

import java.util.Date;
//************************************
//Program Name: DataBaseMealHelper.java
//Developer: Jacob Zimmerhanzel
//Date Created: 04/18/2025
//Version: 3.2
//Purpose: Provides methods and a database for the meal tracking menu
//************************************
public class DatabaseMealHelper extends SQLiteOpenHelper {
    //variables to set the meal database
    private static final String DATABASE_NAME = "ThriveNutrientDB";
    private static final String COL_ID = "id"; // row id
    private static final String COL_NUTRIENT = "nutrient";//name of the nutrient
    private static final String COL_DATA = "data"; //nutrients for meal

    private static final String COL_DATE = "date";//when the user logged their exercise

    public DatabaseMealHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }//make sure to increment version (last int value) each time the database structure is changed
    //calls when class is created in another java class
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DATABASE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUTRIENT + " TEXT UNIQUE, " +
                COL_DATA + " TEXT, " +
                COL_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    //updates the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    //gets number of exercises/rows in the database
    private int getNumberOfRows() {
        String countQuery = "SELECT  * FROM " + DATABASE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //converts the string version of nutrien data into an array of its
    private int[] stringToIntArray(String[] dataString){
        int[] dataArray = new int[dataString.length];
        int i = 0; //position in int array
        for(String singleData : dataString){
            dataArray[i++] = Integer.parseInt(singleData);
        }
        return dataArray;
    }

    //gets an int array of data associated with a nutrient
    public int[] getNutrientData(String nutrient) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT data FROM " + DATABASE_NAME + " WHERE nutrient=?", new String[]{nutrient});
        if (cursor.moveToFirst()) {
            String[] data = cursor.getString(0).split(",");
            data[0] = data[0].replace("data","");
            cursor.close();
            if(data.length == 1 && data[0] == "")
                return new int[]{0};
            return stringToIntArray(data);
        }
        cursor.close();
        return null; // Return null if user not found
    }

    //gets the dates of when the user logged information
    public String[] getDateArray(String nutrientName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date FROM "+DATABASE_NAME+" WHERE nutrient=?", new String[]{nutrientName});
        if (cursor.moveToFirst()) {
            String[] dates = cursor.getString(0).split(",");
            dates[0] = dates[0].replace("date","");
            cursor.close();
            return dates;
        }
        cursor.close();
        return null;
    }

    //gets date string to add new dates to the database
    private String getDateString(String nutrientParam) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT date FROM " + DATABASE_NAME + " WHERE nutrient=?", new String[]{nutrientParam});

        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(0); // Get the user's name
            cursor.close();
            return data;
        }
        cursor.close();
        return null; // Return null if user not found
    }

    // Adds new nutrient to database. string parameter is name of nutrient
    public boolean addNutrient(String nutrient) {// doesnt seem to work
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NUTRIENT, nutrient);
        values.put(COL_DATA, "");
        values.put(COL_DATE, "");

        long result = db.insert(DATABASE_NAME, null, values);
        return result != -1; // Returns true if successful
    }

    //currently gets the String version of the user data from the database
    private String getDataString(String nutrientName) {
        SQLiteDatabase db = this.getReadableDatabase();

        //probably doesnt work since i dont know how rawQuery works
        Cursor cursor = db.rawQuery("SELECT '"+COL_DATA+ "' FROM "+DATABASE_NAME+" WHERE  nutrient=?", new String[]{nutrientName});
        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(0); // Get the user's data
            cursor.close();
            return data;
        }
        cursor.close();
        return null; // Return null if user not found
    }

    //gets current date as a formatted string
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH) + 1;
        int year = calendar.get(calendar.YEAR);
        String currentDate = "";
        currentDate = month + "/" + day + "/" + year;

        return currentDate;
    }


    //adds data to nutrient. string is nutrient name, int data is the data to add to end of list
    public void addData(String nutrient,int data){
        //gets information already written and adds on to it
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(data >=0){
            String dataString = getDataString(nutrient);
            dataString += Integer.toString(data);
            dataString += ",";
            values.put(COL_DATA, dataString);
        }
        String date = getDateString(nutrient);
        date += getCurrentDate();
        date += ",";
        values.put(COL_DATE, date);
        db.update(DATABASE_NAME, values, "nutrient=?", new String[]{nutrient});
    }
    public String[] getAllTrackedNutrients(){
        String[] nutrientNames = new String[getNumberOfRows()];
        String selectQuery = "SELECT nutrient FROM " + DATABASE_NAME;
        int arrayPosition = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            nutrientNames[arrayPosition] = cursor.getString(0);
            arrayPosition += 1;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return nutrientNames;
    }




}
