package com.semicolons.madhusudansaha.semicolons18;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Madhusudan Saha on 20-Feb-18.
 */

@Database(entities = {DataStore.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataStoreDao dataStoreDao();
}
