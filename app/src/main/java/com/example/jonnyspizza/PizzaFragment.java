package com.example.jonnyspizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PizzaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PizzaFragment extends Fragment {

    /**
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
     */

    /**
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
     */

    public PizzaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PizzaFragment.
     */
    // Rename and change types and number of parameters
    public static PizzaFragment newInstance() {
        PizzaFragment fragment = new PizzaFragment();
        /**Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_order_pizza, container, false);

        rootView.findViewById(R.id.decreasePizzaQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.increasePizzaQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.addPizzaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(v);
                ((OrderItemActivity)getActivity()).createNewDialog(v);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Complete Order Button Click
     * @param view
     */
    public void addToCart(View view){

        Pizza pizza = createPizza();
        addToppings(pizza);
        pizza.calculateCost();

        Cart cart = ((OrderItemActivity)getActivity()).getCart();
        cart.addItem(pizza);

        /**Intent i = new Intent(getActivity(), DisplayPizzaOrderActivity.class);
        i.putExtra(getString(R.string.pizza_name), pizza);
        startActivity(i);*/

    }

    /**
     * Creates a Pizza object based on selected radio buttons
     * @return Pizza
     */
    private Pizza createPizza(){
        // Find group for each pizza category
        RadioGroup sizeGroup = getView().findViewById(R.id.sizeGroup);
        RadioGroup crustGroup = getView().findViewById(R.id.crustGroup);
        RadioGroup sauceGroup = getView().findViewById(R.id.sauceGroup);
        RadioGroup cheeseGroup = getView().findViewById(R.id.cheeseGroup);

        // Get ID of selected button
        int sizeId = sizeGroup.getCheckedRadioButtonId();
        int crustId = crustGroup.getCheckedRadioButtonId();
        int sauceId = sauceGroup.getCheckedRadioButtonId();
        int cheeseId = cheeseGroup.getCheckedRadioButtonId();

        boolean validInput = validateRadioGroups(sizeId, crustId, sauceId, cheeseId);

        // Get value of selected button
        String size = (String) ((RadioButton) getView().findViewById(sizeId)).getText();
        String crust = (String) ((RadioButton) getView().findViewById(crustId)).getText();
        String sauce = (String) ((RadioButton) getView().findViewById(sauceId)).getText();
        String cheese = (String) ((RadioButton) getView().findViewById(cheeseId)).getText();

        // Get quantity
        TextView quantityText = getView().findViewById(R.id.quantityPizzaText);
        int quantity = Integer.parseInt(quantityText.getText().toString());

        Pizza pizza = new Pizza(quantity, size, crust, sauce, cheese);

        return pizza;
    }

    /**
     * Adds selected toppings to a Pizza object
     * @param pizza
     */
    private void addToppings(Pizza pizza){
        // Gather all toppings checkboxes
        CheckBox pepperoniBox = getView().findViewById(R.id.pepperoniCheck);
        CheckBox baconBox = getView().findViewById(R.id.baconCheck);
        CheckBox sausageBox = getView().findViewById(R.id.sausageCheck);
        CheckBox phillySteakBox = getView().findViewById(R.id.phillySteakCheck);
        CheckBox hamBox = getView().findViewById(R.id.hamCheck);
        CheckBox chickenBox = getView().findViewById(R.id.chickenCheck);
        CheckBox mushroomBox = getView().findViewById(R.id.mushroomCheck);
        CheckBox pineappleBox = getView().findViewById(R.id.pineappleCheck);
        CheckBox onionsBox = getView().findViewById(R.id.onionsCheck);
        CheckBox greenPeppersBox = getView().findViewById(R.id.greenPeppersCheck);
        CheckBox jalapenoBox = getView().findViewById(R.id.jalapenoCheck);
        CheckBox spinachBox = getView().findViewById(R.id.spinachCheck);

        // Determined if selected
        boolean pepperoni = pepperoniBox.isChecked();
        boolean bacon = baconBox.isChecked();
        boolean sausage = sausageBox.isChecked();
        boolean phillySteak = phillySteakBox.isChecked();
        boolean ham = hamBox.isChecked();
        boolean chicken = chickenBox.isChecked();
        boolean mushrooms = mushroomBox.isChecked();
        boolean pineapples = pineappleBox.isChecked();
        boolean onion = onionsBox.isChecked();
        boolean greenPeppers = greenPeppersBox.isChecked();
        boolean jalapenos = jalapenoBox.isChecked();
        boolean spinach = spinachBox.isChecked();

        // Add selected toppings
        if (pepperoni) pizza.addToppings("Pepperoni");
        if (bacon) pizza.addToppings("Bacon");
        if (sausage) pizza.addToppings("Sausage");
        if (phillySteak) pizza.addToppings("Philly Steak");
        if (ham) pizza.addToppings("Ham");
        if (chicken) pizza.addToppings("Chicken");
        if (mushrooms) pizza.addToppings("Mushrooms");
        if (pineapples) pizza.addToppings("Pineapples");
        if (onion) pizza.addToppings("Onions");
        if (greenPeppers) pizza.addToppings("Green Peppers");
        if (jalapenos) pizza.addToppings("Jalapeno Peppers");
        if (spinach) pizza.addToppings("Spinach");
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
            //displayInvalidToast(message);
            return false;
        }
        else if (crustId == invalid){
            message = "Must Select Crust";
            //displayInvalidToast(message);
            return false;
        }
        else if(sauceId == invalid){
            message = "Must Select Sauce";
            //displayInvalidToast(message);
            return false;
        }
        else if(cheeseId == invalid){
            message = "Must Select Cheese";
            //displayInvalidToast(message);
            return false;
        }
        return true;
    }

    /**
     * Decreases the quantity counter
     * @param view
     */
    public void decreaseQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantityPizzaText);
        String quantityString = quantityTV.getText().toString();
        int quantity = Integer.parseInt(quantityString);

        if (quantity > 1) {
            quantity--;
            quantityTV.setText(String.valueOf(quantity));
        }
    }

    /**
     * Increases the quantity counter
     * @param view
     */
    public void increaseQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantityPizzaText);
        String quantityString = quantityTV.getText().toString();
        int quantity = Integer.parseInt(quantityString);

        quantity++;
        quantityTV.setText(String.valueOf(quantity));
    }

    /**
     * Refresh the pizza ordering page to default
     */
    public void refresh(){
        resetRadioButtons();
        resetCheckboxes();
        resetQuantity();
        resetScroll();
    }

    /**
     * Reset all of the radio buttons to the default (first) choice in the group
     */
    private void resetRadioButtons(){
        // Find group for each pizza category
        RadioGroup sizeGroup = getView().findViewById(R.id.sizeGroup);
        RadioGroup crustGroup = getView().findViewById(R.id.crustGroup);
        RadioGroup sauceGroup = getView().findViewById(R.id.sauceGroup);
        RadioGroup cheeseGroup = getView().findViewById(R.id.cheeseGroup);

        sizeGroup.check(R.id.smallBtn);
        crustGroup.check(R.id.handTossedBtn);
        sauceGroup.check(R.id.marinaraBtn);
        cheeseGroup.check(R.id.regCheeseBtn);
    }

    /**
     * Deselect all of the checkboxes
     */
    private void resetCheckboxes(){
        boolean selected = false;

        // Gather all toppings checkboxes
        CheckBox pepperoniBox = getView().findViewById(R.id.pepperoniCheck);
        CheckBox baconBox = getView().findViewById(R.id.baconCheck);
        CheckBox sausageBox = getView().findViewById(R.id.sausageCheck);
        CheckBox phillySteakBox = getView().findViewById(R.id.phillySteakCheck);
        CheckBox hamBox = getView().findViewById(R.id.hamCheck);
        CheckBox chickenBox = getView().findViewById(R.id.chickenCheck);
        CheckBox mushroomBox = getView().findViewById(R.id.mushroomCheck);
        CheckBox pineappleBox = getView().findViewById(R.id.pineappleCheck);
        CheckBox onionsBox = getView().findViewById(R.id.onionsCheck);
        CheckBox greenPeppersBox = getView().findViewById(R.id.greenPeppersCheck);
        CheckBox jalapenoBox = getView().findViewById(R.id.jalapenoCheck);
        CheckBox spinachBox = getView().findViewById(R.id.spinachCheck);

        // Reset checkboxes
        pepperoniBox.setChecked(selected);
        baconBox.setChecked(selected);
        sausageBox.setChecked(selected);
        phillySteakBox.setChecked(selected);
        hamBox.setChecked(selected);
        chickenBox.setChecked(selected);
        mushroomBox.setChecked(selected);
        pineappleBox.setChecked(selected);
        onionsBox.setChecked(selected);
        greenPeppersBox.setChecked(selected);
        jalapenoBox.setChecked(selected);
        spinachBox.setChecked(selected);
    }

    /**
     * Reset the quantity counter to 1
     */
    private void resetQuantity(){
        TextView quantityTV = getView().findViewById(R.id.quantityPizzaText);
        quantityTV.setText("1");
    }

    /**
     * Set the scroll to the top of the fragment
     */
    private void resetScroll(){
        NestedScrollView scrollView = getView().findViewById(R.id.pizzaScroll);
        scrollView.smoothScrollTo(0, 0);
    }

}