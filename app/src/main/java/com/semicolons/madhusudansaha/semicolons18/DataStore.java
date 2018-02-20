package com.semicolons.madhusudansaha.semicolons18;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Madhusudan Saha on 20-Feb-18.
 */

@Entity
public class DataStore {
    @PrimaryKey
    @NonNull
    private String key;
    private String value;

    public DataStore(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
