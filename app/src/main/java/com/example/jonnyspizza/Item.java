package com.example.jonnyspizza;

import java.io.Serializable;

public class Item implements Serializable {

    private String name;
    private double cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Item(String name, double cost){
        this.name = name;
        this.cost = cost;
    }

    public Item(String name){
        this.name = name;
    }
}
