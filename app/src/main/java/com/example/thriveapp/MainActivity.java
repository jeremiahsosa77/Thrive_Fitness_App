package com.example.thriveapp;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        // Retrieve the logged-in user's email
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String loggedInEmail = prefs.getString("loggedInEmail", null);

        if (loggedInEmail != null) {
            String userName = dbHelper.getUserName(loggedInEmail);
            if (userName != null) {
                getSupportActionBar().setTitle("Hello, " + userName);
            } else {
                getSupportActionBar().setTitle("Hello, User");
            }
        } else {
            getSupportActionBar().setTitle("Hello, User");
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
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
