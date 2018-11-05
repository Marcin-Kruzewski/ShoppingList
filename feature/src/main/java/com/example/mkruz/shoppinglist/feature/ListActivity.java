package com.example.mkruz.shoppinglist.feature;

import android.Manifest;
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

public class ListActivity extends AppCompatActivity {

    EditText item, qty, price;
    String itemValue = "";
    String qtyValue = "";
    String priceValue = "";
    TodoDbAdapter db;
    private final int MEMORY_ACCESS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){}
        else{
            ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MEMORY_ACCESS);
        }
        try {
            db = new TodoDbAdapter(getApplicationContext());
            db.open();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = (EditText) findViewById(R.id.item);
        item.setText(itemValue);
        qty = (EditText) findViewById(R.id.qty);
        qty.setText(qtyValue);
        price = (EditText) findViewById(R.id.price);
        price.setText(priceValue);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);

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
        itemValue = item.getText().toString();
        qtyValue = qty.getText().toString();
        priceValue = price.getText().toString();
        super.onPause();
    }

    public void onSaveBtn(View view) {
        try {
            long id;
            itemValue = item.getText().toString();
            qtyValue = qty.getText().toString();
            priceValue = price.getText().toString();
            if (!itemValue.equals("")) {
                if (qtyValue.equals("")) {
                    qtyValue = "1";
                }
                if (priceValue.equals("")) {
                    priceValue = "";
                }
                id = db.insertTodo(itemValue);
                TodoTask fromdb = db.getTodo(id);
                db.close();
                Toast.makeText(getApplicationContext(), fromdb.getDescription()+" added!", Toast.LENGTH_LONG).show();
                finish();

            }else{
                Snackbar.make(view, "Invalid data!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
