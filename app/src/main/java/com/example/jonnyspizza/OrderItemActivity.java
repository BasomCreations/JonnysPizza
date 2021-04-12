package com.example.jonnyspizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.jonnyspizza.CustomObjects.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jonnyspizza.ui.main.SectionsPagerAdapter;

public class OrderItemActivity extends AppCompatActivity {

    private final static int PIZZA_INDEX = 0;
    private final static int SUB_INDEX = 1;
    private final static int WINGS_INDEX = 2;
    private final static int DRINKS_INDEX = 3;

    private final static int LAUNCH_CONFIRMATION_ACTIVITY = 1;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button backToMenuBtn, checkoutBtn;
    private Order order;
    private Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.order = (Order) getIntent().getSerializableExtra(getString(R.string.order_name));
        this.cart = this.order.getCart();

        setContentView(R.layout.activity_order_item);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount());       // retains the info between all the menu fragments
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        updateCartCost();
    }

    /**
     * Creates the popup to alert user that the item has been added to the order
     * @param view
     */
    public void createNewDialog(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View addedItemPopupView = getLayoutInflater().inflate(R.layout.added_item_popup, null);

        backToMenuBtn = addedItemPopupView.findViewById(R.id.backToMenuBtn);
        checkoutBtn = addedItemPopupView.findViewById(R.id.checkoutBtn);

        dialogBuilder.setView(addedItemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // Update the cost TextView
                updateCartCost();

                // Refresh all of the fragments
                refreshFragments();
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                proceedToCheckout(v);
            }
        });
    }

    /**
     * Advance to the checkout screen
     */
    public void proceedToCheckout(View v){
        // only allow users to checkout if they have added at least one item to the cart
        if (this.cart.getItems().size() > 0) {
            Intent i = new Intent(this, OrderSummaryActivity.class);
            i.putExtra(getString(R.string.order_name), order);
            i.putExtra(getString(R.string.summary_mode), getString(R.string.new_editable));
            //startActivity(i);
            startActivityForResult(i, LAUNCH_CONFIRMATION_ACTIVITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CONFIRMATION_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK){
                this.order = (Order) data.getSerializableExtra(getString(R.string.order_name));
                this.cart = this.order.getCart();
                updateCartCost();
                refreshFragments();
            }
            if (resultCode == Activity.RESULT_CANCELED){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
    }

    /**
     * Getter for the cart
     * @return
     */
    public Cart getCart(){
        return this.cart;
    }

    /**
     * Clear all of the changed features of the menu fragments
     */
    private void refreshFragments(){
        // Set the current fragment to the first one (Pizza)
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setCurrentItem(PIZZA_INDEX);

        PizzaFragment pizzaFragment = (PizzaFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + PIZZA_INDEX);
        if (pizzaFragment != null) pizzaFragment.refresh();

        SubFragment subFragment = (SubFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + SUB_INDEX);
        if (subFragment != null) subFragment.refresh();

        WingsFragment wingsFragment = (WingsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + WINGS_INDEX);
        if (wingsFragment != null) wingsFragment.refresh();

        DrinkFragment drinkFragment = (DrinkFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + DRINKS_INDEX);
        if (drinkFragment != null) drinkFragment.refresh();
    }

    /**
     * Updates the TextView containing the total cost of the cart
     */
    private void updateCartCost(){
        TextView cartCostTV = findViewById(R.id.cartCostTextView);
        String totalCostString = String.format("($%.2f)", this.cart.getTotalCost());
        cartCostTV.setText(totalCostString);
    }
}