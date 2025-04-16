package com.example.thriveapp;

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

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MealTracking extends AppCompatActivity {
    private DatabaseMealHelper dbMealHelper = new DatabaseMealHelper(this);

    private boolean loaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_tracking);
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

    public void ClickedNutrient(View button) {
        String buttonText = ((Button)button).getText().toString();
        TextView NutrientName = (TextView)findViewById(R.id.NutrientName);
        NutrientName.setText(buttonText);


    }
    public void InformationAdded(View button) {
        TextView nutrientText = (TextView)findViewById(R.id.NutrientName);
        EditText DataField = (EditText)findViewById(R.id.dataField);
        String taskName = nutrientText.getText().toString().trim();
        int dataAdded =  Integer.parseInt(DataField.getText().toString());

        if(!nutrientExists(taskName) || dataAdded < 0) {
            return;}
        int data = dataAdded;
        dbMealHelper.addData(nutrientName, data);
    }

    public void addNutrient(View button){

        EditText nutrientNameContainer = (EditText) findViewById(R.id.newNutrientNameInput);

        String nutrientName = nutrientNameContainer.getText().toString();
        if(nutrientName == "" || nutrientName == "Name") {
            return;//empty
        }
        if(!dbMealHelper.addNutrient(nutrientName)) {
            System.out.println("Saves not Succesfully");
        }


        Button nutrientButton = new Button(this);
        nutrientButton.setText(nutrientName);
        button.setBackgroundColor(Color.rgb(46,125,50));

        nutrientButton.setOnClickListener(this::ClickedNutrient);
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        buttonContainer.addView(nutrientButton);
    }

    //returns true if nutrient exists
    private boolean nutrientExists(String checknutrient){
        String[] nutrients = dbMealHelper.getAllTrackedNutrients();
        for(var nutrientName : nutrients) {
            if(nutrientName.equals(checknutrient)){
                return true;
            }
        }
        return false;
    }
    public void listOfnutrients() {
        System.out.println("Testing");
        String[] nutrients = dbMealHelper.getAllTrackedNutrients();
        for (var nutrientName : nutrients) {
            // android:id="@+id/buttonContainer";
            if (!Objects.equals(nutrientName, null)) {
                LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

                Button button = new Button(this);
                button.setText(nutrientName);
                button.setBackgroundColor(Color.rgb(46, 125, 50));
                button.setOnClickListener(this::ClickedNutrient);
                button.setLetterSpacing(1);
                buttonContainer.addView(button);
            }
        }

    }
    
    
    
}
