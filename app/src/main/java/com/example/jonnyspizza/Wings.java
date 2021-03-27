package com.example.jonnyspizza;

import java.io.Serializable;

public class Wings extends Item implements Serializable {

    private String size;
    private String sauce;


    public Wings(String size, String sauce, double cost, int quantity) {
        super("Wings", cost, quantity);
        this.size = size;
        this.sauce = sauce;
    }

    public Wings(String size, String sauce, int quantity) {
        super("Wings", quantity);
        this.size = size;
        this.sauce = sauce;
    }

    public void calculateCost(){
        double total = 0;

        // Add cost for pizza size
        if (this.size != null && this.size.toLowerCase().equals(Price.WINGS_HALF_DOZEN.getName())){
            total += Price.WINGS_HALF_DOZEN.getValue();
        }
        else if (this.size != null && this.size.toLowerCase().equals(Price.WINGS_DOZEN.getName())){
            total += Price.WINGS_DOZEN.getValue();
        }

        setCost(total);
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        if (this.size != null) sb.append(this.size + " ");
        if (this.sauce != null) sb.append(this.sauce);

        return sb.toString();
    }

    public String getSize() {
        return size;
    }

    public String getSauce() {
        return sauce;
    }
}
