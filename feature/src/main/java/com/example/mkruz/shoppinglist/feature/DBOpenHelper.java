package com.example.mkruz.shoppinglist.feature;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "shopping_list";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "item" + " TEXT, " +
                    "price" + " DECIMAL, " +
                    "completed" + " INTEGER, " +
                    "qty" + " INTEGER);";
    DBOpenHelper(Context context) {
        super(context, "base.db", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
