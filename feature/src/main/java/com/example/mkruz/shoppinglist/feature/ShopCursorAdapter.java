package com.example.mkruz.shoppinglist.feature;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ShopCursorAdapter extends CursorAdapter {
    public ShopCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.shop_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = view.findViewById(R.id.tvShopItem);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ShopDbAdapter.KEY_NAME));
        tvBody.setText(name);
        TextView tvSubtitle = view.findViewById(R.id.tvShopSubtitle);
        String subtitle = cursor.getString(cursor.getColumnIndexOrThrow(ShopDbAdapter.KEY_SUBTITLE));
        tvSubtitle.setText(subtitle);
        TextView tvLat = view.findViewById(R.id.tvShopLat);
        String geo = "Lat: " +
                String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(ShopDbAdapter.KEY_LAT))) +
                "  Lng: " +
                String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(ShopDbAdapter.KEY_LNG)));
        tvLat.setText(geo);
    }
}
