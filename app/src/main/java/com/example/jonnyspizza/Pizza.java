package com.example.jonnyspizza;

import java.io.Serializable;
import java.util.ArrayList;

public class Pizza extends Item implements Serializable {

    //private final static String name = "Pizza";
    //private String name = "Pizza";
    private String size;
    private String crust;
    private String sauce;
    private String cheese;
    private ArrayList<String> toppings = new ArrayList<>();

    public Pizza(String size, String crust, String sauce, String cheese) {
        super("Pizza");
        this.size = size;
        this.crust = crust;
        this.sauce = sauce;
        this.cheese = cheese;
    }

    public void addToppings(String topping){
        this.toppings.add(topping);
    }

    private void calculateCost(){

    }

    public String getSize() {
        return size;
    }

    public String getCrust() {
        return crust;
    }

    public String getSauce() {
        return sauce;
    }

    public String getCheese() {
        return cheese;
    }

    public ArrayList<String> getToppings() {
        return toppings;
    }
}
