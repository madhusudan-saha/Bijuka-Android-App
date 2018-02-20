package com.semicolons.madhusudansaha.semicolons18;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        /*
        if(refreshedToken != null) {
            Log.d(getResources().getString(R.string.app_name), "Refreshing token");
            SaveToken.saveTokenToFile(refreshedToken, getApplicationContext());
        }
        */


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(m_androidId, refreshedToken);
    }
    // [END refresh_token]

    protected static boolean subscribeToPushService(Context applicationContext) {
        FirebaseMessaging.getInstance().subscribeToTopic(applicationContext.getResources().getString(R.string.topic));
        Log.d(applicationContext.getResources().getString(R.string.app_name), "Subscribed");
        Toast.makeText(applicationContext, "Subscribed", Toast.LENGTH_SHORT).show();
        //String token = FirebaseInstanceId.getInstance().getToken();

        /*
        if(token != null) {
            SaveToken.saveTokenToFile(token, applicationContext);
        }
        */

        return true;
    }

    protected static boolean unsubscribeFromPushService(Context applicationContext) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(applicationContext.getResources().getString(R.string.topic));
        Log.d(applicationContext.getResources().getString(R.string.app_name), "Unsubscribed");
        Toast.makeText(applicationContext, "Unsubscribed", Toast.LENGTH_SHORT).show();
        //String token = FirebaseInstanceId.getInstance().getToken();

        /*
        if(token != null) {
            SaveToken.saveTokenToFile(token, applicationContext);
        }
        */

        return false;
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */

    /*
    private void sendRegistrationToServer(String androidId, String token) {
        // TODO: Implement this method to send token to your app server.
    }
    */
}