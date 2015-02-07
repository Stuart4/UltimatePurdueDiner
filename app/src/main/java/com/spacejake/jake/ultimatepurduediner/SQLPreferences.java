package com.spacejake.jake.ultimatepurduediner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jake on 2/7/15.
 */
public class SQLPreferences extends SQLiteOpenHelper{

    public SQLPreferences(Context context) {
        super(context, "preferences", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE preferences (id TEXT primary key, pref int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE preferences");
        onCreate(db);
    }
}
