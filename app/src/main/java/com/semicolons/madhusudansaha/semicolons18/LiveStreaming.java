package com.semicolons.madhusudansaha.semicolons18;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by madhusudan_saha on 23-02-2018.
 */

public class LiveStreaming extends AppCompatActivity {

    // Declare variables
    ProgressDialog pDialog;
    ImageView imageView;

    String URL = "http://192.168.43.185:3004/get_image_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_streaming);
        imageView = (ImageView) findViewById(R.id.imageView);
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(this);
        // Set progressbar title
        pDialog.setTitle("Live Streaming");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        // Show progressbar
        pDialog.show();

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                pDialog.dismiss();
                new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(URL);
            }
        }, 2, 1, TimeUnit.SECONDS);
    }
}
