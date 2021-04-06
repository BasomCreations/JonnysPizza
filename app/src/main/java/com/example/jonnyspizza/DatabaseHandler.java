package com.example.jonnyspizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

        String CREATE_PIZZA_TABLE;
        String CREATE_SUB_TABLE;
        String CREATE_WINGS_TABLE;
        String CREATE_DRINK_TABLE;

        db.execSQL(CREATE_ORDER_TABLE);

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
        String test  = "DROP TABLE IF EXISTS DB_Util.TABLE_ORDER";
        db.execSQL(test);

        // create new tables
        onCreate(db);

    }

    public boolean addOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = handleOrder(order);
        long result = db.insert(DB_Util.TABLE_ORDER, null, values);
        db.close();

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From " + DB_Util.TABLE_ORDER, null);
        int records = cursor.getCount();
        db.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }

    }

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
}
