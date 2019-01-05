package com.example.mkruz.shoppinglist.feature;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ShopListActivity extends AppCompatActivity {

    ShopDbAdapter db;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("ShopList", "0");
        super.onCreate(savedInstanceState);
        Log.i("ShopList", "01");
        setContentView(R.layout.activity_shop_list);

        Log.i("ShopList", "1");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String language = prefs.getString("example_text", "");
        setTitle(language);

        Log.i("ShopList", "2");

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            if (prefs.getString("dark_theme", "").equals("2")){
                Toast.makeText(getApplicationContext(), "Dark theme", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ShopList", "3");
        try {
            db = new ShopDbAdapter(getApplicationContext());
            Log.i("ShopList", "31");
            db.open();
            Log.i("ShopList", "32");
            lvItems = findViewById(R.id.listView2);
            Log.i("ShopList", "33");
            ShopCursorAdapter shopAdapter = new ShopCursorAdapter(this, db.getAllShops());
            Log.i("ShopList", "34");
            lvItems.setAdapter(shopAdapter);
            Log.i("ShopList", "35");
            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(ShopListActivity.this, EditShopActivity.class);
                    intent1.putExtra("id", id);
                    startActivity(intent1);
                }
            });
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        Log.i("ShopList", "4");
    }

    public void addShop(View view) {
        Intent intent1 = new Intent(this, EditShopActivity.class);
        startActivity(intent1);
    }
}
