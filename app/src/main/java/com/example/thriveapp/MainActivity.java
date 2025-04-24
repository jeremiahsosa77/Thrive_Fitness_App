package com.example.thriveapp;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        // Hide default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Retrieve the logged-in user's email
        //SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        //String loggedInEmail = prefs.getString("loggedInEmail", null);

        // Dashboard grid
        GridLayout dashboardGrid = findViewById(R.id.dashboardGrid);
        LayoutInflater inflater = LayoutInflater.from(this);

        addDashboardCard(inflater, dashboardGrid, R.drawable.dumbell, "Fitness Tracking", "Log workouts & goals", GoalTracking.class);
        addDashboardCard(inflater, dashboardGrid, R.drawable.meal_tracking, "Meal Tracking", "Stay on top of nutrition", MealTracking.class);
        addDashboardCard(inflater, dashboardGrid, R.drawable.graph_image_foreground, "Progress Graphs", "Visualize progress", Graphing.class);
        addDashboardCard(inflater, dashboardGrid, R.drawable.sun_icon, "Daily Goals", "Complete your habits", DailyGoalsActivity.class);
        addDashboardCard(inflater, dashboardGrid, R.drawable.rocky_cropped, "Coach Rocky", "One step at a time", CoachRockyActivity.class);
        addDashboardCard(inflater, dashboardGrid, R.drawable.cog_icon, "Preferences", "", PreferencesActivity.class);

    } // end of onCreate

    // Add cards to dashboard
    private void addDashboardCard(LayoutInflater inflater, GridLayout parent, int iconRes, String title, String context, Class<?> activityClass) {
        View card = inflater.inflate(R.layout.view_dashboard_card, parent, false);

        ((ImageView) card.findViewById(R.id.icon)).setImageResource(iconRes);
        ((TextView) card.findViewById(R.id.title)).setText(title);
        ((TextView) card.findViewById(R.id.subtext)).setText(context);

        card.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, activityClass)));

        // Add layout params to manage spacing
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0; // auto-fit with columnCount
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(24,24,24,24);
        card.setLayoutParams(params);

        parent.addView(card);
    }


    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Clear the stored user session
            getSharedPreferences("UserSession", MODE_PRIVATE)
                    .edit()
                    .remove("loggedInEmail")
                    .apply();

            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();

            // Navigate to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}