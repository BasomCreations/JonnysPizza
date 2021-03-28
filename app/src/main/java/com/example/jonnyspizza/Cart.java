package com.example.jonnyspizza;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {

    private ArrayList<Item> items;
    private double totalCost;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalCost = 0;
    }

    public void addItem(Item item){
        this.items.add(item);
        totalCost += (item.getCost() * item.getQuantity());
    }

    public void removeItem(Item item){
        double cost = item.getCost() * item.getQuantity();

        this.items.remove(item);
        this.totalCost -= cost;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
