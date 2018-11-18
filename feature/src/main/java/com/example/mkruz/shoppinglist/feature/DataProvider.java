package com.example.mkruz.shoppinglist.feature;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;

public class DataProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.mkruz.shopinglist.feature.DataProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final int ITEMS = 1;
    static final int ITEM_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "items", ITEMS);
        uriMatcher.addURI(PROVIDER_NAME, "item/#", ITEM_ID);
    }

    TodoDbAdapter db;

    @Override
    public boolean onCreate() {
        Log.i("Provider", "creating");
        try {
            db = new TodoDbAdapter(getContext());
            db.open();
        }catch(Exception e){
            Log.i("Provider", "can't DB");
        }
        return false;
    }

    @Override
    public Uri insert (Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }


    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {
        Log.i("DAtaProvider", "query!");
        Cursor c = null;
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                c = db.getAllTodos();
                break;
            case ITEM_ID:
                String id = uri.getPathSegments().get(1);
                c = db.getTodoTaskCursor(id);
                break;
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case ITEMS:
                return "vnd.android.cursor.dir/vnd.example.students";
            /**
             * Get a particular student
             */
            case ITEM_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
