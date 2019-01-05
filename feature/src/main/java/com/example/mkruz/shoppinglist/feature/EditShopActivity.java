package com.example.mkruz.shoppinglist.feature;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditShopActivity extends AppCompatActivity {

    long id;
    EditText name, subtitle, lat, lng, range;
    String nameValue = "";
    String subtitleValue = "";
    String latValue = "";
    String lngValue = "";
    String rangeValue = "";
    ShopDbAdapter db;
    Shop editedItem;
    private final int MEMORY_ACCESS = 5;
    private final long NON_EXISTING_ID = -1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("ShopList", "0");
        if(ActivityCompat.shouldShowRequestPermissionRationale(EditShopActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){}
        else{
            ActivityCompat.requestPermissions(EditShopActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MEMORY_ACCESS);
        }
        try {
            db = new ShopDbAdapter(getApplicationContext());
            db.open();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        Log.i("ShopList", "2");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);

        Log.i("ShopList", "3");
        name = (EditText) findViewById(R.id.name);
        subtitle = (EditText) findViewById(R.id.subtitle);
        lat = (EditText) findViewById(R.id.lat);
        lng = (EditText) findViewById(R.id.lng);
        range = (EditText) findViewById(R.id.range);
        id = getIntent().getLongExtra("id", NON_EXISTING_ID);
        Log.i("ShopList", "4");
        if (id != NON_EXISTING_ID && editedItem == null){
            editedItem = db.getShop(id);
            //Toast.makeText(getApplicationContext(), editedItem.toString(), Toast.LENGTH_LONG).show();
            name.setText(editedItem.getName());
            subtitle.setText(String.valueOf(editedItem.getSubtitle()));
            lat.setText(String.valueOf(editedItem.getLat()));
            Log.i("ShopList", String.valueOf(editedItem.getLat()));
            lng.setText(String.valueOf(editedItem.getLng()));
            range.setText(String.valueOf(editedItem.getRange()));
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton8);
            fab.setVisibility(View.VISIBLE);
            Log.i("ShopList", "5");
        }else{
            name.setText(nameValue);
            subtitle.setText(subtitleValue);
            lat.setText(latValue);
            lng.setText(lngValue);
            range.setText(rangeValue);
        }

        Log.i("ShopList", "6");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case(MEMORY_ACCESS):
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{

                    Toast.makeText(getApplicationContext(), "Memory write permission missing!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onPause() {
        nameValue = name.getText().toString();
        subtitleValue = subtitle.getText().toString();
        latValue = lat.getText().toString();
        lngValue = lng.getText().toString();
        rangeValue = range.getText().toString();
        super.onPause();
    }

    public void onSaveBtn(View view) {
        nameValue = name.getText().toString();
        subtitleValue = subtitle.getText().toString();
        latValue = lat.getText().toString();
        lngValue = lng.getText().toString();
        Log.i("Lat1", latValue);
        rangeValue = range.getText().toString();
        if (!nameValue.equals("")) {
            if (subtitleValue.equals("")) {
                subtitleValue = "1";
            }
            if (latValue.equals("")) {
                latValue = "0";
            }
            if (lngValue.equals("")) {
                lngValue = "0";
            }
            if (rangeValue.equals("")) {
                rangeValue = "0";
            }
            float latFloat = Float.parseFloat(latValue);
            Log.i("Lat2", String.valueOf(latFloat));
            float lngFloat = Float.parseFloat(lngValue);
            long rangeFloat = (long) Float.parseFloat(rangeValue);
            if (id == NON_EXISTING_ID){
                id = db.insertShop(nameValue, subtitleValue, latFloat, lngFloat, rangeFloat, false);
                finish();
            }else{
                db.updateShop(id, nameValue, subtitleValue, latFloat, lngFloat, rangeFloat, false);
                finish();
            }
        }else{
            Snackbar.make(view, "Invalid data!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void onDeleteButton(View view){
        db.deleteShop(id);
        finish();
    }
}