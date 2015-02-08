package com.spacejake.jake.ultimatepurduediner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
