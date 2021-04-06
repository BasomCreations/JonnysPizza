package com.example.jonnyspizza;

public final class DB_Util {

    public static final int VERSION_NUMBER = 2;
    public static final String DB_NAME = "JonnyPizza.db";

    // Order Table
    public static final String TABLE_ORDER = "order_table";
    public static final String ORDER_PK = "ID";
    public static final String ORDER_TYPE = "Order_Type";
        public static final String DELIVERY_TYPE = "Delivery";
        public static final String CARRYOUT_TYPE = "Carryout";
    public static final String ORDER_COST = "Cost";
    public static final String ORDER_DATE = "Date";

    // Pizza Table
    public static final String TABLE_PIZZA = "pizza_table";
    public static final String PIZZA_PK = "ID";
    public static final String PIZZA_FK = "OrderID";
    public static final String PIZZA_SIZE = "Size";
    public static final String PIZZA_CRUST = "Crust";
    public static final String PIZZA_SAUCE = "Sauce";
    public static final String PIZZA_TOPPINGS = "Toppings";
    public static final String PIZZA_QUANTITY = "Quantity";

    // Sub Table
    public static final String TABLE_SUB = "sub_table";
    public static final String SUB_PK = "ID";
    public static final String SUB_FK = "OrderID";
    public static final String SUB_TYPE = "Type";
    public static final String SUB_QUANTITY = "Quantity";

    // Wings Table
    public static final String TABLE_WINGS = "wings_table";
    public static final String WINGS_PK = "ID";
    public static final String WINGS_FK = "OrderID";
    public static final String WINGS_SIZE = "Size";
    public static final String WINGS_SAUCE = "Sauce";
    public static final String WINGS_QUANTITY = "Quantity";

    // Drink Table
    public static final String TABLE_DRINK = "drink_table";
    public static final String DRINK_PK = "ID";
    public static final String DRINK_FK = "OrderID";
    public static final String DRINK_TYPE = "Type";
    public static final String DRINK_QUANTITY = "Quantity";

    private DB_Util(){ }

    public static String createOrderTable(){
        return "CREATE TABLE " + TABLE_ORDER + " (" + ORDER_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDER_TYPE + " TEXT, " + ORDER_COST + " REAL, " + ORDER_DATE + " TEXT)";
    }
}
