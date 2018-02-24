package com.semicolons.madhusudansaha.semicolons18;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class HealthMonitor extends AppCompatActivity {

    Button live;
    Button flashButton;
    Button soundButton;
    TextView toolbarTitle;
    AppDatabase db;
    Toolbar toolbar;
    ImageView batteryIcon;
    ImageView soundIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);

        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));

        db = ((MyApplication) this.getApplication()).getDb();

        live = (Button) findViewById(R.id.live);
        flashButton = (Button) findViewById(R.id.flashButton);
        soundButton = (Button) findViewById(R.id.soundButton);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        batteryIcon = (ImageView) findViewById(R.id.batteryIcon);
        soundIcon = (ImageView) findViewById(R.id.soundIcon);

        live.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(HealthMonitor.this, LiveStreaming.class);
                startActivity(intent);
            }
        });

        flashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            batteryIcon.setImageDrawable(getResources().getDrawable(R.drawable.light));
                            batteryIcon.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{getResources().getColor(R.color.green)}));                       }
                    }, 500);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            batteryIcon.setImageDrawable(getResources().getDrawable(R.drawable.close_light));
                            batteryIcon.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{getResources().getColor(R.color.red)}));                       }
                    }, 3000);
             /*
                String url = "http://192.168.43.185:3003/lightdiag";
                String result;
                HttpPostRequest s = new HttpPostRequest();
                try {
                    result = s.execute(url).get();
                    Toast.makeText(getApplicationContext(), "Result: "+result, Toast.LENGTH_SHORT).show();
                    Log.d("Result: ", result);
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        soundIcon.setImageDrawable(getResources().getDrawable(R.drawable.sound_up));
                        soundIcon.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{getResources().getColor(R.color.green)}));                       }
                }, 500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        soundIcon.setImageDrawable(getResources().getDrawable(R.drawable.sound_down));
                        soundIcon.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{getResources().getColor(R.color.red)}));                       }
                }, 3000);
            }
        });

        translate();
    }

    @Override
    protected void onStart() {
        super.onStart();

        translate();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    public void backButton(View view) {
        this.finish();
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

    @Override
    protected void onResume() {
        super.onResume();

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                getResources().getString(R.string.app_name), BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),
                getResources().getColor(R.color.background));
        this.setTaskDescription(taskDescription);
    }

    private void translate() {
        String language = db.dataStoreDao().findByName("language").getValue();

        toolbarTitle.setText(Constants.TRANSLATION.get(R.string.app_name).get(Constants.LANGUAGE.get(language)));
        flashButton.setText(Constants.TRANSLATION.get(R.string.light_content).get(Constants.LANGUAGE.get(language)));
        soundButton.setText(Constants.TRANSLATION.get(R.string.sound_content).get(Constants.LANGUAGE.get(language)));
        live.setText(Constants.TRANSLATION.get(R.string.live).get(Constants.LANGUAGE.get(language)));

        flashButton.setTextSize(16f);
        soundButton.setTextSize(16f);
        live.setTextSize(16f);
    }
}
