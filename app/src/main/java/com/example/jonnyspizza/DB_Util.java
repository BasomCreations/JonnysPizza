package com.example.jonnyspizza;

public final class DB_Util {

    public static final String DB_NAME = "JonnyPizza.db";
    public static final int VERSION_NUMBER = 7;

    // Order Table
    public static final String TABLE_ORDER = "order_table";
    public static final String ORDER_PK = "ID";
    public static final String ORDER_TYPE = "Order_Type";
        public static final String DELIVERY_TYPE = "Delivery";
        public static final String CARRYOUT_TYPE = "Carryout";
    public static final String ORDER_COST = "Cost";
    public static final String ORDER_DATE = "Date";

    public static final int NUMBER_RECENT_ORDERS = 10;

    // General Item Info
    public static final String ITEM_NAME = "Name";
    public static final String ITEM_COST = "Cost";
    public static final String ITEM_QUANTITY = "Quantity";

    // JSON Strings
    public static final String DELIVERY_ADDRESS_KEY = "deliveryAddress";
    public static final String CUSTOMER_KEY = "customer";
    public static final String PAYMENT_KEY = "payment";
    public static final String CART_KEY = "cart";
    public static final String PIZZA_KEY = "pizza";
    public static final String SUB_KEY = "sub";
    public static final String WINGS_KEY = "wings";
    public static final String DRINK_KEY = "drink";

    // Pizza Table
    public static final String TABLE_PIZZA = "pizza_table";
    public static final String PIZZA_PK = "ID";
    public static final String PIZZA_FK = "OrderID";
    public static final String PIZZA_SIZE = "Size";
    public static final String PIZZA_CRUST = "Crust";
    public static final String PIZZA_SAUCE = "Sauce";
    public static final String PIZZA_CHEESE = "Cheese";
    public static final String PIZZA_TOPPINGS = "Toppings";
    public static final String PIZZA_COST = "Cost";
    public static final String PIZZA_QUANTITY = "Quantity";

    // Sub Table
    public static final String TABLE_SUB = "sub_table";
    public static final String SUB_PK = "ID";
    public static final String SUB_FK = "OrderID";
    public static final String SUB_TYPE = "Type";
    public static final String SUB_COST = "Cost";
    public static final String SUB_QUANTITY = "Quantity";

    // Wings Table
    public static final String TABLE_WINGS = "wings_table";
    public static final String WINGS_PK = "ID";
    public static final String WINGS_FK = "OrderID";
    public static final String WINGS_SIZE = "Size";
    public static final String WINGS_SAUCE = "Sauce";
    public static final String WINGS_COST = "Cost";
    public static final String WINGS_QUANTITY = "Quantity";

    // Drink Table
    public static final String TABLE_DRINK = "drink_table";
    public static final String DRINK_PK = "ID";
    public static final String DRINK_FK = "OrderID";
    public static final String DRINK_TYPE = "Type";
    public static final String DRINK_COST = "Cost";
    public static final String DRINK_QUANTITY = "Quantity";

    // Delivery Address Table
    public static final String TABLE_DELIVERY_ADDRESS = "deliveryAddress_table";
    public static final String DELIVERY_ADDRESS_PK = "ID";
    public static final String DELIVERY_ADDRESS_FK = "OrderID";
    public static final String DELIVERY_ADDRESS_STREET = "Street";
    public static final String DELIVERY_ADDRESS_CITY = "City";
    public static final String DELIVERY_ADDRESS_STATE = "State";
    public static final String DELIVERY_ADDRESS_ZIP = "Zip";

    // Customer Table
    public static final String TABLE_CUSTOMER = "customer_table";
    public static final String CUSTOMER_PK = "ID";
    public static final String CUSTOMER_FK = "OrderID";
    public static final String CUSTOMER_FIRST_NAME = "FirstName";
    public static final String CUSTOMER_LAST_NAME = "LastName";
    public static final String CUSTOMER_EMAIL = "Email";
    public static final String CUSTOMER_PHONE = "Phone";

    // Payment Table
    public static final String TABLE_PAYMENT = "payment_table";
    public static final String PAYMENT_PK = "ID";
    public static final String PAYMENT_FK = "OrderID";
    public static final String PAYMENT_CREDIT_CARD = "CreditCard";
    public static final String PAYMENT_SECURITY_CODE = "SecurityCode";
    public static final String PAYMENT_MONTH = "Month";
    public static final String PAYMENT_YEAR = "Year";
    public static final String PAYMENT_ZIP = "Zip";

    private DB_Util(){ }

}
