package com.example.thriveapp;

import static androidx.compose.ui.graphics.ColorKt.Color;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

//TODO: create loop of the database to add buttons for each excercise
//TODO: add cool little button that adds new exercises

public class GoalTracking extends AppCompatActivity {
    private DatabaseTaskHelper dbTaskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal_tracking);
        dbTaskHelper = new DatabaseTaskHelper(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            System.out.println("test3");
            listOfTasks();//makes list of buttons for each task
            System.out.println("test4");

            return insets;
        });
    }
    public void ClickedExercise(View button) {
        String buttonText = ((Button)button).getText().toString();
        TextView ExerciseName = (TextView)findViewById(R.id.ExerciseName);
        ExerciseName.setText(buttonText);


    }
    public void InformationAdded(View button) {
        TextView ExerciseText = (TextView)findViewById(R.id.ExerciseName);
        EditText DataField = (EditText)findViewById(R.id.DataField);
        String taskName = ExerciseText.getText().toString().trim();
        int dataAdded =  Integer.parseInt(DataField.getText().toString());

        dbTaskHelper.addData(taskName, dataAdded);

    }

    public void addTask(View button){
        EditText taskNameContainer = (EditText) findViewById(R.id.newTaskNameInput);

        String taskName = taskNameContainer.getText().toString();
        if(taskName == "" || taskName == "Name") {
            return;//empty
        }
        if(!dbTaskHelper.addTask(taskName)) {
            System.out.println("Saves not Succesfully");
        }


        Button taskButton = new Button(this);
        taskButton.setText(taskName);
        button.setBackgroundColor(Color.rgb(46,125,50));

        taskButton.setOnClickListener(this::ClickedExercise);
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        buttonContainer.addView(taskButton);
    }

    public void listOfTasks(){
            String[] tasks = dbTaskHelper.getAllTasks();
            for(var taskName : tasks) {
                //code to create button for each task
                //might work, if not its probably the database helper getAllTasks() method

                // android:id="@+id/buttonContainer";
                if(!Objects.equals(taskName, null)) {
                    LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

                    Button button = new Button(this);
                    button.setText(taskName);
                    button.setBackgroundColor(Color.rgb(46, 125, 50));
                    button.setOnClickListener(this::ClickedExercise);
                    button.setLetterSpacing(1);
                    buttonContainer.addView(button);
                }
            }

    }

}
