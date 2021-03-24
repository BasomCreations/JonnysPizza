package com.example.jonnyspizza;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonnyspizza.ui.main.SectionsPagerAdapter;

import org.w3c.dom.Text;

public class OrderItemActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button backToMenuBtn, checkoutBtn;
    private Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cart = new Cart();

        setContentView(R.layout.activity_order_item);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
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
                // define save button here
                dialog.dismiss();
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToCheckout();
                //dialog.dismiss();
            }
        });
    }

    /**
     * Advance to the checkout screen
     */
    private void proceedToCheckout(){
        Intent i = new Intent(this, DisplayPizzaOrderActivity.class);
        i.putExtra(getString(R.string.cart_name), cart);
        startActivity(i);
    }

    /**
     * Getter for the cart
     * @return
     */
    public Cart getCart(){
        return this.cart;
    }
}