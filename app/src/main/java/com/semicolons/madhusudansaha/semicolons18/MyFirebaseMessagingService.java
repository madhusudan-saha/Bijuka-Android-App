package com.semicolons.madhusudansaha.semicolons18;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    AppDatabase db;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //message will contain the Push Message
        String deviceID = remoteMessage.getData().get("deviceID");
        String image = remoteMessage.getData().get("image");
        String type = remoteMessage.getData().get("type");
        String location = remoteMessage.getData().get("location");
        String timestamp = remoteMessage.getData().get("timestamp");
        String buzz = remoteMessage.getData().get("buzz");
        Long t = Long.parseLong(timestamp);
        String time = (new SimpleDateFormat("hh:mm:ss")).format(new Date(t));

        //To get a Bitmap image from the base64 encoded string received
        bitmap = ImageRetrieve.base64ToImage(image);
        String filename = "image" + timestamp + ".jpg";
        String imagePath = ImageRetrieve.saveToInternalStorage(bitmap, filename, getApplicationContext());

        getNotification(deviceID, type, location, time, buzz, imagePath);
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void getNotification(String deviceID, String type, String location, String time, String buzz, String imagePath) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("deviceID", deviceID);
        intent.putExtra("type", type);
        intent.putExtra("location", location);
        intent.putExtra("time", time);
        intent.putExtra("buzz", buzz);
        intent.putExtra("imagePath", imagePath);

        Log.d("deviceID", deviceID);
        Log.d("type", type);
        Log.d("location", location);
        Log.d("time", time);
        Log.d("buzz", buzz);

        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap image = ImageRetrieve.loadImageFromStorage(imagePath);

        Uri sound;
        boolean buzzTime = false;

        if(buzz.equalsIgnoreCase("true")) {
            db = ((MyApplication) this.getApplication()).getDb();

            DataStore ds = null;
            ds = db.dataStoreDao().findByName("buzz_time");
            if (ds != null) {
                long diff = System.currentTimeMillis() - (Long.parseLong(ds.getValue()));
                long seconds = diff / 1000;
                if (seconds >= 20) {
                    buzzTime = true;
                }
            } else {
                buzzTime = true;
            }
        }

        if(buzzTime) {
            sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.buzzer);
        }
        else {
            sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)/*Notification icon image*/
                .setSmallIcon(R.drawable.icon_report_problem)
                .setContentTitle(deviceID+":"+type)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE) /* ID of notification */,
                notificationBuilder.build());
    }

}
