package com.semicolons.madhusudansaha.semicolons18;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class HealthMonitor extends AppCompatActivity {

    Button live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));

        live = (Button) findViewById(R.id.live);

        live.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(HealthMonitor.this, LiveStreaming.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                getResources().getString(R.string.app_name), BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),
                getResources().getColor(R.color.background));
        this.setTaskDescription(taskDescription);
    }
}
