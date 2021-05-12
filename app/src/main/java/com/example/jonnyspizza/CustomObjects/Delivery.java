package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.Cart;

public class Delivery extends Order{

    private Address deliveryAddress;

    public Delivery(int userID, Cart cart, Customer customer, Payment payment, Address deliveryAddress) {
        super(userID, cart, customer, payment);
        this.deliveryAddress = deliveryAddress;
    }

    public Delivery(Cart cart, Customer customer, Payment payment, Address deliveryAddress) {
        super(cart, customer, payment);
        this.deliveryAddress = deliveryAddress;
    }

    public Delivery(int userID, Cart cart, Address deliveryAddress) {
        super(userID, cart);
        this.deliveryAddress = deliveryAddress;
    }

    public Delivery(Cart cart, Address deliveryAddress) {
        super(cart);
        this.deliveryAddress = deliveryAddress;
    }

    public Delivery(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }
}
