package com.example.jonnyspizza;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayPizzaOrderActivity extends AppCompatActivity {

    private final static int HEADER_TEXT_SIZE = 24;
    private final static int NORMAL_TEXT_SIZE = 16;
    private final static int HEADER_PADDING = 0;
    private final static int HEADER_PADDING_BOTTOM = 24;
    private final static int INNER_PADDING_LEFT = 10;
    private final static int INNER_PADDING_RIGHT = 10;
    private final static int ITEM_PADDING = 12;
    private final static int DIVIDER_WIDTH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pizza_order);

        Pizza pizza = (Pizza) getIntent().getSerializableExtra(getString(R.string.pizza_name));
        Sub sub = (Sub) getIntent().getSerializableExtra(getString(R.string.sub_name));
        //displayOrder(pizza);
        displayItem(sub);
    }

    /**
     * Display an idea on the confirmation screen
     * @param item
     */
    private void displayItem(Item item){
        // Original layout in the activity
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        // Create layout for the entire item
        LinearLayout itemLayout = new LinearLayout(DisplayPizzaOrderActivity.this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setBackgroundResource(R.drawable.order_display_border);
        itemLayout.setPadding(ITEM_PADDING, ITEM_PADDING, ITEM_PADDING, ITEM_PADDING);

        // Create inner layout for the item name, quantity, and cost
        LinearLayout itemInnerLayout = new LinearLayout(DisplayPizzaOrderActivity.this);
        itemInnerLayout.setOrientation(LinearLayout.VERTICAL);

        // Create text for item name
        TextView itemName = new TextView(DisplayPizzaOrderActivity.this);
        itemName.setText(item.getName());
        itemName.setTextSize(HEADER_TEXT_SIZE);
        itemName.setTextColor(Color.BLACK);
        itemName.setPadding(HEADER_PADDING, HEADER_PADDING, HEADER_PADDING, HEADER_PADDING_BOTTOM);

        // Create text for item quantity
        TextView itemQuantity = new TextView(DisplayPizzaOrderActivity.this);
        itemQuantity.setText("Quantity: " + item.getQuantity());
        itemQuantity.setTextSize(NORMAL_TEXT_SIZE);
        itemQuantity.setPadding(0, 0, INNER_PADDING_RIGHT, 0);

        // Create text for total item cost
        TextView itemCost = new TextView(DisplayPizzaOrderActivity.this);
        String totalCost = String.format("$%.2f", (item.getCost() * item.getQuantity()));
        itemCost.setText("Total Cost: " + totalCost);
        itemCost.setTextSize(NORMAL_TEXT_SIZE);
        itemCost.setPadding(0, 0, INNER_PADDING_RIGHT, 0);

        // Create vertical line for divider
        View divider = new View(DisplayPizzaOrderActivity.this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(DIVIDER_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT));
        divider.setBackgroundColor(Color.BLACK);

        // Create text for description of item
        TextView itemDescription = new TextView(DisplayPizzaOrderActivity.this);
        itemDescription.setText(item.getDescription());
        itemDescription.setTextSize(NORMAL_TEXT_SIZE);
        itemDescription.setPadding(INNER_PADDING_LEFT, 0, 0, 0);

        // Add views to layouts
        itemInnerLayout.addView(itemName);
        itemInnerLayout.addView(itemQuantity);
        itemInnerLayout.addView(itemCost);

        itemLayout.addView(itemInnerLayout);
        itemLayout.addView(divider);
        itemLayout.addView(itemDescription);
        parentLayout.addView(itemLayout);
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

        sb.append(String.format("\n\n%s %.2f", Html.fromHtml("<b>Cost:</b>"), pizza.getCost()));

        sb.append("\nQuantity: " + pizza.getQuantity());
        sb.append(String.format("\nTotal Cost: %.2f", (pizza.getQuantity() * pizza.getCost())));

        displayText.setText(sb.toString());
    }
}