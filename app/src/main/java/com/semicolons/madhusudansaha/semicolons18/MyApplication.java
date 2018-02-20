package com.semicolons.madhusudansaha.semicolons18;

import android.app.Application;
import android.arch.persistence.room.Room;

/**
 * Created by Madhusudan Saha on 20-Feb-18.
 */

public class MyApplication extends Application {

    private AppDatabase db;

    @Override
    public void onCreate() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "DB").allowMainThreadQueries().build();
    }

    public AppDatabase getDb() {
        return db;
    }
}
