package com.semicolons.madhusudansaha.semicolons18;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Madhusudan Saha on 12-Feb-18.
 */

public class ImageRetrieve {

    protected static Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;

        try {
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    protected static Bitmap base64ToImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedImage;
    }

    protected static String saveToInternalStorage(Bitmap bitmapImage, String filename, Context applicationContext){
        ContextWrapper cw = new ContextWrapper(applicationContext);
        // path to /data/data/yourapp/app_data/images
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File path=new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("PATH", "Image path: " + path.getAbsolutePath());

        return path.getAbsolutePath();
    }
}
