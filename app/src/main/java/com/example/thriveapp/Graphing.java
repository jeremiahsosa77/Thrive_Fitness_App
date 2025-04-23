package com.example.thriveapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    private DatabaseHelper dbHelper;
    private DatabaseTaskHelper taskHelper;
    private Map<Float, String> timestampMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing);

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        taskHelper = new DatabaseTaskHelper(this);

        drawWorkoutGraph();
    }

    private void drawWorkoutGraph() {
        LineChart chart = findViewById(R.id.workoutChart);
        List<Entry> entries = new ArrayList<>();

        Cursor cursor = taskHelper.getWorkoutLogs();
        int index = 0;
        while (cursor.moveToNext()) {
            String timestamp = cursor.getString(0);
            int reps = cursor.getInt(1);

            float x = (float) index;
            entries.add(new Entry(x, reps));
            timestampMap.put(x, timestamp);
            index++;
        }
        cursor.close();

        LineDataSet dataSet = new LineDataSet(entries, "Workout Graph");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(6f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getDescription().setEnabled(false);
        chart.invalidate();

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String timestamp = timestampMap.get(e.getX());
                Cursor statsCursor = taskHelper.getStatsByTime(timestamp);
                String message = "Time: " + timestamp + "\nTotal Reps: " + (int)e.getY();
                if (statsCursor.moveToFirst()) {
                    int sets = statsCursor.getInt(0);
                    int duration = statsCursor.getInt(1);
                    int calories = statsCursor.getInt(2);
                    message += "\nSets: " + sets + "\nLength: " + duration + " min\nCalories: " + calories;
                }
                statsCursor.close();

                new AlertDialog.Builder(Graphing.this)
                        .setTitle("Workout Info")
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onNothingSelected() {}
        });
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
