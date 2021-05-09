package com.example.jonnyspizza;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Pizza extends Item implements Serializable {

    //private final static String name = "Pizza";
    //private String name = "Pizza";
    @JsonProperty(DB_Util.PIZZA_SIZE)
    private String size;

    @JsonProperty(DB_Util.PIZZA_CRUST)
    private String crust;

    @JsonProperty(DB_Util.PIZZA_SAUCE)
    private String sauce;

    @JsonProperty(DB_Util.PIZZA_CHEESE)
    private String cheese;

    @JsonProperty(DB_Util.PIZZA_TOPPINGS)
    private ArrayList<String> toppings = new ArrayList<>();

    public Pizza(int quantity, String size, String crust, String sauce, String cheese) {
        super("Pizza", quantity);
        this.size = size;
        this.crust = crust;
        this.sauce = sauce;
        this.cheese = cheese;
    }

    /**
     * Empty constructor only used for deserialization from JSON
     */
    protected Pizza(){ }

    public void addToppings(String topping){
        this.toppings.add(topping);
    }

    public void calculateCost(){
        double total = 0;

        // Add cost for pizza size
        if (this.size != null && this.size.toLowerCase().equals(Price.SMALL.getName())){
            total += Price.SMALL.getValue();
        }
        else if (this.size != null && this.size.toLowerCase().equals(Price.MEDIUM.getName())){
            total += Price.MEDIUM.getValue();
        }
        else if (this.size != null && this.size.toLowerCase().equals(Price.LARGE.getName())){
            total += Price.LARGE.getValue();
        }

        // Add cost for extra cheese
        if (this.cheese != null && this.cheese.toLowerCase().equals(Price.EXTRA_CHEESE.getName())){
            total += Price.EXTRA_CHEESE.getValue();
        }

        // Add cost for toppings (2 free, additional $0.50 each)
        int num_toppings = toppings.size();
        int additional_toppings = num_toppings - 2;
        if (additional_toppings > 0){
            total += (additional_toppings * Price.ADDITIONAL_TOPPINGS.getValue());
        }

        setCost(total);
    }

    // Getters

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

    /**
     * Returns a semi-colon separated string of the pizza toppings
     * @return
     */
    public String getToppingsString(){
        StringBuilder sb = new StringBuilder();

        int numToppings = this.toppings.size();

        if (numToppings > 0){
            for (int i = 0; i < numToppings-1; i++){
                sb.append(this.toppings.get(i) + ";");
            }
            sb.append(this.toppings.get(numToppings - 1));
        }

        return sb.toString();
    }

    /**
     * Gets a formatted string of the description of the item
     * @return String
     */
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        if (this.size != null) { sb.append(this.size + " - "); }
        if (this.crust != null) { sb.append(this.crust + " Crust\n"); }
        if (this.sauce != null) { sb.append(this.sauce + " Sauce\n"); }
        if (this.cheese != null) { sb.append(this.cheese +  " Cheese\n"); }

        int numToppings = this.toppings.size();

        if (numToppings == 1){
            sb.append(this.toppings.get(0));
        }

        else if (numToppings > 1) {

            for (int i = 0; i < numToppings - 1; i++) {
                sb.append(this.toppings.get(i) + ", ");
            }
            sb.append(this.toppings.get(numToppings - 1));      // Add the last topping
        }

        return sb.toString();
    }
}
