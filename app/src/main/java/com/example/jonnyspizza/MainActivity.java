package com.example.jonnyspizza;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jonnyspizza.CustomObjects.Address;
import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Customer;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;
import com.example.jonnyspizza.CustomObjects.Payment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText deliveryPopup_streetAddress, deliveryPopup_city, deliveryPopup_zip;
    private Spinner deliveryPopup_stateSpinner;
    private Button deliveryPopup_cancelBtn, deliveryPopup_saveBtn, historyPopup_closeBtn;

    private DatabaseHandler dbHandler;
    private RESTHandler restHandler;

    private final static int LAUNCH_ORDER_SUMMARY_ACTIVITY = 1;

    private LinearLayout historyLinearLayout;
    private final static int HISTORY_TEXT_SIZE = 20;
    private final static int HISTORY_SIDE_PADDING = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DatabaseHandler(this);
        restHandler = new RESTHandler(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ORDER_SUMMARY_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK){
            }
            if (resultCode == Activity.RESULT_CANCELED){
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }

    /**
     * Starts the order items activity
     * @param order Order object for the specific type of order (Carryout or Delivery)
     */
    private void proceedToOrder(Order order){
        Intent intent = new Intent(this, OrderItemActivity.class);
        intent.putExtra(getString(R.string.order_name), order);
        startActivity(intent);
        //recreate();
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
                dialog.dismiss();
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
            isValid = false;
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

    /**
     * Handles the "Order History" button click
     * @param view
     */
    public void orderHistoryBtn_Click(View view){
        createNewOrderHistoryDialog();
    }

    /**
     * Create the popup to display the order history
     */
    public void createNewOrderHistoryDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View orderHistoryPopupView = getLayoutInflater().inflate(R.layout.fragment_order_history, null);
        historyPopup_closeBtn = (Button) orderHistoryPopupView.findViewById(R.id.closeHistoryBtn);
        historyLinearLayout = (LinearLayout) orderHistoryPopupView.findViewById(R.id.historyLinearLayout);

        //populateOrderHistoryDialog(dialogBuilder.getContext());

        restHandler.getRecentOrders(dialogBuilder.getContext(), orderHistoryPopupView);

        //dialogBuilder.setView(orderHistoryPopupView);
        //dialog = dialogBuilder.create();
        //dialog.show();

        historyPopup_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    protected void populateOrderHistoryDialog(Context context, View view, ArrayList<ContentValues> results){
        //ArrayList<ContentValues> results = dbHandler.getRecentOrders();

        int i = 1;
        for (ContentValues values: results) {
            displayOrder(context, values, i);
            i++;
        }

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void displayOrder(Context context, ContentValues values, int index){
        String orderID = (String) values.get(DB_Util.ORDER_PK);
        String orderType = (String) values.get(DB_Util.ORDER_TYPE);
        String orderCost = (String) values.get(DB_Util.ORDER_COST);
        String orderDate = (String) values.get(DB_Util.ORDER_DATE);

        // Conversion from dp (specified in xml) to pixels
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixelsHeight = (int) (32 * scale + 0.5f);
        int pixelsID = (int) (80 * scale + 0.5f);
        int pixelsDate = (int) (200 * scale + 0.5f);
        int pixelsCost = (int) (85 * scale + 0.5f);
        int pixelsType = (int) (110 * scale + 0.5f);
        int pixelsDetails = (int) (75 * scale + 0.5f);

        LinearLayout recordLayout = new LinearLayout(context);
        recordLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixelsHeight));
        recordLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView orderIDTV = new TextView(context);
        orderIDTV.setLayoutParams(new LinearLayout.LayoutParams(pixelsID, ViewGroup.LayoutParams.MATCH_PARENT));
        orderIDTV.setTextSize(HISTORY_TEXT_SIZE);
        orderIDTV.setText(orderID);
        orderIDTV.setGravity(Gravity.RIGHT);
        orderIDTV.setPadding(HISTORY_SIDE_PADDING, 0, HISTORY_SIDE_PADDING, 0);
        orderIDTV.setBackgroundResource(R.drawable.table_cell_border);

        TextView orderDateTV = new TextView(context);
        orderDateTV.setLayoutParams(new LinearLayout.LayoutParams(pixelsDate, ViewGroup.LayoutParams.MATCH_PARENT));
        orderDateTV.setTextSize(HISTORY_TEXT_SIZE);
        orderDateTV.setText(orderDate);
        orderDateTV.setPadding(HISTORY_SIDE_PADDING, 0, 0, 0);
        orderDateTV.setBackgroundResource(R.drawable.table_cell_border);

        TextView orderCostTV = new TextView(context);
        orderCostTV.setLayoutParams(new LinearLayout.LayoutParams(pixelsCost, ViewGroup.LayoutParams.MATCH_PARENT));
        orderCostTV.setTextSize(HISTORY_TEXT_SIZE);
        orderCostTV.setText(formatMoney(orderCost));
        orderCostTV.setGravity(Gravity.RIGHT);
        orderCostTV.setPadding(HISTORY_SIDE_PADDING, 0, HISTORY_SIDE_PADDING, 0);
        orderCostTV.setBackgroundResource(R.drawable.table_cell_border);

        TextView orderTypeTV = new TextView(context);
        orderTypeTV.setLayoutParams(new LinearLayout.LayoutParams(pixelsType, ViewGroup.LayoutParams.MATCH_PARENT));
        orderTypeTV.setTextSize(HISTORY_TEXT_SIZE);
        orderTypeTV.setText(orderType);
        orderTypeTV.setGravity(Gravity.CENTER);
        orderTypeTV.setPadding(HISTORY_SIDE_PADDING, 0, 0, 0);
        orderTypeTV.setBackgroundResource(R.drawable.table_cell_border);

        Button viewButton = new Button(context);
        viewButton.setLayoutParams(new LinearLayout.LayoutParams(pixelsDetails, ViewGroup.LayoutParams.MATCH_PARENT));
        viewButton.setBackgroundResource(R.drawable.view_button_background);
        viewButton.setText("View");
        viewButton.setPadding(0,0,0,0);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewButton_Click(orderID, orderType);
            }
        });

        recordLayout.addView(orderIDTV);
        recordLayout.addView(orderDateTV);
        recordLayout.addView(orderCostTV);
        recordLayout.addView(orderTypeTV);
        recordLayout.addView(viewButton);

        historyLinearLayout.addView(recordLayout);
    }

    /**
     * Handles the View button click to go to the "Order Summary" screen
     * @param orderID
     * @param orderType
     */
    private void viewButton_Click(String orderID, String orderType){
//        Order order;
//        Customer customer = dbHandler.getCustomer(orderID);
//        Payment payment = dbHandler.getPayment(orderID);
//        Cart cart = dbHandler.getOrderItems(orderID);

        restHandler.getOrder(orderID, orderType);

//        if (orderType.equals(DB_Util.DELIVERY_TYPE)){
//            Address deliveryAddress = dbHandler.getDeliveryAddress(orderID);
//            order = new Delivery(cart, customer, payment, deliveryAddress);
//        }
//        else{
//            order = new Carryout(cart, customer, payment);
//        }
//
//        viewOrderSummary(order);
    }

    /**
     * Proceed to the Order Summary activities
     * @param order
     */
    protected void viewOrderSummary(Order order){
        Intent intent = new Intent(this, OrderSummaryActivity.class);
        intent.putExtra(getString(R.string.order_name), order);
        intent.putExtra(getString(R.string.summary_mode), getString(R.string.view_only));
        startActivityForResult(intent, LAUNCH_ORDER_SUMMARY_ACTIVITY);
    }

    /**
     * Helper method to format money as a string
     * @param cost Original money string
     * @return String with formatted money
     */
    private String formatMoney(String cost){
        double costDouble = Double.parseDouble(cost);
        return String.format("$%.2f", costDouble);
    }
}