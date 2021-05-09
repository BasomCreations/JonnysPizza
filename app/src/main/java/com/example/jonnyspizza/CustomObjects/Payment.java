package com.example.jonnyspizza.CustomObjects;

import com.example.jonnyspizza.DB_Util;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Payment implements Serializable {

    @JsonProperty(DB_Util.PAYMENT_CREDIT_CARD)
    private String creditCartNumber;

    @JsonProperty(DB_Util.PAYMENT_SECURITY_CODE)
    private String securityCode;

    @JsonProperty(DB_Util.PAYMENT_MONTH)
    private String expMM;

    @JsonProperty(DB_Util.PAYMENT_YEAR)
    private String expYYYY;

    @JsonProperty(DB_Util.PAYMENT_ZIP)
    private String billingZip;

    public Payment(String creditCartNumber, String securityCode, String expMM, String expYYYY, String billingZip) {
        this.creditCartNumber = creditCartNumber;
        this.securityCode = securityCode;
        this.expMM = expMM;
        this.expYYYY = expYYYY;
        this.billingZip = billingZip;
    }

    /**
     * Empty constructor only used for deserialization from JSON
     */
    protected Payment(){}

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
