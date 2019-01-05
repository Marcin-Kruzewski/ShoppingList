package com.example.mkruz.shoppinglist.feature;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ShopDbAdapter db = new ShopDbAdapter(getApplicationContext());
        db.open();

        Cursor shopsCursor = db.getAllShops();
        Log.i("MaPAPAPa", "1");
        if (shopsCursor.moveToFirst()){
            do{
                Log.i("MaPAPAPa", "2");
                float lat = shopsCursor.getFloat(ShopDbAdapter.LAT_COLUMN);
                float lng = shopsCursor.getFloat(ShopDbAdapter.LNG_COLUMN);
                LatLng shopGeo = new LatLng(lat, lng);
                String shopName = shopsCursor.getString(ShopDbAdapter.NAME_COLUMN);
                Log.i("MaPAPAPa", shopName);
                Log.i("MaPAPAPa", String.valueOf(lat));
                Log.i("MaPAPAPa", String.valueOf(lng));
                mMap.addMarker(new MarkerOptions().position(shopGeo).title(shopName));
            }while(shopsCursor.moveToNext());
        }
        shopsCursor.close();

        Log.i("MaPAPAPa", "3");
        // Add a marker in Home and move the camera
        LatLng filo = new LatLng(53.03060, 18.65648);
        mMap.addMarker(new MarkerOptions().position(filo).title("Dom"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(filo));
    }
}
