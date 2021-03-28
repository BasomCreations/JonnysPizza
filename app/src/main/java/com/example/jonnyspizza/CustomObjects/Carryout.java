package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.Cart;

public class Carryout extends Order{

    public Carryout(Cart cart, Customer customer, Payment payment) {
        super(cart, customer, payment);
    }

    public Carryout(Cart cart) {
        super(cart);
    }

    public Carryout() {
    }
}
