package com.semicolons.madhusudansaha.semicolons18;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Madhusudan Saha on 20-Feb-18.
 */

@Dao
public interface DataStoreDao {
    @Query("SELECT * FROM dataStore")
    List<DataStore> getAll();

    @Query("SELECT * FROM dataStore where key = :key LIMIT 1")
    DataStore findByName(String key);

    @Insert
    void insert(DataStore dataStore);

    @Delete
    void delete(DataStore dataStore);

    @Update
    void update(DataStore dataStore);
}
