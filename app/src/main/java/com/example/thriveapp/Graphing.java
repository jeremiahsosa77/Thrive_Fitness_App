package com.example.thriveapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graphing extends AppCompatActivity {
    private DatabaseHelper dbHelper; // For user info
    private DatabaseTaskHelper taskHelper; // For workout logs
    private Map<Float, Integer> workoutTimeMap = new HashMap<>(); // hour -> reps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this); // user info
        taskHelper = new DatabaseTaskHelper(this); // workout tasks

        drawWorkoutGraph(); // show dynamic graph
        showUserGoalRecommendation(); // show AI plan
    }

    // Show workout logs dynamically on graph
    private void drawWorkoutGraph() {
        LineChart chart = findViewById(R.id.workoutChart);
        List<Entry> entries = new ArrayList<>();

        // Fetch all task names
        String[] tasks = taskHelper.getAllTasks();
        for (String taskName : tasks) {
            if (taskName == null) continue;
            int[] repsArray = taskHelper.getData(taskName); // reps data
            if (repsArray == null) continue;

            // simulate each entry as a different hour of day
            float hour = 17f + entries.size();
            int totalReps = 0;
            for (int rep : repsArray) {
                totalReps += rep;
            }
            workoutTimeMap.put(hour, totalReps); // map for details
            entries.add(new Entry(hour, totalReps)); // add to graph
        }

        LineDataSet dataSet = new LineDataSet(entries, "Workout Summary");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(6f);
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getDescription().setEnabled(false);
        chart.invalidate();

        // show details when clicking point
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float hour = e.getX();
                int reps = workoutTimeMap.containsKey(hour) ? workoutTimeMap.get(hour) : (int) e.getY();
                new AlertDialog.Builder(Calendar.this)
                        .setTitle("Workout Details")
                        .setMessage("Time: " + (int) hour + ":00\nTotal Reps: " + reps + "\nLength: ~45 min")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onNothingSelected() {}
        });
    }

    // Show fitness recommendation based on user goal
    private void showUserGoalRecommendation() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("loggedInEmail", null);
        if (email == null) return;

        String goal = dbHelper.getUserGoal(email);
        String recommendation = "";

        if (goal == null) goal = "GENERAL FITNESS AND HEALTH";

        switch (goal.toUpperCase()) {
            case "LEANED / TONED":
                recommendation = "Circuit training, cardio, calorie deficit, high protein.";
                break;
            case "BUILD MUSCLE":
                recommendation = "Strength training, surplus diet, progressive overload.";
                break;
            case "STRENGTH & POWER":
                recommendation = "Heavy low-rep lifts, balanced macros, moderate surplus.";
                break;
            case "ATHLETIC & AGILE":
                recommendation = "Sprints, mobility, agility drills. Performance diet.";
                break;
            case "ENDURANCE & STAMINA":
                recommendation = "Cardio like running/cycling, high carbs.";
                break;
            case "GENERAL FITNESS AND HEALTH":
            default:
                recommendation = "Cardio + weights + flexibility. Balanced clean eating.";
                break;
        }

        TextView txt = findViewById(R.id.recommendationText);
        txt.setText("Goal: " + goal + "\nPlan: " + recommendation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
