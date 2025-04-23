package com.example.thriveapp;
/*
Daily Goals activity
Contains the UI for the daily goals section
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Objects;

public class DailyGoalsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goals);

        // Toolbar for back button & streak
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daily Goals");
        TextView streakText = findViewById(R.id.streakText);

        // Checkboxes for tasks
        CheckBox task1 = findViewById(R.id.task1);
        CheckBox task2 = findViewById(R.id.task2);
        CheckBox task3 = findViewById(R.id.task3);

        // Logic for handling resetting tasks on new day (daily goals)
        SharedPreferences prefs = getSharedPreferences("daily_tasks", MODE_PRIVATE);
        String lastCheckedDate = prefs.getString("last_checked_date", "");
        String today = LocalDate.now().toString();

        // If a new day, reset tasks
        if(!today.equals(lastCheckedDate)){
            task1.setChecked(false);
            task2.setChecked(false);
            task3.setChecked(false);
            prefs.edit().putString("last_checked_date", today).apply();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // back to main activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
