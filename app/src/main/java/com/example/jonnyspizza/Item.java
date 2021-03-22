package com.example.jonnyspizza;

import java.io.Serializable;

public class Item implements Serializable {

    private String name;
    private double cost;        // cost of item of 1 quantity
    private int quantity;

    /**
     * Constructor
     * @param name
     * @param cost
     */
    public Item(String name, double cost, int quantity){
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    /**
     * Constructor
     * @param name
     */
    public Item(String name, int quantity){
        this.name = name;
        this.cost = 0;      // default cost is 0 until calculated later
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    protected void setCost(double cost) { this.cost = cost; }     // only visible and usable by subclasses

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
