package ru.intelinvest.mysafes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Stanislav on 05.01.2015.
 */
public class DbConnector extends SQLiteOpenHelper {
    private static final String DB_NAME = "myDB";
    private static final int DB_VERSION = 1;
    final String myLogs = "myLogs";


    public DbConnector(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE crypto ( " +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "str BLOB (5000) NOT NULL);";
        Log.d("myLogs", query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
