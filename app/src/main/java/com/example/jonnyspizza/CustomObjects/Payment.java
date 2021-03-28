package com.example.jonnyspizza.CustomObjects;

import java.io.Serializable;

public class Payment implements Serializable {

    private String creditCartNumber;
    private String securityCode;
    private String expMM;
    private String expYYYY;
    private String billingZip;

    public Payment(String creditCartNumber, String securityCode, String expMM, String expYYYY, String billingZip) {
        this.creditCartNumber = creditCartNumber;
        this.securityCode = securityCode;
        this.expMM = expMM;
        this.expYYYY = expYYYY;
        this.billingZip = billingZip;
    }

    public String getCreditCartNumber() {
        return creditCartNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public String getExpMM() {
        return expMM;
    }

    public String getExpYYYY() {
        return expYYYY;
    }

    public String getBillingZip() {
        return billingZip;
    }
}
