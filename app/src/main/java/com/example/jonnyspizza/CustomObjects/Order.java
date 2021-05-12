package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.Cart;

import java.io.Serializable;

public abstract class Order implements Serializable {

    private int userID;
    private Cart cart;
    private Customer customer;
    private Payment payment;

    /** Constructors */
    public Order(int userID, Cart cart, Customer customer, Payment payment) {
        this.userID = userID;
        this.cart = cart;
        this.customer = customer;
        this.payment = payment;
    }

    public Order(Cart cart, Customer customer, Payment payment) {
        this.cart = cart;
        this.customer = customer;
        this.payment = payment;
    }

    public Order(int userID, Cart cart) {
        this.userID = userID;
        this.cart = cart;
    }

    public Order(Cart cart) {
        this.cart = cart;
    }

    public Order(int userID) {
        this.userID = userID;
    }

    public Order(){
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
