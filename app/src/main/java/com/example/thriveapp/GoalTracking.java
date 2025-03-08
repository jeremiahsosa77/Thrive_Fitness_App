package com.example.thriveapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class GoalTracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void ClickedExercise(View button) {
        String buttonText = ((Button)button).getText().toString();
        TextView ExerciseName = (TextView)findViewById(R.id.ExerciseName);
        ExerciseName.setText(buttonText);


    }
    public void InformationAdded(View button) {
        EditText DataField = (EditText)findViewById(R.id.DataField);
        int dataAdded =  Integer.parseInt(DataField.getText().toString());
    }

}