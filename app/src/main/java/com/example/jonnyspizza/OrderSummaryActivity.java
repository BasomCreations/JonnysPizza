package com.example.jonnyspizza;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jonnyspizza.CustomObjects.Address;
import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;

import org.jetbrains.annotations.NotNull;

public class OrderSummaryActivity extends AppCompatActivity {

    private final static int HEADER_TEXT_SIZE = 24;
    private final static int NORMAL_TEXT_SIZE = 16;
    private final static int HEADER_PADDING = 0;
    private final static int HEADER_PADDING_BOTTOM = 24;
    private final static int INNER_PADDING_LEFT = 10;
    private final static int INNER_PADDING_RIGHT = 10;
    private final static int ITEM_PADDING = 12;
    private final static int DIVIDER_WIDTH = 2;
    private final static int INNER_ITEM_WIDTH = 300;
    private final static int ITEM_WIDTH = 950;
    private final static int DELETE_BTN_DIMENSION = 100;

    private final static int LAUNCH_PAYMENT_ACTIVITY = 1;
    private final static int LAUNCH_ORDER_ITEMS_ACTIVITY = 1;

    private Order order;
    private boolean editMode, oldOrder;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private RadioGroup orderTypeGroup;
    private EditText deliveryPopup_streetAddress, deliveryPopup_city, deliveryPopup_zip;
    private Spinner deliveryPopup_stateSpinner;
    private Button deliveryPopup_cancelBtn, deliveryPopup_saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        setMode();
        this.order = (Order) getIntent().getSerializableExtra(getString(R.string.order_name));
        Cart cart = order.getCart();
        handleCart(cart);

