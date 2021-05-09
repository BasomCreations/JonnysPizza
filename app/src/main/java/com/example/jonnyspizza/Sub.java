package com.example.jonnyspizza;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Sub extends Item implements Serializable {

    @JsonProperty(DB_Util.SUB_TYPE)
    private String type;

    public Sub(String type, double cost, int quantity) {
        super("Sub", cost, quantity);
        this.type = type;
    }

    /**
     * Empty constructor only used for deserialization from JSON
     */
    protected Sub(){}

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
