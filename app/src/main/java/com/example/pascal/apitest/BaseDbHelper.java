package com.example.pascal.apitest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDbHelper extends SQLiteOpenHelper {
    //single instance
    private static BaseDbHelper sInstance = null;

    private static final String DATABASE_NAME = "spotifysync.db";
    private static final int DATABASE_VERSION = 17;

    private static final String CREATE_USERS = ""
            + "CREATE TABLE users ("
            + "ID integer primary key autoincrement,"
            + "user_name VARCHAR(20),"
            + "real_name VARCHAR(30),"
            + "active INTEGER"
            + ");";

    private static final String CREATE_PLAYLIST = ""
            + "CREATE TABLE  playlists ("
            + "ID 				INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "playlist_id 	    VARCHAR(25),"
            + "playlist_name	VARCHAR(20),"
            + "playlist_desc	VARCHAR(100)"
            + ");";

   private BaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static BaseDbHelper getInstance(Context context) {
        //http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new BaseDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USERS);
        database.execSQL(CREATE_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w("DATABASEHELPER", "Upgrading database from version " + oldVersion + " to " + newVersion + "");
        if (oldVersion < 4) {
            database.execSQL("DROP TABLE IF EXISTS users;");
            database.execSQL(CREATE_USERS);
            database.execSQL("DROP TABLE IF EXISTS playlists;");
            database.execSQL(CREATE_PLAYLIST);
        }
    }
}