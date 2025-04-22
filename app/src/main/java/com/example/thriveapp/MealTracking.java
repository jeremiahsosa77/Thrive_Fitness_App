package com.example.thriveapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
            if(!loaded){
                listOfNutrients();//makes list of buttons for each nutrient
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
        String nutrientName = nutrientText.getText().toString().trim();
        int dataAdded =  Integer.parseInt(DataField.getText().toString());

        if(!nutrientExists(nutrientName) || dataAdded < 0) {
            return;}
        int data = dataAdded;
        dbMealHelper.addData(nutrientName, data);
    }

    public void addNutrient(View button){

        EditText nutrientNameContainer = (EditText) findViewById(R.id.NutrientNameInput);

        String nutrientName = nutrientNameContainer.getText().toString();
        if(nutrientName == "" || nutrientName == "Name") {
            return;//empty
        }
        if(!dbMealHelper.addNutrient(nutrientName)) {
            System.out.println("Saves not Succesfully");
        }


        Button nutrientButton = new Button(this);
        Drawable background = getResources().getDrawable(R.drawable.button_rounded);
        button.setBackground(background);

        nutrientButton.setText(nutrientName);
        nutrientButton.setTextSize(23);
        nutrientButton.setTextColor(Color.WHITE);
        nutrientButton.setOnClickListener(this::ClickedNutrient);
        nutrientButton.setTextColor(Color.rgb(0,0,0));

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
    public void listOfNutrients() {
        String[] nutrients = dbMealHelper.getAllTrackedNutrients();
        for (var nutrientName : nutrients) {
            // android:id="@+id/buttonContainer";
            if (!Objects.equals(nutrientName, null)) {
                LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

                Button button = new Button(this);
                Drawable background = getResources().getDrawable(R.drawable.button_rounded);
                button.setBackground(background);

                button.setText(nutrientName);
                button.setTextSize(23);
                button.setTextColor(Color.WHITE);
                button.setOnClickListener(this::ClickedNutrient);
                buttonContainer.addView(button);
            }
        }

    }
    
    
    
}
