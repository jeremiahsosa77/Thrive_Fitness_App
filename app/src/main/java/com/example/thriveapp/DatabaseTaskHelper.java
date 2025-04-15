package com.example.thriveapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseTaskHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ThriveTaskDB1";
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_TASK = "task";//name of the exercise
    private static final String COL_WEIGHT = "weight"; //weight for exercise
    private static final String COL_REPS = "reps"; //repititions for exercise
    private static final String COL_TIME = "time";//time logged for exercise

    private static final String COL_DATE = "date";//when the user logged their exercise

    public DatabaseTaskHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }


    //converts an int array into a string so it can be put into the database
    private String arrayToString(int[] dataArray) {
        //StringBuilder is more efficient for loops. assign string to stringBuild at end
        StringBuilder stringBuild = new StringBuilder();
        String dataString = "";
        for(int element : dataArray) {
            stringBuild.append(element);
            stringBuild.append(",");
        }
        dataString = stringBuild.toString();
        return dataString;
    }

    //converts the string version of task data into an array of its
    private int[] stringToIntArray(String dataString){//doesnt work
        int[] dataArray = new int[dataString.length()];
        String numberString ="";
        int y = 0; //position in int array
        for(int i = 0; i < dataString.length();i++){
            if(dataString.charAt(i) != ','){
                numberString+= dataString.charAt(i);
            }
            else {
                dataArray[y++] = Integer.parseInt(numberString);
                i++;
            }
        }
        return dataArray;
    }
    private float[] stringToFloatArray(String dataString){//doesnt work
        float[] dataArray = new float[dataString.length()];
        String numberString ="";
        int y = 0; //position in int array
        for(int i = 0; i < dataString.length();i++){
            if(dataString.charAt(i) != ','){
                numberString+= dataString.charAt(i);
            }
            else {
                dataArray[y++] = Float.parseFloat(numberString);
                i++;
            }
        }
        return dataArray;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DATABASE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK + " TEXT UNIQUE, " +
                COL_WEIGHT + " TEXT, " +
                COL_REPS + " TEXT, " +
                COL_TIME + " TEXT, " +

                COL_DATE + " TEXT)";
        db.execSQL(createTable);
    }
    public void getDate(){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    // Adds new task to database. string parameter is name of task
    public boolean addTask(String task) {// doesnt seem to work
        System.out.println("Starting to attempt to add task");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK, task);
        values.put(COL_WEIGHT, "");
        values.put(COL_REPS, "");
        values.put(COL_TIME, "");
        values.put(COL_DATE, "");

        long result = db.insert(DATABASE_NAME, null, values);
        System.out.println("finished attempt to save data");
        return result != -1; // Returns true if successful
    }

    //currently gets the String version of the user data from the database
    private String getDataString(String taskName, String dataType) {
        SQLiteDatabase db = this.getReadableDatabase();

        //probably doesnt work since i dont know how rawQuery works
        Cursor cursor = db.rawQuery("SELECT "+ dataType+" FROM "+DATABASE_NAME+" WHERE task=?", new String[]{taskName});

        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(0); // Get the user's name
            cursor.close();
            return data;
        }
        cursor.close();
        return null; // Return null if user not found
    }

    //gets data of the task given from the string. returns int array.
    public int[] getWeightData(String taskParam) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT weight FROM " + DATABASE_NAME + " WHERE task=?", new String[]{taskParam});

        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(1); // Get the user's name
            cursor.close();
            return stringToIntArray(data);
        }
        cursor.close();
        return null; // Return null if user not found
    }
    public int[] getRepsData(String taskParam) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT reps FROM " + DATABASE_NAME + " WHERE task=?", new String[]{taskParam});

        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(1); // Get the user's name
            cursor.close();
            return stringToIntArray(data);
        }
        cursor.close();
        return null; // Return null if user not found
    }
    public float[] getTimeData(String taskParam) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT time FROM " + DATABASE_NAME + " WHERE task=?", new String[]{taskParam});

        if (cursor.moveToFirst()) { // If db found
            String data = cursor.getString(1); // Get the user's name
            cursor.close();
            return stringToFloatArray(data);
        }
        cursor.close();
        return null; // Return null if unsuccessful
    }

    //adds data to task. string is task name, int data is the data to add to end of list
public void addData(String task,int weight,int reps, float time){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    if(weight >0){
        String weightDataString = getDataString(task,Integer.toString(weight));
        weightDataString += weight;
        weightDataString += ",";
        values.put(COL_WEIGHT, weightDataString);
    }
    else {
        values.put(COL_WEIGHT,getDataString(task,"weight"));
    }
    if(reps >0){
        String repsDataString = getDataString(task,Integer.toString(reps));
        repsDataString += reps;
        repsDataString += ",";
        values.put(COL_REPS, repsDataString);
    }
    else {
        values.put(COL_WEIGHT,getDataString(task,"reps"));
    }
    if(time >0){
        String timeDataString = getDataString(task,Float.toString(time));
        timeDataString += time;
        timeDataString += ",";
        values.put(COL_TIME, timeDataString);
    }
    else {
        values.put(COL_WEIGHT,getDataString(task,"time"));
    }
    db.update(DATABASE_NAME, values, "task = task", new String[]{task});//task = task is the potential issue

}
//get all task names to create appropriate buttons
public String[] getAllTasks(){ //this seems to work fine, information seems to not being added. check addTask()
    String[] taskNames = new String[20]; //TODO: change to the actual number of tasks and not arbitrary large size
    String selectQuery = "SELECT task FROM " + DATABASE_NAME;
    int arrayPosition = 0;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    cursor.moveToFirst();

    while (!cursor.isAfterLast())
            {
                taskNames[arrayPosition] = cursor.getString(0); //this is probably the wrong column
                System.out.println(taskNames[arrayPosition]);
                arrayPosition += 1;
                cursor.moveToNext();

            }

    cursor.close();
    db.close();


    return taskNames;

}

}
