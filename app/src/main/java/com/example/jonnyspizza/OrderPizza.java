package com.example.jonnyspizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OrderPizza extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pizza);
    }

    public void completeOrder(View view){
        RadioGroup sizeGroup = findViewById(R.id.sizeGroup);
        RadioGroup crustGroup = findViewById(R.id.crustGroup);
        RadioGroup sauceGroup = findViewById(R.id.sauceGroup);
        RadioGroup cheeseGroup = findViewById(R.id.cheeseGroup);

        int sizeId = sizeGroup.getCheckedRadioButtonId();
        int crustId = crustGroup.getCheckedRadioButtonId();
        int sauceId = sauceGroup.getCheckedRadioButtonId();
        int cheeseId = cheeseGroup.getCheckedRadioButtonId();

        boolean validInput = validateRadioGroups(sizeId, crustId, sauceId, cheeseId);

        if (validInput){
            CheckBox pepperoniBox = findViewById(R.id.pepperoniCheck);
            CheckBox baconBox = findViewById(R.id.baconCheck);
            CheckBox hamBox = findViewById(R.id.hamCheck);
            CheckBox chickenBox = findViewById(R.id.chickenCheck);
            CheckBox onionsBox = findViewById(R.id.onionsCheck);
            CheckBox greenPeppersBox = findViewById(R.id.greenPeppersCheck);

            String size = (String) ((RadioButton) findViewById(sizeId)).getText();
            String crust = (String) ((RadioButton) findViewById(crustId)).getText();
            String sauce = (String) ((RadioButton) findViewById(sauceId)).getText();
            String cheese = (String) ((RadioButton) findViewById(cheeseId)).getText();

            boolean pepperoni = pepperoniBox.isChecked();
            boolean bacon = baconBox.isChecked();
            boolean ham = hamBox.isChecked();
            boolean chicken = chickenBox.isChecked();
            boolean onion = onionsBox.isChecked();
            boolean greenPeppers = greenPeppersBox.isChecked();

            Pizza pizza = new Pizza(size, crust, sauce, cheese);
            if (pepperoni) pizza.addToppings("Pepperoni");
            if (bacon) pizza.addToppings("Bacon");
            if (ham) pizza.addToppings("Ham");
            if (chicken) pizza.addToppings("Chicken");
            if (onion) pizza.addToppings("Onions");
            if (greenPeppers) pizza.addToppings("Green Peppers");

            Intent i = new Intent(this, DisplayPizzaOrderActivity.class);
            i.putExtra("Pizza", pizza);
            startActivity(i);
        }
    }

    /**
     * Determines if the user has selected one choice from each category
     * @param sizeId
     * @param crustId
     * @param sauceId
     * @param cheeseId
     * @return true if the input is valid
     */
    private boolean validateRadioGroups(int sizeId, int crustId, int sauceId, int cheeseId){
        // if id == -1, then selection is empty
        int invalid = -1;
        String message;

        if (sizeId == invalid){
            message = "Must Select Size";
            displayInvalidToast(message);
            return false;
        }
        else if (crustId == invalid){
            message = "Must Select Crust";
            displayInvalidToast(message);
            return false;
        }
        else if(sauceId == invalid){
            message = "Must Select Sauce";
            displayInvalidToast(message);
            return false;
        }
        else if(cheeseId == invalid){
            message = "Must Select Cheese";
            displayInvalidToast(message);
            return false;
        }
        return true;
    }

    /**
     * Displays the message that one of the inputs was invalid
     * @param message
     */
    private void displayInvalidToast(String message){
        Context context = getApplicationContext();
        int duration = 100; //Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
        toast.show();
    }
}