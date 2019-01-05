package com.example.mkruz.shoppinglist.feature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShopDbAdapter {
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static final String DEBUG_TAG = "SqLiteTodoManager";

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "database1.db";
    private static final String DB_TODO_TABLE = "shops";

    public static final int ID_COLUMN = 0;
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int NAME_COLUMN = 1;
    public static final String KEY_NAME = "name";
    public static final String NAME_OPTIONS = "TEXT NOT NULL";
    public static final int SUBTITLE_COLUMN = 2;
    public static final String KEY_SUBTITLE = "subtitle";
    public static final String SUBTITLE_OPTIONS = "TEXT NOT NULL";
    public static final int LAT_COLUMN = 3;
    public static final String KEY_LAT = "lat";
    public static final String LAT_OPTIONS = "DECIMAL NOT NULL";
    public static final int LNG_COLUMN = 4;
    public static final String KEY_LNG = "lng";
    public static final String LNG_OPTIONS = "DECIMAL NOT NULL";
    public static final int RANGE_COLUMN = 5;
    public static final String KEY_RANGE = "range";
    public static final String RANGE_OPTIONS = "DECIMAL DEFAULT 0";
    public static final int FAVORITE_COLUMN = 6;
    public static final String KEY_FAVORITE = "favorite";
    public static final String FAVORITE_OPTIONS = "BOOLEAN DEFAULT FALSE";

    private static final String DB_CREATE_TODO_TABLE =
            "CREATE TABLE " + DB_TODO_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_NAME + " " + NAME_OPTIONS + ", " +
                    KEY_SUBTITLE + " " + SUBTITLE_OPTIONS + ", " +
                    KEY_LAT + " " + LAT_OPTIONS + ", " +
                    KEY_LNG + " " + LNG_OPTIONS + ", " +
                    KEY_RANGE + " " + RANGE_OPTIONS + ", " +
                    KEY_FAVORITE + " " + FAVORITE_OPTIONS +
                    ");";

    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + DB_TODO_TABLE;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }


    public ShopDbAdapter(Context context) {
        this.context = context;
    }

    public ShopDbAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertShop(String name, String subtitle, float lat, float lng, long range, boolean favorite) {
        ContentValues newShopValues = new ContentValues();
        newShopValues.put(KEY_NAME, name);
        newShopValues.put(KEY_SUBTITLE, subtitle);
        newShopValues.put(KEY_LAT, lat);
        newShopValues.put(KEY_LNG, lng);
        newShopValues.put(KEY_RANGE, range);
        newShopValues.put(KEY_FAVORITE, favorite);
        return db.insert(DB_TODO_TABLE, null, newShopValues);
    }

    public boolean updateShop(Shop shop) {
        return updateShop(shop.getId(), shop.getName(), shop.getSubtitle(), shop.getLat(), shop.getLng(), shop.getRange(), shop.isFavorite());
    }

    public boolean updateShop(long id, String name, String subtitle, float lat, float lng, long range, boolean favorite) {
        String where = KEY_ID + "=" + id;
        ContentValues newShopValues = new ContentValues();
        newShopValues.put(KEY_NAME, name);
        newShopValues.put(KEY_SUBTITLE, subtitle);
        newShopValues.put(KEY_LAT, lat);
        newShopValues.put(KEY_LNG, lng);
        newShopValues.put(KEY_RANGE, range);
        newShopValues.put(KEY_FAVORITE, favorite);
        return db.update(DB_TODO_TABLE, newShopValues, where, null) > 0;
    }

    public boolean deleteShop(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TODO_TABLE, where, null) > 0;
    }

    public Cursor getAllShops() {
        String[] columns = {KEY_ID, KEY_NAME, KEY_SUBTITLE, KEY_LAT, KEY_LNG, KEY_RANGE, KEY_FAVORITE};
        return db.query(DB_TODO_TABLE, columns, null, null, null, null, null);
    }

    public Cursor getShopCursor(String id) {
        String[] columns = {KEY_ID, KEY_NAME, KEY_SUBTITLE, KEY_LAT, KEY_LNG, KEY_RANGE, KEY_FAVORITE};
        String where = KEY_ID + "=" + id;
        return  db.query(DB_TODO_TABLE, columns, where, null, null, null, null);
    }

    public Shop getShop(long id) {
        Cursor cursor = getShopCursor(String.valueOf(id));
        Shop shop = null;
        if(cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(NAME_COLUMN);
            String subtitle = cursor.getString(SUBTITLE_COLUMN);
            float lat = cursor.getFloat(LAT_COLUMN);
            float lng = cursor.getFloat(LNG_COLUMN);
            long range = cursor.getLong(RANGE_COLUMN);
            boolean favorite = cursor.isNull(FAVORITE_COLUMN);
            shop = new Shop(name, subtitle, lat, lng, range);
            shop.setId((int) id);
            shop.setFavorite(favorite);
        }
        return shop;
    }
}

