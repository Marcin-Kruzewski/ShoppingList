package com.example.mkruz.shoppinglist.feature;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = view.findViewById(R.id.tvItem);
        //String id = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(TodoDbAdapter.KEY_ID)));
        String qty = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(TodoDbAdapter.KEY_QTY)));
        //String price = String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(TodoDbAdapter.KEY_PRICE)));
        String body =  qty + " " + cursor.getString(cursor.getColumnIndexOrThrow(TodoDbAdapter.KEY_DESCRIPTION));
        tvBody.setText(body);
    }
}