package com.example.mkruz.shoppinglist.feature;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 100; // in Milliseconds

    TodoDbAdapter db;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String language = prefs.getString("example_text", "");
        setTitle(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            if (prefs.getString("dark_theme", "").equals("2")) {
                Toast.makeText(getApplicationContext(), "Dark theme", Toast.LENGTH_LONG).show();
            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("PerPerPermissingsions","lalla");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 6);
        }
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, new MyLocationListener());

        ShopDbAdapter db = new ShopDbAdapter(getApplicationContext());
        db.open();
        Cursor shopsCursor = db.getAllShops();
        if (shopsCursor.moveToFirst()){
            do{
                String shopName = shopsCursor.getString(ShopDbAdapter.NAME_COLUMN);
                float lat = shopsCursor.getFloat(ShopDbAdapter.LAT_COLUMN);
                float lng = shopsCursor.getFloat(ShopDbAdapter.LNG_COLUMN);
                long range = shopsCursor.getLong(ShopDbAdapter.RANGE_COLUMN);
                Location shopGeo = new Location(shopName);
                shopGeo.setLatitude(lat);
                shopGeo.setLongitude(lng);
                Intent broadcast = new Intent();
                broadcast.putExtra("Name", shopName);
                PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, broadcast, 0);
                locationManager.addProximityAlert(lat, lng, range, -1, proximityIntent);
            }while(shopsCursor.moveToNext());
        }
        shopsCursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MainActivity", "1");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i("ShopList", "???");
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent2 = new Intent(this, SettingsActivity.class);
            startActivity(intent2);
        }
        if (id == R.id.action_shop_list) {

            Log.d("ShopList", "ini");
            Intent intent3 = new Intent(this, ShopListActivity.class);
            startActivity(intent3);
        }
        if (id == R.id.action_map) {

            Log.d("Map", "ini");
            Intent intent4 = new Intent(this, MapActivity.class);
            startActivity(intent4);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            db = new TodoDbAdapter(getApplicationContext());
            db.open();
            lvItems = findViewById(R.id.listView1);
            TodoCursorAdapter todoAdapter = new TodoCursorAdapter(this, db.getAllTodos());
            lvItems.setAdapter(todoAdapter);
            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(MainActivity.this, ListActivity.class);
                    intent1.putExtra("id", id);
                    startActivity(intent1);
                }
            });
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void addList(View view) {
        Intent intent1 = new Intent(this, ListActivity.class);
        startActivity(intent1);
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Log.i("onLocationChanged","1");
            Log.i("onLocationChanged",location.toString());
            ShopDbAdapter db = new ShopDbAdapter(getApplicationContext());
            db.open();

            Log.i("onLocationChanged","2");
            Cursor shopsCursor = db.getAllShops();
            if (shopsCursor.moveToFirst()){
                do{
                    Log.i("onLocationChanged","3");
                    String shopName = shopsCursor.getString(ShopDbAdapter.NAME_COLUMN);
                    float lat = shopsCursor.getFloat(ShopDbAdapter.LAT_COLUMN);
                    float lng = shopsCursor.getFloat(ShopDbAdapter.LNG_COLUMN);
                    Location shopGeo = new Location(shopName);
                    Log.i("onLocationChanged","4");
                    shopGeo.setLatitude(lat);
                    shopGeo.setLongitude(lng);
                    float distance = location.distanceTo(shopGeo);
                    Log.i("onLocationChanged", String.valueOf(distance));
                    Toast.makeText(MainActivity.this, shopName + ": " + distance, Toast.LENGTH_LONG).show();
                }while(shopsCursor.moveToNext());
            }
            shopsCursor.close();

            Log.i("onLocationChanged","5");
        }
        public void onStatusChanged(String s, int i, Bundle b) {}
        public void onProviderDisabled(String s) {}
        public void onProviderEnabled(String s) {}
    }
}
