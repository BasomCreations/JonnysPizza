package com.example.jonnyspizza.CustomObjects;

import java.io.Serializable;

public class Address implements Serializable {

    private String streetAddress;
    private String city;
    private String state;
    private String zip;

    public Address(String streetAddress, String city, String state, String zip) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}
