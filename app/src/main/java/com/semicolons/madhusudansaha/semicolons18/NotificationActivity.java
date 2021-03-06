package com.semicolons.madhusudansaha.semicolons18;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Madhusudan Saha on 12-Feb-18.
 */

public class NotificationActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView timeTextView;
    TextView deviceIDTextView;
    ImageView imageView;
    TextView typeTextView;
    String deviceID;
    String type;
    String location;
    String time;
    String buzz;
    String imagePath;
    AppDatabase db;
    TextView toolbarTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));

        Intent values = getIntent();

        deviceID = values.getExtras().getString("deviceID");
        type = values.getExtras().getString("type");
        location = values.getExtras().getString("location");
        time = values.getExtras().getString("time");
        buzz = values.getExtras().getString("buzz");
        imagePath = values.getExtras().getString("imagePath");

        Bitmap image = ImageRetrieve.loadImageFromStorage(imagePath);

        deviceIDTextView = (TextView) findViewById(R.id.deviceIDTextView);
        deviceIDTextView.setText(deviceID);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        typeTextView = (TextView) findViewById(R.id.typeTextView);
        typeTextView.setText(type);

        timeTextView = (TextView) findViewById(R.id.timeTextView);
        timeTextView.setZ(0.2f);
        timeTextView.setText(time+"");

        //Toast.makeText(this, "Alert!", Toast.LENGTH_SHORT).show();

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = ((MyApplication) this.getApplication()).getDb();
        DataStore ds = null;
        ds = db.dataStoreDao().findByName("buzz_time");
        if(ds != null) {
            db.dataStoreDao().update(new DataStore("buzz_time", Long.toString(System.currentTimeMillis())));
        }
        else {
            db.dataStoreDao().insert(new DataStore("buzz_time", Long.toString(System.currentTimeMillis())));
        }

        translate();

        setResult(RESULT_OK, values);
    }

    @Override
    protected void onStart() {
        super.onStart();

        translate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                getResources().getString(R.string.app_name), BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),
                getResources().getColor(R.color.background));
        this.setTaskDescription(taskDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_english) {
            try {
                Language.chooseLanguage(getResources().getString(R.string.english), this);
                translate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_hindi) {
            try {
                Language.chooseLanguage(getResources().getString(R.string.hindi), this);
                translate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_marathi) {
            try {
                Language.chooseLanguage(getResources().getString(R.string.marathi), this);
                translate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String[] latLong = location.split(",");
        Double latitude = Double.parseDouble(latLong[0].trim());
        Double longitude = Double.parseDouble(latLong[1].trim());
        LatLng marker = new LatLng(latitude, longitude);

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.addMarker(new MarkerOptions().position(marker)
                .title(deviceID));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        db.dataStoreDao().update(new DataStore("buzz_time", Long.toString(System.currentTimeMillis())));
        this.finish();
    }

    public void backButton(View view) {
        db.dataStoreDao().update(new DataStore("buzz_time", Long.toString(System.currentTimeMillis())));
        this.finish();
    }

    private void translate() {
        String language = db.dataStoreDao().findByName("language").getValue();

        toolbarTitle.setText(Constants.TRANSLATION.get(R.string.app_name).get(Constants.LANGUAGE.get(language)));
        switch (type) {
            case "leopard":
                typeTextView.setText(Constants.TRANSLATION.get(R.string.leopard).get(Constants.LANGUAGE.get(language)));
                break;
            case "deer":
                typeTextView.setText(Constants.TRANSLATION.get(R.string.deer).get(Constants.LANGUAGE.get(language)));
                break;
            case "elephant":
                typeTextView.setText(Constants.TRANSLATION.get(R.string.elephant).get(Constants.LANGUAGE.get(language)));
                break;
            case "monkey":
                typeTextView.setText(Constants.TRANSLATION.get(R.string.monkey).get(Constants.LANGUAGE.get(language)));
                break;
            case "bird":
                typeTextView.setText(Constants.TRANSLATION.get(R.string.bird).get(Constants.LANGUAGE.get(language)));
                break;
        }
    }
}
