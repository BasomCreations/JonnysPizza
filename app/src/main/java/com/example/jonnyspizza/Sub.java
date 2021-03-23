package com.example.jonnyspizza;

import java.io.Serializable;

public class Sub extends Item implements Serializable {

    private String type;

    public Sub(String type, double cost, int quantity) {
        super("Sub", cost, quantity);
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
