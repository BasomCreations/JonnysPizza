package com.example.jonnyspizza;

/**
 * Simple enum type to represent the prices of the menu
 */
public enum Price {

    SMALL(8.00, "small"), MEDIUM(10.00, "medium"), LARGE(12.00, "large"),
    EXTRA_CHEESE(1.00, "extra"),
    ADDITIONAL_TOPPINGS(0.50, "additional toppings");

    private double value;
    private String name;

    Price(double value, String name){
        this.value = value;
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
