package com.example.jonnyspizza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jonnyspizza.CustomObjects.Address;
import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText deliveryPopup_streetAddress, deliveryPopup_city, deliveryPopup_zip;
    private Spinner deliveryPopup_stateSpinner;
    private Button deliveryPopup_cancelBtn, deliveryPopup_saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts the order items activity
     * @param order Order object for the specific type of order (Carryout or Delivery)
     */
    private void proceedToOrder(Order order){
        Intent intent = new Intent(this, OrderItemActivity.class);
        intent.putExtra(getString(R.string.order_name), order);
        startActivity(intent);
    }

    /**
     * Handles the "Carryout" button click (continues to ordering screen)
     * @param view
     */
    public void CarryoutBtn_Click(View view){
        Cart cart = new Cart();
        Carryout carryout = new Carryout(cart);
        proceedToOrder(carryout);
    }

    /**
     * Handles the "Delivery" button click
     * @param v
     */
    public void DeliveryBtn_Click(View v){
        createNewDeliveryAddressDialog();
    }

    /**
     * Create the popup to obtain the delivery address
     */
    public void createNewDeliveryAddressDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View deliveryPopupView = getLayoutInflater().inflate(R.layout.delivery_address_popup, null);
        deliveryPopup_streetAddress = (EditText) deliveryPopupView.findViewById(R.id.deliveryPopup_streetAddress);
        deliveryPopup_city = (EditText) deliveryPopupView.findViewById(R.id.deliveryPopup_city);
        deliveryPopup_zip = (EditText) deliveryPopupView.findViewById(R.id.deliveryPopup_zip);

        deliveryPopup_stateSpinner = (Spinner) deliveryPopupView.findViewById(R.id.deliveryPopup_stateSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states_array, R.layout.spinner_item);
                //ArrayAdapter.createFromResource(this, R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryPopup_stateSpinner.setAdapter(adapter);

        deliveryPopup_cancelBtn = (Button) deliveryPopupView.findViewById(R.id.deliveryPopup_cancelBtn);
        deliveryPopup_saveBtn = (Button) deliveryPopupView.findViewById(R.id.deliveryPopup_saveBtn);

        dialogBuilder.setView(deliveryPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        deliveryPopup_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        deliveryPopup_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmAddressBtn_Click();
            }
        });
    }

    /**
     * Proceeds to ordering activity after the address confirmation button is clicked
     */
    private void ConfirmAddressBtn_Click(){
        boolean isValid = validateDeliveryPopupInput();
        if (isValid){
            Address deliveryAddress = createDeliveryAddress();
            Cart cart = new Cart();
            Delivery delivery = new Delivery(cart, deliveryAddress);
            proceedToOrder(delivery);
        }
    }

    /**
     * Make sure all Delivery address inputs are valid
     * @return true if all inputs are valid, else false
     */
    private boolean validateDeliveryPopupInput() {
        boolean isValid = true;
        String emptyString = "";
        String streetAddress = deliveryPopup_streetAddress.getText().toString();
        String city = deliveryPopup_city.getText().toString();
        String zip = deliveryPopup_zip.getText().toString();

        if (streetAddress == null || streetAddress.equals(emptyString)) {
            deliveryPopup_streetAddress.setError("Street Address Required!");
            isValid = false;
        }

        if (city == null || city.equals(emptyString)){
            deliveryPopup_city.setError("City Required!");
            isValid = false;
        }

        if (zip == null || zip.equals(emptyString)){
            deliveryPopup_zip.setError("Zip Code Required!");
            isValid = false;
        }
        else if (zip.length() != 5){                        // check zip code length
            deliveryPopup_zip.setError("Invalid Zip Code!");
        }

        return isValid;
    }

    /**
     * Creates a DeliveryAddress object based on popup input
     * @return Address
     */
    private Address createDeliveryAddress(){
        String streetAddress = deliveryPopup_streetAddress.getText().toString();
        String city = deliveryPopup_city.getText().toString();
        String state = deliveryPopup_stateSpinner.getSelectedItem().toString();//deliveryPopup_state.getText().toString();
        String zip = deliveryPopup_zip.getText().toString();

        Address address = new Address(streetAddress, city, state, zip);

        return address;
    }
}