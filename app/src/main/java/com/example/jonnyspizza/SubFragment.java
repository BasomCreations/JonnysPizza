package com.example.jonnyspizza;

import android.content.Intent;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubFragment newInstance(String param1, String param2) {
        SubFragment fragment = new SubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                completeOrder(v);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Complete Order Button Click
     * @param view
     */
    public void completeOrder(View view){

        RadioGroup hotSubGroup = getView().findViewById(R.id.hotSubGroup);
        int subId = hotSubGroup.getCheckedRadioButtonId();
        String subType = (String) ((RadioButton) getView().findViewById(subId)).getText();

        // Get quantity
        TextView quantityText = getView().findViewById(R.id.quantitySubText);
        int quantity = Integer.parseInt(quantityText.getText().toString());

        Sub sub = new Sub(subType, Price.SUB.getValue(), quantity);


        Intent i = new Intent(getActivity(), DisplayPizzaOrderActivity.class);
        i.putExtra(getString(R.string.sub_name), sub);
        startActivity(i);

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
}