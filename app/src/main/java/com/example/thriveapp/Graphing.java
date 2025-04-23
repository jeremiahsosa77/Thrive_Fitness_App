package com.example.thriveapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private DatabaseHelper dbHelper;
    private DatabaseTaskHelper taskHelper;
    private Map<Float, String> timestampMap = new HashMap<>(); // x -> date map for graph points

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing); // Set layout file for this screen

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Handle padding for system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init helpers for DB and logic
        dbHelper = new DatabaseHelper(this);
        taskHelper = new DatabaseTaskHelper(this);

        setupTaskSpinner(); // Show dropdown to choose exercise
    }

    // Setup dropdown spinner to choose a task to graph
    private void setupTaskSpinner() {
        Spinner spinner = findViewById(R.id.taskSelector);
        String[] tasks = taskHelper.getAllTasks(); // Fetch available task names from DB

        if (tasks == null || tasks.length == 0) return;

        // Bind data to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tasks);
        spinner.setAdapter(adapter);

        // Set what happens when user selects a task
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTask = parent.getItemAtPosition(position).toString();
                drawWorkoutGraph(selectedTask); // Draw graph for selected task
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Draw a workout graph using data for selected task
    private void drawWorkoutGraph(String taskName) {
        LineChart chart = findViewById(R.id.workoutChart);
        List<Entry> entries = new ArrayList<>();
        timestampMap.clear(); // Clear old points
        System.out.println("kree3m");
        // Get data from DB for selected task
        int[] reps = taskHelper.getRepsData(taskName);
        System.out.println(taskName);
        String[] dates = taskHelper.getDateArray(taskName);
        if (reps == null || dates == null) return;
        System.out.println("kreem");

        // Add each point to graph
        for (int i = 0; i < reps.length; i++) {
            entries.add(new Entry(i, reps[i]));
            timestampMap.put((float) i, i < dates.length ? dates[i] : "Unknown");
        }

        // Style the line graph
        LineDataSet dataSet = new LineDataSet(entries, taskName + " Reps");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(6f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData); // Add dataset to chart

        // Configure chart's X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getDescription().setEnabled(false); // Hide chart description
        chart.invalidate(); // Refresh chart

        // Show pop-up info when point is tapped
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                String timestamp = timestampMap.getOrDefault(x, "Unknown");

                new AlertDialog.Builder(Graphing.this)
                        .setTitle("Workout Info")
                        .setMessage("Date: " + timestamp + "\nReps: " + (int) e.getY() + "\nLength: ~45 min")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onNothingSelected() {}
        });
    }

    // Show recommendation text based on user's fitness goal


    // Handle top-left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}