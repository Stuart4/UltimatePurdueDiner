package com.spacejake.jake.ultimatepurduediner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.sql.SQLException;

/**
 * Created by jake on 2/7/15.
 */
public class PreferenceDataSource {
    private SQLiteDatabase db;
    private SQLPreferences sqlPreferences;

    public PreferenceDataSource(Context context) throws Exception{
        sqlPreferences = new SQLPreferences(context);
    }

    public void open() throws SQLException {
        db = sqlPreferences.getWritableDatabase();
    }

    public void close() {
        sqlPreferences.close();
    }

    public void like (String name) throws Exception{
        db.execSQL(String.format("INSERT OR REPLACE INTO preferences(id, pref) VALUES('%s', 1)", name));
    }

    public void dislike (String name) throws Exception{
        db.execSQL(String.format("INSERT OR REPLACE INTO preferences(id, pref) VALUES('%s', 0)", name));
    }

    public void noPref (String name) throws Exception{
        db.execSQL(String.format("DELETE FROM preferences WHERE id = '%s'", name));
    }

    public boolean getPref (String name) throws Exception{
//        Cursor c = db.query("preferences", new String[]{"pref"}, "id = " + name, null, null, null, null);
        Cursor c = db.rawQuery(String.format("SELECT pref FROM preferences WHERE id = '%s'", name), null);
        c.moveToFirst();

        return c.getInt(0) == 1;
    }
}
