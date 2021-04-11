package com.example.jonnyspizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jonnyspizza.CustomObjects.Address;
import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, DB_Util.DB_NAME, null, DB_Util.VERSION_NUMBER);
    }

    /**
     * This method is called the first time the db is accessed
     * Should contain code to create a new db
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_Util.TABLE_ORDER + " (" + DB_Util.ORDER_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_Util.ORDER_TYPE + " TEXT, " + DB_Util.ORDER_COST + " REAL, " + DB_Util.ORDER_DATE + " TEXT)"; //DB_Util.createOrderTable();

        String CREATE_PIZZA_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_Util.TABLE_PIZZA + " (" + DB_Util.PIZZA_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_Util.PIZZA_FK + " INTEGER, " + DB_Util.PIZZA_SIZE + " TEXT, " + DB_Util.PIZZA_CRUST + " TEXT, " + DB_Util.PIZZA_SAUCE + " TEXT, " + DB_Util.PIZZA_CHEESE + " TEXT, " +
                DB_Util.PIZZA_COST + " REAL, " + DB_Util.PIZZA_TOPPINGS + " TEXT, " + DB_Util.PIZZA_QUANTITY +
                " INTEGER, FOREIGN KEY(" + DB_Util.PIZZA_FK + ") REFERENCES " + DB_Util.TABLE_ORDER + "(" + DB_Util.ORDER_PK + "))";

        String CREATE_SUB_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_Util.TABLE_SUB + " (" + DB_Util.SUB_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_Util.SUB_FK + " INTEGER, " + DB_Util.SUB_TYPE + " TEXT, " + DB_Util.SUB_COST + " REAL, " + DB_Util.SUB_QUANTITY +
                " INTEGER, FOREIGN KEY(" + DB_Util.SUB_FK + ") REFERENCES " + DB_Util.TABLE_ORDER + "(" + DB_Util.ORDER_PK + "))";

        String CREATE_WINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_Util.TABLE_WINGS + " (" + DB_Util.WINGS_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_Util.WINGS_FK + " INTEGER, " + DB_Util.WINGS_SIZE + " TEXT, " + DB_Util.WINGS_SAUCE + " TEXT, " + DB_Util.WINGS_COST + " REAL, " +
                DB_Util.WINGS_QUANTITY + " INTEGER, FOREIGN KEY(" + DB_Util.WINGS_FK + ") REFERENCES " + DB_Util.TABLE_ORDER + "(" + DB_Util.ORDER_PK + "))";

        String CREATE_DRINK_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_Util.TABLE_DRINK + " (" +DB_Util.DRINK_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_Util.DRINK_FK + " INTEGER, " + DB_Util.DRINK_TYPE + " TEXT," + DB_Util.DRINK_COST + " REAL, " + DB_Util.DRINK_QUANTITY + " INTEGER, FOREIGN KEY(" +
                DB_Util.DRINK_FK + ") REFERENCES " + DB_Util.TABLE_ORDER + "(" + DB_Util.ORDER_PK + "))";

        String CREATE_DELIVERY_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_Util.TABLE_DELIVERY_ADDRESS + " (" + DB_Util.DELIVERY_ADDRESS_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_Util.DELIVERY_ADDRESS_FK + " INTEGER, " + DB_Util.DELIVERY_ADDRESS_STREET + " TEXT," + DB_Util.DELIVERY_ADDRESS_CITY + " TEXT, " + DB_Util.DELIVERY_ADDRESS_STATE + " TEXT, " +
                DB_Util.DELIVERY_ADDRESS_ZIP + " TEXT, FOREIGN KEY(" + DB_Util.DELIVERY_ADDRESS_FK + ") REFERENCES " + DB_Util.TABLE_ORDER + "(" + DB_Util.ORDER_PK + "))";

        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_PIZZA_TABLE);
        db.execSQL(CREATE_SUB_TABLE);
        db.execSQL(CREATE_WINGS_TABLE);
        db.execSQL(CREATE_DRINK_TABLE);
        db.execSQL(CREATE_DELIVERY_ADDRESS_TABLE);

    }

    /**
     * called if the db version number changes
     * prevents previous user apps from breaking if the database is changed
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_Util.TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Util.TABLE_PIZZA);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Util.TABLE_SUB);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Util.TABLE_WINGS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Util.TABLE_DRINK);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Util.TABLE_DELIVERY_ADDRESS);

        // create new tables
        onCreate(db);

    }

    /**
     * Adds an order to the database
     * @param order
     * @return boolean - true if successfully added
     */
    public boolean addOrder(Order order){
        boolean success = true;

        SQLiteDatabase db = this.getWritableDatabase();

        // Insert the Order into the DB
        ContentValues values = handleOrder(order);
        long result = db.insert(DB_Util.TABLE_ORDER, null, values);
        if (result == -1) {return false; }

        String orderID = getLastRowID(db);

        // If Delivery, insert delivery address
        if (orderID != null && order instanceof Delivery){
            Delivery delivery = (Delivery) order;
            success = handleDeliveryAddress(db, orderID, delivery.getDeliveryAddress());
        }

        // Insert all of the items
        if (orderID != null){
            success = handleItems(db, orderID, order.getCart()) & success;
        }
        else { success = false; }

        db.close();

        /*db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_ORDER, null);
        int records = cursor.getCount();
        db.close();*/

        return success;

    }

    /**
     * Parse the Order for data to add to the database
     * @param order
     * @return ContentValues containing the data to add to the db
     */
    private ContentValues handleOrder(Order order){
        ContentValues values = new ContentValues();

        if (order instanceof Carryout){
            values.put(DB_Util.ORDER_TYPE, DB_Util.CARRYOUT_TYPE);
        }
        else{
            values.put(DB_Util.ORDER_TYPE, DB_Util.DELIVERY_TYPE);
        }

        values.put(DB_Util.ORDER_COST, order.getCart().getTotalCost());

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        values.put(DB_Util.ORDER_DATE, strDate);

        return values;
    }

    /**
     * Inserts Delivery Address from an Order into the DB
     * @param db
     * @param orderID ID of the corresponding order
     * @param deliveryAddress
     * @return boolean true if the insertion was successful
     */
    private boolean handleDeliveryAddress(SQLiteDatabase db, String orderID, Address deliveryAddress){
        boolean success = true;

        ContentValues values = new ContentValues();

        values.put(DB_Util.DELIVERY_ADDRESS_FK, orderID);
        values.put(DB_Util.DELIVERY_ADDRESS_STREET, deliveryAddress.getStreetAddress());
        values.put(DB_Util.DELIVERY_ADDRESS_CITY, deliveryAddress.getCity());
        values.put(DB_Util.DELIVERY_ADDRESS_STATE, deliveryAddress.getState());
        values.put(DB_Util.DELIVERY_ADDRESS_ZIP, deliveryAddress.getZip());

        long result = db.insert(DB_Util.TABLE_DELIVERY_ADDRESS, null, values);
        if (result == -1) {success = false; }

        return success;
    }

    /**
     * Inserts Items from a Cart into the DB
     * @param db
     * @param orderID ID of the corresponding order
     * @param cart
     * @return boolean true if the insertions were successful
     */
    private boolean handleItems(SQLiteDatabase db, String orderID, Cart cart){
        boolean success = true;         // success of inserting all the items
        boolean result = true;          // success of inserting an individual item

        for (Item item: cart.getItems()) {
            if (item instanceof Pizza){
                result = handlePizza(db, orderID, (Pizza)item);
            }
            else if (item instanceof Sub){
                result = handleSub(db, orderID, (Sub)item);
            }
            else if (item instanceof Wings){
                result = handleWings(db, orderID, (Wings)item);
            }
            else if (item instanceof Drink){
                result = handleDrink(db, orderID, (Drink)item);
            }
            success = success && result;            // once an individual insertion is unsuccessful, entire process considered unsuccessful
        }
        return success;
    }

    /**
     * Inserts a Pizza into the pizza_table
     * @param db
     * @param orderID ID of the corresponding order
     * @param pizza
     * @return boolean true if the insertion was successful
     */
    private boolean handlePizza(SQLiteDatabase db, String orderID, Pizza pizza){
        boolean success = true;

        ContentValues values = new ContentValues();

        values.put(DB_Util.PIZZA_FK, orderID);
        values.put(DB_Util.PIZZA_SIZE, pizza.getSize());
        values.put(DB_Util.PIZZA_CRUST, pizza.getCrust());
        values.put(DB_Util.PIZZA_SAUCE, pizza.getSauce());
        values.put(DB_Util.PIZZA_CHEESE, pizza.getCheese());
        values.put(DB_Util.PIZZA_TOPPINGS, pizza.getToppingsString());
        values.put(DB_Util.PIZZA_COST, pizza.getCost());
        values.put(DB_Util.PIZZA_QUANTITY, pizza.getQuantity());

        long result = db.insert(DB_Util.TABLE_PIZZA, null, values);
        if (result == -1) {success = false; }

        return success;
    }

    /**
     * Inserts a Sub into the sub_table
     * @param db
     * @param orderID ID of the corresponding order
     * @param sub
     * @return boolean true if the insertion was successful
     */
    private boolean handleSub(SQLiteDatabase db, String orderID, Sub sub){
        boolean success = true;

        ContentValues values = new ContentValues();

        values.put(DB_Util.SUB_FK, orderID);
        values.put(DB_Util.SUB_TYPE, sub.getType());
        values.put(DB_Util.SUB_COST, sub.getCost());
        values.put(DB_Util.SUB_QUANTITY, sub.getQuantity());

        long result = db.insert(DB_Util.TABLE_SUB, null, values);
        if (result == -1) {success = false; }

        return success;
    }

    /**
     * Inserts a Wings object into the wings_table
     * @param db
     * @param orderID ID of the corresponding order
     * @param wings
     * @return boolean true if the insertion was successful
     */
    private boolean handleWings(SQLiteDatabase db, String orderID, Wings wings){
        boolean success = true;

        ContentValues values = new ContentValues();

        values.put(DB_Util.WINGS_FK, orderID);
        values.put(DB_Util.WINGS_SIZE, wings.getSize());
        values.put(DB_Util.WINGS_SAUCE, wings.getSauce());
        values.put(DB_Util.WINGS_COST, wings.getCost());
        values.put(DB_Util.WINGS_QUANTITY, wings.getQuantity());

        long result = db.insert(DB_Util.TABLE_WINGS, null, values);
        if (result == -1) {success = false; }

        return success;
    }

    /**
     * Inserts a Drink into the drink_table
     * @param db
     * @param orderID ID of the corresponding order
     * @param drink
     * @return boolean true if the insertion was successful
     */
    private boolean handleDrink(SQLiteDatabase db, String orderID, Drink drink){
        boolean success = true;

        ContentValues values = new ContentValues();

        values.put(DB_Util.DRINK_FK, orderID);
        values.put(DB_Util.DRINK_TYPE, drink.getType());
        values.put(DB_Util.DRINK_COST, drink.getCost());
        values.put(DB_Util.DRINK_QUANTITY, drink.getQuantity());

        long result = db.insert(DB_Util.TABLE_DRINK, null, values);
        if (result == -1) {success = false; }

        return success;
    }

    /**
     * Retrieves the ID of the last row inserted into the database
     * @param db
     * @return String with the ID
     */
    private String getLastRowID(SQLiteDatabase db){
        String id;
        Cursor cursor = db.rawQuery("Select last_insert_rowid()", null);

        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
            return id;
        }
        return null;
    }

    /**
     * Query for the most recent orders
     * @return ArrayList<ContentValues> containing the 10 most recent orders
     */
    public ArrayList<ContentValues> getRecentOrders(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_ORDER + " ORDER BY datetime(" + DB_Util.ORDER_DATE + ") DESC LIMIT " + DB_Util.NUMBER_RECENT_ORDERS, null);

        ContentValues contentValues = new ContentValues();

        ArrayList<ContentValues> resultsList = new ArrayList<>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String orderID = cursor.getString(0);
                String orderType = cursor.getString(1);
                String orderCost = cursor.getString(2);
                String orderDate = cursor.getString(3);

                ContentValues values = new ContentValues();
                values.put(DB_Util.ORDER_PK, orderID);
                values.put(DB_Util.ORDER_TYPE, orderType);
                values.put(DB_Util.ORDER_COST, orderCost);
                values.put(DB_Util.ORDER_DATE, orderDate);

                resultsList.add(values);

            } while (cursor.moveToNext());
        }

        db.close();

        return resultsList;
    }

    public Address getDeliveryAddress(String orderID){
        Address address = null;
        String street, city, state, zip;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_DELIVERY_ADDRESS + " WHERE " + DB_Util.DELIVERY_ADDRESS_FK + " = " + orderID, null);

        if (cursor.moveToFirst()) {
            street = cursor.getString(2);
            city = cursor.getString(3);
            state = cursor.getString(4);
            zip = cursor.getString(5);

            address = new Address(street, city, state, zip);
        }

        db.close();

        return address;
    }

    /**
     * Gets a Cart with the items ordered from the specified order
     * @param orderID ID of the past order
     * @return Cart
     */
    public Cart getOrderItems(String orderID){
        Cart cart = new Cart();
        SQLiteDatabase db = this.getReadableDatabase();

        addPizzasToCart(db, orderID, cart);
        addSubsToCart(db, orderID, cart);
        addWingsToCart(db, orderID, cart);
        addDrinksToCart(db, orderID, cart);

        db.close();

        return cart;
    }

    /**
     * Get all of the pizzas for a specified order
     * @param db
     * @param orderID ID of the past order
     * @param cart Cart to add the pizzas
     */
    private void addPizzasToCart(SQLiteDatabase db, String orderID, Cart cart){

        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_PIZZA + " WHERE " + DB_Util.PIZZA_FK + " = " + orderID, null);

        if (cursor.moveToFirst()){
            do {

                String size, crust, sauce, cheese;
                String[] toppings;
                double cost;
                int quantity;

                size = cursor.getString(2);
                crust = cursor.getString(3);
                sauce = cursor.getString(4);
                cheese = cursor.getString(5);
                cost = cursor.getDouble(6);
                toppings = cursor.getString(7).split(";");
                quantity = cursor.getInt(8);

                Pizza pizza = new Pizza(quantity, size, crust, sauce, cheese);
                for (String topping: toppings) {
                    pizza.addToppings(topping);
                }
                pizza.setCost(cost);
                cart.addItem(pizza);

            } while (cursor.moveToNext());
        }
    }

    /**
     * Get all of the subs for a specified order
     * @param db
     * @param orderID ID of the past order
     * @param cart Cart to add the subs
     */
    private void addSubsToCart(SQLiteDatabase db, String orderID, Cart cart){

        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_SUB + " WHERE " + DB_Util.SUB_FK + " = " + orderID, null);

        if (cursor.moveToFirst()){
            do {

                String type;
                double cost;
                int quantity;

                type = cursor.getString(2);
                cost = cursor.getDouble(3);
                quantity = cursor.getInt(4);

                Sub sub = new Sub(type, cost, quantity);
                cart.addItem(sub);

            } while (cursor.moveToNext());
        }
    }

    /**
     * Get all of the wings for a specified order
     * @param db
     * @param orderID ID of the past order
     * @param cart Cart to add the wings
     */
    private void addWingsToCart(SQLiteDatabase db, String orderID, Cart cart){

        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_WINGS + " WHERE " + DB_Util.WINGS_FK + " = " + orderID, null);

        if (cursor.moveToFirst()){
            do {

                String size, sauce;
                double cost;
                int quantity;

                size = cursor.getString(2);
                sauce = cursor.getString(3);
                cost = cursor.getDouble(4);
                quantity = cursor.getInt(5);

                Wings wings = new Wings(size, sauce, cost, quantity);
                cart.addItem(wings);

            } while (cursor.moveToNext());
        }
    }

    /**
     * Get all of the drinks for a specified order
     * @param db
     * @param orderID ID of the past order
     * @param cart Cart to add the drinks
     */
    private void addDrinksToCart(SQLiteDatabase db, String orderID, Cart cart){

        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_DRINK + " WHERE " + DB_Util.DRINK_FK + " = " + orderID, null);

        if (cursor.moveToFirst()){
            do {

                String type;
                double cost;
                int quantity;

                type = cursor.getString(2);
                cost = cursor.getDouble(3);
                quantity = cursor.getInt(4);

                Drink drink = new Drink(type, cost, quantity);
                cart.addItem(drink);

            } while (cursor.moveToNext());
        }
    }
}
