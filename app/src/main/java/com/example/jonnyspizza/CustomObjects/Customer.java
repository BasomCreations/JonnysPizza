package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.DB_Util;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Customer implements Serializable {

    @JsonProperty(DB_Util.CUSTOMER_FIRST_NAME)
    private String firstName;

    @JsonProperty(DB_Util.CUSTOMER_LAST_NAME)
    private String lastName;

    @JsonProperty(DB_Util.CUSTOMER_EMAIL)
    private String email;

    @JsonProperty(DB_Util.CUSTOMER_PHONE)
    private String phoneNumber;

    public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Empty constructor only used for deserialization from JSON
     */
    protected Customer(){}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
