package com.example.jonnyspizza;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Drink extends Item implements Serializable {

    @JsonProperty(DB_Util.DRINK_TYPE)
    private String type;

    public Drink(String type, double cost, int quantity) {
        super("Drink", cost, quantity);
        this.type = type;
    }

    /**
     * Empty constructor only used for deserialization from JSON
     */
    protected Drink(){}

    @Override
    public String getDescription() {

        if (this.type != null){
            return this.type;
        }
        return "";
    }

    public String getType() {
        return type;
    }
}
