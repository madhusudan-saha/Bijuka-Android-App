package com.semicolons.madhusudansaha.semicolons18;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by Madhusudan Saha on 12-Feb-18.
 */

public class MainActivity extends AppCompatActivity {

    Button subscribeButton;
    CheckedTextView subscribedCheckedTextView;
    AppDatabase db;

    HashMap<String,String> mp=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp.put("Hi","बिजूका");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent testIntent = new Intent(getApplicationContext(),HealthMonitor.class);
                    startActivity(testIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);

        db = ((MyApplication) this.getApplication()).getDb();

        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribedCheckedTextView = (CheckedTextView) findViewById(R.id.subscribedCheckedTextView);
        DataStore ds = db.dataStoreDao().findByName("subscribed");
        String subscribed;

        if(ds != null) {
            subscribed = ds.getValue();
            if(subscribed.equalsIgnoreCase("true")) {
                subscribeButton.setText(getResources().getString(R.string.unsubscribe));
            }
            else {
                subscribeButton.setText(getResources().getString(R.string.subscribe));
            }
            subscribedCheckedTextView.setChecked(Boolean.parseBoolean(subscribed));
        }
        else {
            subscribed = "false";
            ds = new DataStore("subscribed", subscribed);
            db.dataStoreDao().insert(ds);
        }

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    subscribeUnsubscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        subscribeButton.setClickable(true);

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this project the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("deviceID") != null) {
                Intent intent = new Intent(this, NotificationActivity.class);

                for (String key : getIntent().getExtras().keySet()) {
                    String value = getIntent().getExtras().getString(key);
                    intent.putExtra(key, value);
                }

                final int result = 1;
                startActivityForResult(intent, result);
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Result", "NotificationActivityClosed: " + data.toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void subscribeUnsubscribe() {
        DataStore ds = null;
        String subUnsub = (String) subscribeButton.getText();

        if(subUnsub.equalsIgnoreCase(getResources().getString(R.string.unsubscribe))) {
            MyFirebaseInstanceIDService.unsubscribeFromPushService(getApplicationContext());
            ds = new DataStore("subscribed", "false");
            subscribeButton.setText(getResources().getString(R.string.subscribe)+mp.get("Hi"));
            subscribedCheckedTextView.setChecked(false);
        }
        else {
            MyFirebaseInstanceIDService.subscribeToPushService(getApplicationContext());
            ds = new DataStore("subscribed", "true");
            subscribeButton.setText(getResources().getString(R.string.unsubscribe));
            subscribedCheckedTextView.setChecked(true);
        }
        db.dataStoreDao().update(ds);
    }
}
