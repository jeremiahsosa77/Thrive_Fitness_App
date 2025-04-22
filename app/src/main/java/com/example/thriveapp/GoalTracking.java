package com.example.thriveapp;

import static androidx.compose.ui.graphics.ColorKt.Color;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

//TODO: change access types to private when it can be tested if that breaks stuff

public class GoalTracking extends AppCompatActivity {
    private DatabaseTaskHelper dbTaskHelper = new DatabaseTaskHelper(this);
    private boolean loaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            System.out.println("workding");
            if(!loaded){
                listOfTasks();//makes list of buttons for each task
                loaded = true;
            }
            return insets;
        });
    }
    public void ClickedExercise(View button) {
        String buttonText = ((Button)button).getText().toString();
        TextView ExerciseName = (TextView)findViewById(R.id.ExerciseName);
        ExerciseName.setText(buttonText);


    }
    public void InformationAdded(View button) {
        TextView exerciseText = (TextView)findViewById(R.id.ExerciseName);
        EditText DataField = (EditText)findViewById(R.id.dataField);
        String taskName = exerciseText.getText().toString().trim();
        if(!taskExists(taskName)) {
            return;}
        int dataAdded =  Integer.parseInt(DataField.getText().toString());
        int weightData =0;
        int repsData = 0;
        float timeData = 0;
        TabLayout tab = (TabLayout)findViewById(R.id.dataType);
        int selectedTab = tab.getSelectedTabPosition();
        switch(selectedTab){
            case 0:
                weightData = dataAdded;
                break;
            case 1:
                repsData = dataAdded;
                break;
            case 2:
                timeData = dataAdded;
                break;
        }
        //check which box is selected
        dbTaskHelper.addData(taskName, weightData,repsData,timeData);

    }

    public void addTask(View button){

        EditText taskNameContainer = (EditText) findViewById(R.id.newTaskNameInput);

        String taskName = taskNameContainer.getText().toString();
        if(taskName == "" || taskName == "Name") {
            return;//empty
        }
        if(!dbTaskHelper.addTask(taskName)) {
            System.out.println("Save not Successful");
            return;
        }
        Button taskButton = new Button (this);

        Drawable background = getResources().getDrawable(R.drawable.button_rounded);
        taskButton.setBackground(background);

        taskButton.setText(taskName);
        taskButton.setTextSize(23);
        taskButton.setTextColor(Color.WHITE);

       // button.setBackgroundColor(Color.rgb(46,125,50));

        taskButton.setOnClickListener(this::ClickedExercise);
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        buttonContainer.addView(taskButton);
    }

    //returns true if task(exercise) exists
    private boolean taskExists(String checkTask){
        String[] tasks = dbTaskHelper.getAllTasks();
        for(var taskName : tasks) {
            if(taskName.equals(checkTask)){
                return true;
            }
        }
        return false;
    }
    public void listOfTasks() {
        System.out.println("Testing");
        String[] tasks = dbTaskHelper.getAllTasks();
        for (var taskName : tasks) {
            // android:id="@+id/buttonContainer";
            if (!Objects.equals(taskName, null)) {
                LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

                Button taskButton = new Button (this);

                Drawable background = getResources().getDrawable(R.drawable.button_rounded);
                taskButton.setBackground(background);

                taskButton.setText(taskName);
                taskButton.setTextSize(20);
                taskButton.setTextColor(Color.WHITE);

                taskButton.setOnClickListener(this::ClickedExercise);
                buttonContainer.addView(taskButton);
            }
        }

    }

}
