package com.example.jonnyspizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void orderPizza(View view){
        View v = findViewById(R.id.orderPizzaBtn);
        //Intent intent = new Intent(this, OrderPizza.class);
        Intent intent = new Intent(this, OrderItemActivity.class);
        startActivity(intent);
    }
}