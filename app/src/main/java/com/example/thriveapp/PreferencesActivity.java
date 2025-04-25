package com.example.thriveapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    public static String USER_GOAL = "USER_GOAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        int[] btnIds = new int[]{
                R.id.btn_leaned, R.id.btn_build, R.id.btn_strength,
                R.id.btn_athletic, R.id.btn_endurance, R.id.btn_general
        };

        for (int id : btnIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        String goal = "";

        int id = v.getId();
        if (id == R.id.btn_leaned) {
            goal = "LEANED / TONED";
        } else if (id == R.id.btn_build) {
            goal = "BUILD MUSCLE";
        } else if (id == R.id.btn_strength) {
            goal = "STRENGTH & POWER";
        } else if (id == R.id.btn_athletic) {
            goal = "ATHLETIC & AGILE";
        } else if (id == R.id.btn_endurance) {
            goal = "ENDURANCE & STAMINA";
        } else if (id == R.id.btn_general) {
            goal = "GENERAL FITNESS AND HEALTH";
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(USER_GOAL, goal);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}