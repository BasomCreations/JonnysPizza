package com.example.jonnyspizza;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkFragment extends Fragment {

    /*
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;*/

    public DrinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DrinkFragment.
     */
    public static DrinkFragment newInstance() {
        DrinkFragment fragment = new DrinkFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drink, container, false);

        rootView.findViewById(R.id.decreaseSodaQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseSodaQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.increaseSodaQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSodaQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.addSodaBtn).setOnClickListener(new View.OnClickListener() {
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
     * Add the current item to the cart
     * @param view
     */
    public void addToCart(View view){

        RadioGroup sodaGroup = getView().findViewById(R.id.sodaGroup);
        int subId = sodaGroup.getCheckedRadioButtonId();
        String sodaType = (String) ((RadioButton) getView().findViewById(subId)).getText();

        // Get quantity
        TextView quantityText = getView().findViewById(R.id.quantitySodaText);
        int quantity = Integer.parseInt(quantityText.getText().toString());

        Drink soda = new Drink(sodaType, Price.DRINKS.getValue(), quantity);

        //cart.addItem(sub);
        Cart cart = ((OrderItemActivity)getActivity()).getCart();
        cart.addItem(soda);

    }

    /**
     * Decreases the quantity counter
     * @param view
     */
    public void decreaseSodaQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantitySodaText);
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
    public void increaseSodaQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantitySodaText);
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
        resetQuantity();
    }

    /**
     * Reset all of the radio buttons to the default (first) choice in the group
     */
    private void resetRadioButtons(){
        RadioGroup sodaGroup = getView().findViewById(R.id.sodaGroup);
        sodaGroup.check(R.id.pepsiBtn);
    }

    /**
     * Reset the quantity counter to 1
     */
    private void resetQuantity(){
        TextView quantityTV = getView().findViewById(R.id.quantitySodaText);
        quantityTV.setText("1");
    }
}