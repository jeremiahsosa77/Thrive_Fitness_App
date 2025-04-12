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

    private static final String COL_TASK = "task";
    private static final String COL_DATA = "data";

    private static final String COL_DATE = "date";

    public DatabaseTaskHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
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
    private int[] stringToArray(String dataString){
        int[] dataArray = new int[dataString.length()];
        for(int i = 0; i < dataString.length();i++){
            dataArray[i] = dataString.charAt(i*2);
        }
        return dataArray;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DATABASE_NAME + " (" +
                COL_TASK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATA + " TEXT, " +
                COL_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    // Adds new task to database. string parameter is name of task
    public boolean addTask(String task) {// doesnt seem to work
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK, task);
        values.put(COL_DATA, "");
        values.put(COL_DATE, "");

        long result = db.insert(DATABASE_NAME, null, values);
        return result != -1; // Returns true if successful
    }

    //currently gets the String version of the user data from the database
    private String getDataString(String task) {
        SQLiteDatabase db = this.getReadableDatabase();

        //probably doesnt work since i dont know how rawQuery works
        Cursor cursor = db.rawQuery("SELECT data FROM task WHERE task=?", new String[]{task});

        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(0); // Get the user's name
            cursor.close();
            return data;
        }
        cursor.close();
        return null; // Return null if user not found
    }

    //gets data of the task given from the string. returns int array.
    public int[] getData(String task) {
        SQLiteDatabase db = this.getReadableDatabase();

        //probably doesnt work since i dont know how rawQuery works
        Cursor cursor = db.rawQuery("SELECT data FROM task WHERE task=?", new String[]{task});

        if (cursor.moveToFirst()) { // If a user is found
            String data = cursor.getString(0); // Get the user's name
            cursor.close();
            return stringToArray(data);
        }
        cursor.close();
        return null; // Return null if user not found
    }

    //adds data to task. string is task name, int data is the data to add to end of list
public void addData(String task, int data){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    String dataString = getDataString(task);
    dataString += data;
    dataString += ",";
//TODO just noticed this was wrong, will fix at meeting. above is good below was bad
}
//get all task names to create appropriate buttons
public String[] getAllTasks(){ //this seems to work fine, information seems to not being added. check addTask()
        System.out.println("Get All Tasks");
    String[] taskNames = new String[5]; //TODO: change to the actual number of tasks and not arbitrary large size
    String selectQuery = "SELECT task FROM " + DATABASE_NAME; //+ DATABASE_NAME;
    int arrayPosition = 0;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    cursor.moveToFirst();

    while (!cursor.isAfterLast())
            {
                System.out.println(cursor.isAfterLast());

                taskNames[arrayPosition] = cursor.getString(1); //this is probably the wrong column
                System.out.println(taskNames[arrayPosition]);
                arrayPosition += 1;
                cursor.moveToNext();

            }

    cursor.close();
    db.close();


    return taskNames;

}

}