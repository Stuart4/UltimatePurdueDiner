package com.spacejake.jake.ultimatepurduediner;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/*This file is part of UltimatePurdueDiner.

		UltimatePurdueDiner is free software: you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation, either version 3 of the License, or
		(at your option) any later version.

		UltimatePurdueDiner is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.

		You should have received a copy of the GNU General Public License
		along with UltimatePurdueDiner.  If not, see <http://www.gnu.org/licenses/>. */

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
        db.execSQL(String.format("INSERT OR REPLACE INTO preferences(id, pref) VALUES('%s', 1)", name.replaceAll("'", "''")));
    }

    public void dislike (String name) throws Exception{
        db.execSQL(String.format("INSERT OR REPLACE INTO preferences(id, pref) VALUES('%s', 0)", name.replaceAll("'", "''")));
    }

    public void noPref (String name) throws Exception{
        db.execSQL(String.format("DELETE FROM preferences WHERE id = '%s'", name.replaceAll("'", "''")));
    }

    public short getPref (String name) throws Exception{
        Cursor c = db.rawQuery(String.format("SELECT pref FROM preferences WHERE id = '%s'", name.replaceAll("'", "''")), null);
        c.moveToFirst();

        try {
            if (c.getInt(0) == 1) {
                return 1;
            } else {
                return -1;
            }
        } catch (CursorIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
