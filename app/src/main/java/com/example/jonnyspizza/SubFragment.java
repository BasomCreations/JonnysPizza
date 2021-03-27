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
 * Use the {@link SubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /**private static final String CART_PARAM = "Cart";

    //private Cart cart; */

    public SubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubFragment.
     */
    public static SubFragment newInstance() {
        SubFragment fragment = new SubFragment();
        /**Bundle args = new Bundle();
        //args.putSerializable(CART_PARAM, cart);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**if (getArguments() != null) {
            cart = (Cart) getArguments().getSerializable(CART_PARAM);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub, container, false);

        rootView.findViewById(R.id.decreaseSubQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseSubQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.increaseSubQuantityBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSubQuantityBtn_Click(v);
            }
        });

        rootView.findViewById(R.id.addSubBtn).setOnClickListener(new View.OnClickListener() {
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

        RadioGroup hotSubGroup = getView().findViewById(R.id.hotSubGroup);
        int subId = hotSubGroup.getCheckedRadioButtonId();
        String subType = (String) ((RadioButton) getView().findViewById(subId)).getText();

        // Get quantity
        TextView quantityText = getView().findViewById(R.id.quantitySubText);
        int quantity = Integer.parseInt(quantityText.getText().toString());

        Sub sub = new Sub(subType, Price.SUB.getValue(), quantity);

        //cart.addItem(sub);
        Cart cart = ((OrderItemActivity)getActivity()).getCart();
        cart.addItem(sub);

    }

    /**
     * Decreases the quantity counter
     * @param view
     */
    public void decreaseSubQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantitySubText);
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
    public void increaseSubQuantityBtn_Click(View view){

        TextView quantityTV = getView().findViewById(R.id.quantitySubText);
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
        RadioGroup hotSubGroup = getView().findViewById(R.id.hotSubGroup);
        hotSubGroup.check(R.id.chickenBaconRanchBtn);
    }

    /**
     * Reset the quantity counter to 1
     */
    private void resetQuantity(){
        TextView quantityTV = getView().findViewById(R.id.quantitySubText);
        quantityTV.setText("1");
    }
}