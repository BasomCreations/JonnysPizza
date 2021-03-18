package com.example.jonnyspizza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DisplayPizzaOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pizza_order);

        Pizza pizza = (Pizza) getIntent().getSerializableExtra("Pizza");
        displayOrder(pizza);
    }

    private void displayOrder(Pizza pizza){
        TextView displayText = findViewById(R.id.finalOrderText);

        StringBuilder sb = new StringBuilder();
        sb.append("You ordered a ");
        sb.append(pizza.getSize().toLowerCase());
        sb.append(" pizza on ");
        sb.append(pizza.getCrust().toLowerCase());
        sb.append(" crust with ");
        sb.append(pizza.getSauce().toLowerCase());
        sb.append(" sauce, ");
        sb.append(pizza.getCheese().toLowerCase());
        sb.append(" cheese and ");

        // Toppings
        int i = 0;
        int numToppings = pizza.getToppings().size();
        // Customer did not select any toppings
        if (numToppings == 0){
            sb.append("no toppings");
        }
        // Customer only selected one topping
        else if (numToppings == 1) {
            sb.append(numToppings + " topping: ");
            sb.append(pizza.getToppings().get(i).toLowerCase());
        }
        // Customer selected multiple toppings
        else if(numToppings > 1) {
            sb.append(numToppings + " toppings: ");

            for (i = 0; i < numToppings - 1; i++){
                sb.append(pizza.getToppings().get(i).toLowerCase() + ", ");
            }
            sb.append(pizza.getToppings().get(i).toLowerCase());
        }

        displayText.setText(sb.toString());
    }
}