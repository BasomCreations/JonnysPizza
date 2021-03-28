package com.example.jonnyspizza;

import java.io.Serializable;

public class Drink extends Item implements Serializable {

    private String type;

    public Drink(String type, double cost, int quantity) {
        super("Drink", cost, quantity);
        this.type = type;
    }

    @Override
    public String getDescription() {

        if (this.type != null){
            return this.type;
        }
        return "";
    }
}
