//************************************
//Program Name: DataBaseMealHelper.java
//Developer: Jacob Zimmerhanzel & Matthias Talbert
//Date Created: 04/12/2025
//Version: 3
//Purpose: Graphs information logged by the user for their exercise stats
//************************************

package com.example.thriveapp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    //declaring database helpers to get data
    private DatabaseHelper dbHelper;
    private DatabaseTaskHelper taskHelper;
    private Map<Float, String> timestampMap = new HashMap<>(); // x -> date map for graph points

    //name of selected task
    private String taskName;
    //selected category of data (weight, repetitions or time)
    private String taskCategory;


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

    // Setup dropdown spinner to choose a task and dataset to graph
    private void setupTaskSpinner() {
        //spinners can be used to select what will be graphed
        Spinner spinnerTask = findViewById(R.id.TaskSelector);
        String[] tasks = taskHelper.getAllTasks(); // Fetch available task names from DB

        Spinner spinnerCategory = findViewById(R.id.CategorySelector);
        String[] categories = {"Weight", "Repetitions", "Time"};



        //checks to see if there is anything to graph
        if (tasks == null || tasks.length == 0) return;
        taskName = tasks[0];
        taskCategory = categories[0];

        //global variables. Placed here instead of onCreate for convenience



        // Bind data to spinnerTask
        final ArrayAdapter<String>[] adapter = new ArrayAdapter[]{new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tasks)};
        spinnerTask.setAdapter(adapter[0]);

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapterCategories);

        // Set what happens when user selects a task
        spinnerTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskName = parent.getItemAtPosition(position).toString();
                drawWorkoutGraph(); // Draw graph for selected task
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskCategory = parent.getItemAtPosition(position).toString();
                drawWorkoutGraph(); // Draw graph for selected task
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Draw a workout graph using data for selected task
    private void drawWorkoutGraph() {
        LineChart chart = findViewById(R.id.workoutChart);
        List<Entry> entries = new ArrayList<>();
        timestampMap.clear(); // Clear old points
        //list of dates the user logged activity
        String[] dates = taskHelper.getDateArray(taskName);
        //list of task data
        int[] dataList;
        //time is of type float so a different array is used
        double[] timeDataList = taskHelper.getTimeData(taskName);
        //get appropriate data for the task and category

        switch(taskCategory){
            case "Weight":
                dataList = taskHelper.getWeightData(taskName);
                break;
            case "Repetitions":
                dataList = taskHelper.getRepsData(taskName);

                break;
            default:
                dataList = new int[]{0};
                break;
        }

        if(!taskCategory.equals("Time")){
            if (dataList == null || dates == null) return;
            // Add each point to graph
            for (int i = 0; i < dataList.length; i++) {
                entries.add(new Entry(i, dataList[i]));
                //ternary function required for imported function
                timestampMap.put((float) i, i < dates.length ? dates[i] : "Unknown");
            }
        }
        else{
            if(timeDataList == null || dates == null) return;

            for(int i = 0; i < timeDataList.length;i++){
                entries.add(new Entry(i, (float) timeDataList[i]));
                //ternary function required for imported function
                timestampMap.put((float) i, i < dates.length ? dates[i] : "Unknown");
            }

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