package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.DB_Util;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Address implements Serializable {

    @JsonProperty(DB_Util.DELIVERY_ADDRESS_STREET)
    private String streetAddress;

    @JsonProperty(DB_Util.DELIVERY_ADDRESS_CITY)
    private String city;

    @JsonProperty(DB_Util.DELIVERY_ADDRESS_STATE)
    private String state;

    @JsonProperty(DB_Util.DELIVERY_ADDRESS_ZIP)
    private String zip;

    public Address(String streetAddress, String city, String state, String zip) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    /**
     * Empty constructor only used for deserialization from JSON
     */
    protected Address(){}

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