        setSummaryText();
    }

    /**
     * Determine if the screen should be view-only (past orders) or editable
     */
    private void setMode(){
        String mode = getIntent().getStringExtra(getString(R.string.summary_mode));
        Button editOrderTypeBtn = findViewById(R.id.editOrderTypeBtn);
        Button editItemsBtn = findViewById(R.id.editItemsBtn);
        Button addItemsBtn = findViewById(R.id.addMoreItemsBtn);

        // Shown when creating a new order
        if (mode.equals(getString(R.string.new_editable))){
            editMode = true;
            oldOrder = false;
            editOrderTypeBtn.setVisibility(View.GONE);
            editItemsBtn.setVisibility(View.GONE);
            addItemsBtn.setVisibility(View.GONE);
        }
        // Shown when viewing past orders that have selected "Edit Items"
        else if (mode.equals(getString(R.string.past_editable))){
            editMode = true;
            oldOrder = true;
            editOrderTypeBtn.setVisibility(View.VISIBLE);
            editItemsBtn.setVisibility(View.GONE);
            addItemsBtn.setVisibility(View.VISIBLE);
        }
        // VIEW_ONLY shown when initially viewing past orders
        else{
            editMode = false;
            oldOrder = true;
            editOrderTypeBtn.setVisibility(View.VISIBLE);
            editItemsBtn.setVisibility(View.VISIBLE);
            addItemsBtn.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the Order Summary text at the top of the page
     */
    private void setSummaryText() {
        TextView summaryText = findViewById(R.id.orderSummary_summaryText);

        //String total = String.format("Total: $%.2f", order.getCart().getTotalCost());

        StringBuilder sb = new StringBuilder();
        if (order instanceof Delivery){
            Delivery delivery = (Delivery) order;

            sb.append("Order Type: Delivery\n\n");

            if (delivery.getDeliveryAddress() != null) {
                sb.append(delivery.getDeliveryAddress().getStreetAddress() + "\n");
                sb.append(delivery.getDeliveryAddress().getCity() + ", " + delivery.getDeliveryAddress().getState() + " " + delivery.getDeliveryAddress().getZip());
            }
        }
        else if (order instanceof Carryout){
            sb.append("Order Type: Carryout");
        }

        //sb.append(total);

        summaryText.setText(sb.toString());
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.order_name), order);
        setResult(Activity.RESULT_OK, returnIntent);
        //setResult(200, returnIntent);
        super.onBackPressed();
        finish();
    }

    /**
     * Handles a cart based on the number of items in it
     * @param cart
     */
    private void handleCart(Cart cart){
        setButtonNames();

        // only display the cart if there are items
        if (cart.getItems().size() > 0){
            displayCart(cart);
        }
        // otherwise, remove the option to checkout
        else{
            Button checkoutBtn = findViewById(R.id.proceedToPaymentBtn);
            checkoutBtn.setVisibility(View.GONE);

            LinearLayout parentLayout = findViewById(R.id.parentLayout);

            TextView noItemTV = new TextView(this);
            noItemTV.setText("Empty Cart");
            noItemTV.setTextSize(HEADER_TEXT_SIZE);
            noItemTV.setGravity(Gravity.CENTER);

            parentLayout.addView(noItemTV);
        }
    }

    /**
     * Adjusts the names of the buttons based on the current mode
     */
    private void setButtonNames(){
        Button backButton = findViewById(R.id.orderSummary_backToMenuBtn);
        Button checkoutButton = findViewById(R.id.proceedToPaymentBtn);

        if (editMode && !oldOrder){
            backButton.setText(R.string.back_editable);
            checkoutButton.setText(R.string.checkout_editable);
        }
        else{
            backButton.setText(R.string.back_view_only);
            checkoutButton.setText(R.string.checkout_view_only);
        }
    }

    /**
     * Display all items in the cart
     * @param cart
     */
    private void displayCart(Cart cart){
        for (Item item: cart.getItems()) {
            displayItem(cart, item);
        }
        displayTotalCost(cart);
    }

    /**
     * Display an item on the confirmation screen
     * @param item
     */
    private void displayItem(Cart cart, Item item){
        // Original layout in the activity
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        // Adjust layout if in view-only mode
        if (!editMode) {
            final float scale = getResources().getDisplayMetrics().density;
            int topPadding = (int) (16 * scale + 0.5f);
            int sidePadding = (int) (25 * scale + 0.5f);
            parentLayout.setPadding(sidePadding, topPadding, 0, 0);
        }

        //Create layout for item and delete button
        LinearLayout outerLayout = new LinearLayout(OrderSummaryActivity.this);
        outerLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create button to delete the item
        Button deleteBtn = null;
        if (editMode) deleteBtn = createDeleteButton(cart, item);

        // Create layout for the entire item
        LinearLayout itemLayout = createItemLayout();

        // Create inner layout for the item name, quantity, and cost
        LinearLayout itemInnerLayout = createInnerItemLayout();

        // Create text for item name
        TextView itemName = createItemNameTV(item);

        // Create text for item quantity
        TextView itemQuantity = new TextView(OrderSummaryActivity.this);
        itemQuantity.setText("Quantity: " + item.getQuantity());
        itemQuantity.setTextSize(NORMAL_TEXT_SIZE);
        itemQuantity.setPadding(0, 0, INNER_PADDING_RIGHT, 0);

        // Create text for total item cost
        TextView itemCost = createItemCostTV(item);

        // Create vertical line for divider
        View divider = createDivider();

        // Create text for description of item
        TextView itemDescription = new TextView(OrderSummaryActivity.this);
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

        if (deleteBtn != null) outerLayout.addView(deleteBtn);
        outerLayout.addView(itemLayout);

        parentLayout.addView(outerLayout);
    }

    /**
     * Creates a button to delete the corresponding item from the cart
     * @param cart
     * @param item
     * @return
     */
    @NotNull
    private Button createDeleteButton(Cart cart, Item item) {
        Button deleteBtn = new Button(OrderSummaryActivity.this);
        deleteBtn.setText("X");
        deleteBtn.setTextColor(Color.RED);
        deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(DELETE_BTN_DIMENSION, DELETE_BTN_DIMENSION));
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.removeItem(item);
                //Intent i = getIntent();
                //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ////i.putExtra(getString(R.string.cart_name), cart);
                //finish();
                //startActivity(i);
                recreate();
            }
        });
        return deleteBtn;
    }

    /**
     * Creates a layout for the item and its description to be displayed
     * @return
     */
    @NotNull
    private LinearLayout createItemLayout() {
        LinearLayout itemLayout = new LinearLayout(OrderSummaryActivity.this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setBackgroundResource(R.drawable.order_display_border);
        itemLayout.setPadding(ITEM_PADDING, ITEM_PADDING, ITEM_PADDING, ITEM_PADDING);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ITEM_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        return itemLayout;
    }

    /**
     * Creates an inner layout for the item type, quantity, and cost
     * @return
     */
    @NotNull
    private LinearLayout createInnerItemLayout() {
        LinearLayout itemInnerLayout = new LinearLayout(OrderSummaryActivity.this);
        itemInnerLayout.setOrientation(LinearLayout.VERTICAL);
        itemInnerLayout.setLayoutParams(new LinearLayout.LayoutParams(INNER_ITEM_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        return itemInnerLayout;
    }

    /**
     * Creates a TextView for the corresponding Item Name
     * @param item
     * @return
     */
    @NotNull
    private TextView createItemNameTV(Item item) {
        TextView itemName = new TextView(OrderSummaryActivity.this);
        itemName.setText(item.getName());
        itemName.setTextSize(HEADER_TEXT_SIZE);
        itemName.setTextColor(Color.BLACK);
        itemName.setPadding(HEADER_PADDING, HEADER_PADDING, HEADER_PADDING, HEADER_PADDING_BOTTOM);
        return itemName;
    }

    /**
     * Creates a TextView for the cost of the item
     * @param item
     * @return
     */
    @NotNull
    private TextView createItemCostTV(Item item) {
        TextView itemCost = new TextView(OrderSummaryActivity.this);
        String totalCost = String.format("$%.2f", (item.getCost() * item.getQuantity()));
        itemCost.setText("Cost: " + totalCost);
        itemCost.setTextSize(NORMAL_TEXT_SIZE);
        itemCost.setPadding(0, 0, INNER_PADDING_RIGHT, 0);
        return itemCost;
    }

    /**
     * Create a divider between the inner item layout and the item description TextView
     * @return
     */
    @NotNull
    private View createDivider() {
        View divider = new View(OrderSummaryActivity.this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(DIVIDER_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT));
        divider.setBackgroundColor(Color.BLACK);
        return divider;
    }

    /**
     * Displays the total cost of the entire cart
     * @param cart
     */
    private void displayTotalCost(Cart cart){
        // Original layout in the activity
        LinearLayout totalCostLayout = findViewById(R.id.totalCostLayout);

        // TextView for the total cost
        TextView totalTextView = new TextView(OrderSummaryActivity.this);

        totalTextView.setText(String.format("Total Cost: $%.2f", cart.getTotalCost()));
        totalTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        totalTextView.setTextSize(HEADER_TEXT_SIZE);
        totalTextView.setTypeface(null, Typeface.BOLD);
        totalTextView.setTextColor(Color.BLACK);
        totalTextView.setBackgroundColor(Color.YELLOW);

        totalCostLayout.addView(totalTextView);
    }

    /**
     * Back to Menu button click
     * @param view
     */
    public void BackToMenuBtn_Click(View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.order_name), order);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Checkout button click
     * @param view
     */
    public void ProceedToPaymentBtn_Click(View view){
        // only allow users to checkout if they have added at least one item to the cart
        if (this.order.getCart().getItems().size() > 0){
            Intent i = new Intent(this, PlaceOrderActivity.class);
            i.putExtra(getString(R.string.order_name), order);
            startActivityForResult(i, LAUNCH_PAYMENT_ACTIVITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_PAYMENT_ACTIVITY || requestCode == LAUNCH_ORDER_ITEMS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK){ }
            else if (resultCode == Activity.RESULT_CANCELED){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
    }

    /**
     * Edit button click displays a popup allowing the user to edit the order type when reordering a past order
     * @param view
     */
    public void editOrderTypeBtn_Click(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View editOrderTypePopupView = getLayoutInflater().inflate(R.layout.order_type_edit_popup, null);

        orderTypeGroup = (RadioGroup) editOrderTypePopupView.findViewById(R.id.orderTypeGroup);
        if (order instanceof Carryout) orderTypeGroup.check(R.id.carryoutRadioBtn);
        else orderTypeGroup.check(R.id.deliveryRadioBtn);

        deliveryPopup_streetAddress = (EditText) editOrderTypePopupView.findViewById(R.id.deliveryPopup_streetAddress);
        deliveryPopup_city = (EditText) editOrderTypePopupView.findViewById(R.id.deliveryPopup_city);
        deliveryPopup_zip = (EditText) editOrderTypePopupView.findViewById(R.id.deliveryPopup_zip);

        deliveryPopup_stateSpinner = (Spinner) editOrderTypePopupView.findViewById(R.id.deliveryPopup_stateSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryPopup_stateSpinner.setAdapter(adapter);

        deliveryPopup_cancelBtn = (Button) editOrderTypePopupView.findViewById(R.id.deliveryPopup_cancelBtn);
        deliveryPopup_saveBtn = (Button) editOrderTypePopupView.findViewById(R.id.deliveryPopup_saveBtn);

        dialogBuilder.setView(editOrderTypePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        if (order instanceof Delivery) displayDeliverySection();

        deliveryPopup_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        deliveryPopup_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switchToDeliveryType();
            }
        });
    }

    /**
     * Takes a new order and replaces the intents old order with this one
     * @param newOrder Order with the updated information
     */
    private void switchOrderType(Order newOrder){
        Intent i = getIntent();
        i.removeExtra(getString(R.string.order_name));
        i.putExtra(getString(R.string.order_name), newOrder);
        setIntent(i);
        recreate();
    }

    /**
     * Switches an order to a new Delivery order
     */
    private void switchToDeliveryType(){
        Delivery delivery;
        Address address;
        boolean valid = validateDeliveryPopupInput();

        if (valid){
            address = createDeliveryAddress();
            delivery = new Delivery(order.getUserID(), order.getCart(), order.getCustomer(), order.getPayment(), address);
            switchOrderType(delivery);
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
        String state = deliveryPopup_stateSpinner.getSelectedItem().toString();
        String zip = deliveryPopup_zip.getText().toString();

        Address address = new Address(streetAddress, city, state, zip);

        return address;
    }

    /**
     * Edit Order Popup "X" button closes the popup
     * @param view
     */
    public void closeOrderTypedEditBtn_Click(View view){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * Selecting "Carryout" radio button switches the order type of Carryout
     * @param view
     */
    public void handleCarryoutSelection(View view){
        View deliveryAddrSection = dialog.findViewById(R.id.editDeliveryAddressLayout);
        deliveryAddrSection.setVisibility(View.GONE);

        dialog.dismiss();

        if (order instanceof Delivery){
            Carryout carryout = new Carryout(order.getUserID(), order.getCart(), order.getCustomer(), order.getPayment());
            switchOrderType(carryout);
        }
    }

    /**
     * Selecting the "Delivery" radio button displays more fields to create a new Delivery address
     * @param view
     */
    public void handleDeliverySelection(View view){
        displayDeliverySection();
    }

    /**
     * Displays the Delivery address fields to update/create a new address
     */
    private void displayDeliverySection(){
        final float scale = getResources().getDisplayMetrics().density;
        int leftPadding = (int) (5 * scale + 0.5f);

        View deliveryAddrSection = dialog.findViewById(R.id.editDeliveryAddressLayout);
        deliveryAddrSection.setVisibility(View.VISIBLE);

        TextView deliveryHeader = dialog.findViewById(R.id.deliveryAddressText);
        deliveryHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        deliveryHeader.setGravity(Gravity.LEFT);
        deliveryHeader.setPadding(leftPadding, 0, 0, 0);
        deliveryHeader.setBackgroundResource(R.drawable.header_border);

        if (order instanceof Delivery){
            Delivery delivery = (Delivery) order;
            deliveryPopup_streetAddress.setText(delivery.getDeliveryAddress().getStreetAddress());
            deliveryPopup_city.setText(delivery.getDeliveryAddress().getCity());
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) deliveryPopup_stateSpinner.getAdapter();
            deliveryPopup_stateSpinner.setSelection(adapter.getPosition(delivery.getDeliveryAddress().getState()));
            deliveryPopup_zip.setText(delivery.getDeliveryAddress().getZip());
        }
    }

    /**
     * When Edit Items is clicked the button is hidden and the "+" button is shown
     * @param view
     */
    public void editItemsBtn_Click(View view){
        Intent i = getIntent();
        i.removeExtra(getString(R.string.summary_mode));
        i.putExtra(getString(R.string.summary_mode), getString(R.string.past_editable));
        setIntent(i);
        recreate();
    }

    /**
     * This button allows users to go to the menu and add more items to the order
     * @param view
     */
    public void addItemsBtn_Click(View view){
        Intent intent = new Intent(this, OrderItemActivity.class);
        intent.putExtra(getString(R.string.order_name), order);
        startActivityForResult(intent, LAUNCH_ORDER_ITEMS_ACTIVITY);
    }

    /**
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
     */
}