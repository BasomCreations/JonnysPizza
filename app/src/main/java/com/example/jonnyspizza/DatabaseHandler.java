package com.example.jonnyspizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jonnyspizza.CustomObjects.Carryout;
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

        String CREATE_SUB_TABLE;
        String CREATE_WINGS_TABLE;
        String CREATE_DRINK_TABLE;

        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_PIZZA_TABLE);

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

        // Insert all of the items
        if (orderID != null){
            handleItems(db, orderID, order.getCart());
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
            success = success && result;            // once an individual insertion is unsuccessful, entire process considered unsuccessful
        }
        return success;
    }

    /**
     * Inserts a Pizza into the pizza_table
     * @param db
     * @param orderID ID of the corresponding order
     * @param pizza
     * @return boolean true if the insertions were successful
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
}
