package com.semicolons.madhusudansaha.semicolons18;

import android.content.Context;

/**
 * Created by madhusudan_saha on 24-02-2018.
 */

public class Language {

    static AppDatabase db;

    public static void chooseLanguage(String lang, Context context) {
        db = ((MyApplication) context.getApplicationContext()).getDb();
        String language = lang;
        DataStore ds = db.dataStoreDao().findByName("language");
        if(!ds.getKey().equalsIgnoreCase(language)) {
            ds.setValue(language);
            db.dataStoreDao().update(ds);
        }
    }
}
