package com.example.jonnyspizza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Customer;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;
import com.example.jonnyspizza.CustomObjects.Payment;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class PlaceOrderActivity extends AppCompatActivity {

    private Order order;
    private Cart cart;

    private EditText firstNameTxt, lastNameTxt, emailAddressTxt, phoneNumberTxt, creditCardTxt, securityCodeTxt, mmTxt, yyyyTxt, billingZipTxt;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView orderPlaced_CustomerMsg;
    private Button orderPlaced_OkBtn;

    private DatabaseHandler dbHandler;
    private RESTHandler restHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        initializeEditTexts();

        this.order = (Order) getIntent().getSerializableExtra(getString(R.string.order_name));
        this.cart = order.getCart();
        setDefaultEditFields();
        setSummaryText();

        dbHandler = new DatabaseHandler(this);
        restHandler = new RESTHandler(this);
    }

    /**
     * Initialize the global variables
     */
    private void initializeEditTexts(){
        firstNameTxt = findViewById(R.id.firstNameText);
        lastNameTxt = findViewById(R.id.lastNameText);
        emailAddressTxt = findViewById(R.id.emailAddressText);
        phoneNumberTxt = findViewById(R.id.phoneNumberText);
        phoneNumberTxt.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        creditCardTxt = findViewById(R.id.creditCardNumberText);
        securityCodeTxt = findViewById(R.id.securityCodeText);
        mmTxt = findViewById(R.id.expirationMonthText);
        yyyyTxt = findViewById(R.id.expirationYearText);
        billingZipTxt = findViewById(R.id.billingZipText);
    }

    /**
     * Fill in the edit text fields with Customer and Payment info if available
     */
    private void setDefaultEditFields(){
        Customer customer;
        Payment payment;
        if ((customer = order.getCustomer()) != null){
            if (customer.getFirstName() != null) firstNameTxt.setText(customer.getFirstName());
            if (customer.getLastName() != null) lastNameTxt.setText(customer.getLastName());
            if (customer.getEmail() != null) emailAddressTxt.setText(customer.getEmail());
            if (customer.getPhoneNumber() != null) phoneNumberTxt.setText(customer.getPhoneNumber());
        }
        if ((payment = order.getPayment()) != null){
            if (payment.getCreditCartNumber() != null) creditCardTxt.setText(payment.getCreditCartNumber());
            if (payment.getSecurityCode() != null) securityCodeTxt.setText(payment.getSecurityCode());
            if (payment.getExpMM() != null) mmTxt.setText(payment.getExpMM());
            if (payment.getExpYYYY() != null) yyyyTxt.setText(payment.getExpYYYY());
            if (payment.getBillingZip() != null) billingZipTxt.setText(payment.getBillingZip());
        }
    }

    /**
     * Sets the Order Summary text at the top of the page
     */
    private void setSummaryText() {
        TextView summaryText = findViewById(R.id.orderSummaryText);

        String total = String.format("Total: $%.2f", cart.getTotalCost());

        StringBuilder sb = new StringBuilder();
        if (order instanceof Delivery){
            Delivery delivery = (Delivery) order;

            sb.append("Order Type: Delivery\n\n");

            if (delivery.getDeliveryAddress() != null) {
                sb.append(delivery.getDeliveryAddress().getStreetAddress() + "\n");
                sb.append(delivery.getDeliveryAddress().getCity() + ", " + delivery.getDeliveryAddress().getState() + " " + delivery.getDeliveryAddress().getZip() + "\n\n");
            }
        }
        else if (order instanceof Carryout){
            sb.append("Order Type: Carryout\n\n");
        }

        sb.append(total);

        summaryText.setText(sb.toString());
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
        finish();
    }

    /**
     * Handles the Place Order Button click
     * @param view
     */
    public void PlaceOrderBtn_Click(View view){
        boolean isValid = validateInput();

        if (isValid){
            setOrderCustomer();
            setOrderPayment();
            //String orderID = dbHandler.addOrder(order);
            restHandler.addOrder(order);
            //createOrderPlacedDialog(orderID);
        }
    }

    /**
     * Create a Customer object to add to the Order
     */
    private void setOrderCustomer(){
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String emailAddress = emailAddressTxt.getText().toString();
        String phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumberTxt.getText().toString());

        Customer customer = new Customer(firstName, lastName, emailAddress, phoneNumber);
        this.order.setCustomer(customer);
    }

    /**
     * Create a Payment object to add to the Order
     */
    private void setOrderPayment(){
        String creditCardNum = creditCardTxt.getText().toString();
        String securityCode = securityCodeTxt.getText().toString();
        String month = mmTxt.getText().toString();
        String year = yyyyTxt.getText().toString();
        String billingZip = billingZipTxt.getText().toString();

        Payment payment = new Payment(creditCardNum, securityCode, month, year, billingZip);
        this.order.setPayment(payment);
    }

    /**
     * Create the popup to notify user of successful order
     * @param orderID
     */
    public void createOrderPlacedDialog(String orderID) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View orderPlacedPopupView = getLayoutInflater().inflate(R.layout.order_placed_popup, null);

        orderPlaced_CustomerMsg = (TextView) orderPlacedPopupView.findViewById(R.id.customerNotificationMessage);
        orderPlaced_CustomerMsg.setText(createPopupText(orderID));
        orderPlaced_OkBtn = (Button) orderPlacedPopupView.findViewById(R.id.orderPlaced_OkBtn);

        dialogBuilder.setView(orderPlacedPopupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        orderPlaced_OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    /**
     * Create the text that will be displayed in the order placed popup
     * @param orderID
     * @return String
     */
    private String createPopupText(String orderID){
        StringBuilder sb = new StringBuilder();
        String firstName = order.getCustomer().getFirstName();
        String email = order.getCustomer().getEmail();
        String phone = parseCustomerPhone();

        sb.append(firstName + ", your order has been received!\n");
        sb.append("Your order number is #" + orderID + ".\n\n");
        sb.append("We will notify you as soon as your order is ready. Make sure to check your email " + email);
        sb.append(" and your phone " + phone + ".");

        return sb.toString();
    }

    /**
     * Format the customer's phone number in a readable format
     * @return
     */
    private String parseCustomerPhone(){
        String originalPhone = order.getCustomer().getPhoneNumber();
        String updatedPhone = "(" + originalPhone.substring(0, 3) + ") " + originalPhone.substring(3, 6) + "-" + originalPhone.substring(6);
        return updatedPhone;
    }

    /**
     * Validate the user input
     * @return true if all input is valid
     */
    private boolean validateInput(){
        boolean isValid =  true;

        String emptyString = "";
        int phoneNumberLength = 14;
        int minCreditCardLength = 13;
        int maxCreditCardLength = 19;
        int securityLength = 3;
        int mmLength = 2;
        int minMonth = 1;
        int maxMonth = 12;
        int yyyyLength = 4;
        int minYear = Calendar.getInstance().get(Calendar.YEAR);
        int maxYear = minYear + 3;
        int zipCodeLength = 5;

        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String emailAddress = emailAddressTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();

        String creditCardNum = creditCardTxt.getText().toString();
        String securityCode = securityCodeTxt.getText().toString();
        String month = mmTxt.getText().toString();
        String year = yyyyTxt.getText().toString();
        String billingZip = billingZipTxt.getText().toString();

        int monthInt = minMonth - 1;
        int yearInt = minYear - 1;

        if (!month.equals(emptyString)){
            monthInt = Integer.parseInt(month);
        }
        if (!year.equals(emptyString)){
            yearInt = Integer.parseInt(year);
        }

        if (firstName.equals(emptyString)){
            firstNameTxt.setError("First Name Required!");
            isValid = false;
        }
        if (lastName.equals(emptyString)){
            lastNameTxt.setError("Last Name Required!");
            isValid = false;
        }
        if (emailAddress.equals(emptyString)){
            emailAddressTxt.setError("Email Address Required!");
            isValid = false;
        }
        if (phoneNumber.equals(emptyString)){
            phoneNumberTxt.setError("Phone Number Required!");
            isValid = false;
        }
        else if (phoneNumber.length() != phoneNumberLength){
            phoneNumberTxt.setError("Invalid Phone Number!");
            isValid = false;
        }

        if (creditCardNum.equals(emptyString)){
            creditCardTxt.setError("Credit Card Number Required!");
            isValid = false;
        }
        else if (creditCardNum.length() < minCreditCardLength || creditCardNum.length() > maxCreditCardLength){
            creditCardTxt.setError("Invalid Credit Card Number!");
            isValid = false;
        }

        if (securityCode.equals(emptyString)){
            securityCodeTxt.setError("Security Code Required!");
            isValid = false;
        }
        else if (securityCode.length() != securityLength){
            securityCodeTxt.setError("Invalid Security Code!");
            isValid = false;
        }

        if (month.equals(emptyString)){
            mmTxt.setError("Month of Expiration Required!");
            isValid = false;
        }
        else if (monthInt < minMonth || monthInt > maxMonth){
            mmTxt.setError("Invalid Expiration Month!");
        }

        if (year.equals(emptyString)){
            yyyyTxt.setError("Year of Expiration Required!");
            isValid = false;
        }
        else if (yearInt < minYear || yearInt > maxYear){
            yyyyTxt.setError("Invalid Expiration Year!");
            isValid = false;
        }

        if (billingZip.equals(emptyString)){
            billingZipTxt.setError("Billing Zip Code Required!");
            isValid = false;
        }
        else if (billingZip.length() != zipCodeLength){
            billingZipTxt.setError("Invalid Zip Code!");
            isValid = false;
        }

        return isValid;
    }
}