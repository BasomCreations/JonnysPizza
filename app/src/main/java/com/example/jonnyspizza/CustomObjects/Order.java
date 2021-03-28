package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.Cart;

import java.io.Serializable;

public abstract class Order implements Serializable {

    private Cart cart;
    private Customer customer;
    private Payment payment;

    public Order(Cart cart, Customer customer, Payment payment) {
        this.cart = cart;
        this.customer = customer;
        this.payment = payment;
    }

    public Order(Cart cart) {
        this.cart = cart;
    }

    public Order(){
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
