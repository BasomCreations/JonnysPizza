package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.Cart;

public class Carryout extends Order{

    public Carryout(int userID, Cart cart, Customer customer, Payment payment) {
        super(userID, cart, customer, payment);
    }

    public Carryout(Cart cart, Customer customer, Payment payment) {
        super(cart, customer, payment);
    }

    public Carryout(int userID, Cart cart) {
        super(userID, cart);
    }

    public Carryout(Cart cart) {
        super(cart);
    }

    public Carryout() {
    }
}
