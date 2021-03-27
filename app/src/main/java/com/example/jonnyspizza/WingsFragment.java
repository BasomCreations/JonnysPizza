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
 * Use the {@link WingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WingsFragment extends Fragment {

    /*//
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //
    private String mParam1;
    private String mParam2;*/

    public WingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WingsFragment.
     */
    public static WingsFragment newInstance() {
        WingsFragment fragment = new WingsFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
         */
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

        View rootView = inflater.inflate(R.layout.fragment_wings, container, false);

        rootView.findViewById(R.id.decreaseWingsQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseWingsQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.increaseWingsQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseWingsQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.addWingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(v);
                ((OrderItemActivity)getActivity()).createNewDialog(v);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Add the current item to the cart
     * @param view
     */
    public void addToCart(View view){

        Wings wings = createWings();
        wings.calculateCost();

        Cart cart = ((OrderItemActivity)getActivity()).getCart();
        cart.addItem(wings);
    }

    /**
     * Creates a Wings object based on the selection of the radio buttons
     * @return Wings
     */
    private Wings createWings() {
        // Get Wings Size
        RadioGroup wingsSizeGroup = getView().findViewById(R.id.wingSizeGroup);
        int wingsId = wingsSizeGroup.getCheckedRadioButtonId();
        String wingsSize = (String) ((RadioButton) getView().findViewById(wingsId)).getText();

        // Get Wings Sauce
        RadioGroup wingsSauceGroup = getView().findViewById(R.id.wingSauceGroup);
        int wingsSauceId = wingsSauceGroup.getCheckedRadioButtonId();
        String wingsSauce = (String) ((RadioButton) getView().findViewById(wingsSauceId)).getText();

        // Get quantity
        TextView quantityText = getView().findViewById(R.id.quantityWingsText);
        int quantity = Integer.parseInt(quantityText.getText().toString());

        Wings wings = new Wings(wingsSize, wingsSauce, quantity);

        return wings;
    }

    /**
     * Decreases the quantity counter
     * @param view
     */
    public void decreaseWingsQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantityWingsText);
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
    public void increaseWingsQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantityWingsText);
        String quantityString = quantityTV.getText().toString();
        int quantity = Integer.parseInt(quantityString);

        quantity++;
        quantityTV.setText(String.valueOf(quantity));
    }

    /**
     * Refresh the wings ordering page to default
     */
    public void refresh(){
        View homeView = getView();

        if (homeView != null) {
            resetRadioButtons();
            resetQuantity();
        }
    }

    /**
     * Reset all of the radio buttons to the default (first) choice in the group
     */
    private void resetRadioButtons(){
        RadioGroup wingSizeGroup = getView().findViewById(R.id.wingSizeGroup);
        if (wingSizeGroup != null) wingSizeGroup.check(R.id.halfDozenBtn);

        RadioGroup wingSauceGroup = getView().findViewById(R.id.wingSauceGroup);
        if (wingSauceGroup != null) wingSauceGroup.check(R.id.plainBtn);
    }

    /**
     * Reset the quantity counter to 1
     */
    private void resetQuantity(){
        TextView quantityTV = getView().findViewById(R.id.quantityWingsText);
        if (quantityTV != null) quantityTV.setText("1");
    }
}