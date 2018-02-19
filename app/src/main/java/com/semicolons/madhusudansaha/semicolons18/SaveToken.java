package com.semicolons.madhusudansaha.semicolons18;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Madhusudan Saha on 18-Feb-18.
 */

public class SaveToken {

    protected static void saveTokenToFile(String token, Context applicationContext) {
        String m_androidId = Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        String tokenFolder = "token";
        String tokenFile = "token.txt";
        Log.d(applicationContext.getResources().getString(R.string.app_name), m_androidId + " : " + token);

        String path = Environment.getExternalStorageDirectory() + File.separator  + applicationContext.getResources().getString(R.string.app_name) + File.separator + tokenFolder;
        File folder = new File(path);
        folder.mkdirs();
        File file = new File(folder, tokenFile);

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(token);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        Log.d("PATH", "Token path: " + file.getAbsolutePath());
    }
}
