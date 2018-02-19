package com.semicolons.madhusudansaha.semicolons18;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
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

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Madhusudan Saha on 12-Feb-18.
 */

public class MainActivity extends AppCompatActivity {

    boolean subscribed;
    Button subscribeButton;
    CheckedTextView subscribedCheckedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        */

        ImageView backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.INVISIBLE);

        File directory = getApplicationContext().getDir("dir", Context.MODE_PRIVATE);
        File file=new File(directory, "subscribed");
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        if(file.exists()) {
            subscribed = true;
            subscribedCheckedTextView = (CheckedTextView) findViewById(R.id.subscribedCheckedTextView);
            subscribedCheckedTextView.setChecked(subscribed);
            subscribeButton.setVisibility(View.GONE);
        }
        else {
            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        subscribe();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = null;
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        if(directory != null) {
            String[] children = directory.list();
            for (int i = 0; i < children.length; i++) {
                new File(directory, children[i]).delete();
            }
        }
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

    private void subscribe() {
        subscribed = MyFirebaseInstanceIDService.subscribeToPushService(getApplicationContext());
        subscribedCheckedTextView = (CheckedTextView) findViewById(R.id.subscribedCheckedTextView);
        subscribedCheckedTextView.setChecked(subscribed);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setVisibility(View.GONE);

        FileOutputStream stream = null;
        try{
            File directory = getApplicationContext().getDir("dir", Context.MODE_PRIVATE);
            File file=new File(directory, "subscribed");
            stream = new FileOutputStream(file);
            stream.write("Subscribed".getBytes());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(stream != null) {
                try{
                    stream.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            subscribed = MyFirebaseInstanceIDService.subscribeToPushService(getApplicationContext());
        }
        else {
            Toast.makeText(MainActivity.this, "Storage Permission Required!", Toast.LENGTH_LONG).show();
        }
    }
    */
}
