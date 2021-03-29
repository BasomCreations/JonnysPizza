package com.example.jonnyspizza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Customer;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;
import com.example.jonnyspizza.CustomObjects.Payment;

public class PlaceOrderActivity extends AppCompatActivity {

    private Order order;
    private Cart cart;

    private EditText firstNameTxt, lastNameTxt, emailAddressTxt, phoneNumberTxt, creditCardTxt, securityCodeTxt, mmTxt, yyyyTxt, billingZipTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        initializeEditTexts();

        this.order = (Order) getIntent().getSerializableExtra(getString(R.string.order_name));
        this.cart = order.getCart();
        setSummaryText();

    }

    /**
     * Initialize the global variables
     */
    private void initializeEditTexts(){
        firstNameTxt = findViewById(R.id.firstNameText);
        lastNameTxt = findViewById(R.id.lastNameText);
        emailAddressTxt = findViewById(R.id.emailAddressText);
        phoneNumberTxt = findViewById(R.id.phoneNumberText);

        creditCardTxt = findViewById(R.id.creditCardNumberText);
        securityCodeTxt = findViewById(R.id.securityCodeText);
        mmTxt = findViewById(R.id.expirationMonthText);
        yyyyTxt = findViewById(R.id.expirationYearText);
        billingZipTxt = findViewById(R.id.billingZipText);
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
            sb.append(delivery.getDeliveryAddress().getStreetAddress() + "\n");
            sb.append(delivery.getDeliveryAddress().getCity() + ", " + delivery.getDeliveryAddress().getState() + " " + delivery.getDeliveryAddress().getZip() + "\n\n");
        }
        else if (order instanceof Carryout){
            sb.append("Order Type: Carryout\n\n");
        }

        sb.append(total);

        summaryText.setText(sb.toString());
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
            finish();
        }
    }

    /**
     * Create a Customer object to add to the Order
     */
    private void setOrderCustomer(){
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String emailAddress = emailAddressTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();

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
     * Validate the user input
     * @return true if all input is valid
     */
    private boolean validateInput(){
        boolean isValid =  true;

        String emptyString = "";
        int phoneNumberLength = 10;
        int minCreditCardLength = 13;
        int maxCreditCardLength = 19;
        int securityLength = 3;
        int mmLength = 2;
        int yyyyLength = 4;
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
        else if (month.length() != mmLength){
            mmTxt.setError("Invalid Expiration Month!");
        }

        if (year.equals(emptyString)){
            yyyyTxt.setError("Year of Expiration Required!");
            isValid = false;
        }
        else if (year.length() != yyyyLength){
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