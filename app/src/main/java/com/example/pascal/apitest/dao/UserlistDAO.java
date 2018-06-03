package com.example.pascal.apitest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pascal.apitest.BaseDbHelper;
import com.example.pascal.apitest.model.Playlist;
import com.example.pascal.apitest.model.User;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeroen on 02/02/16.
 */
public class UserlistDAO {
    // Database fields
    protected SQLiteDatabase database;
    private BaseDbHelper dbHelper;

    //static vars about our table
    protected static final String TABLE_NAME = "users";

    public UserlistDAO(Context context) {
        dbHelper = BaseDbHelper.getInstance(context);
    }

    protected static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(User user){
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUsername());
        values.put("real_name", user.getRealname());
        // long insertId =
        database.insert(TABLE_NAME, null, values);
    }

    public void deleteByUserName(String name) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE user_name=?",
                new String[]{name});
    }

    public void deleteByRealName(String name) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE real_name=?",
                new String[]{name});
    }



    public List<User> selectByUserName(String name) {
        if(name == null) return null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_name=? ORDER BY " + "id" + " ASC",
                new String[]{name});
        return fillList(cursor);
    }


    public List<User> selectByRealName(String name) {
        if(name == null) return null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE real_name=? ORDER BY " + "id" + " ASC",
                new String[]{name});
        return fillList(cursor);
    }

    public List<User> selectAll() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return fillList(cursor);
    }

    protected List<User> fillList(Cursor cursor) {
        List<User> users = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    User user = fillCursor(cursor);
                    users.add(user);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("fillList() Exception", e.getMessage() + "");
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return users;
    }

    /**
     * Fills a single client
     *
     * @param cursor
     * @return
     */
    protected User fill(Cursor cursor) {
        User user = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                user = fillCursor(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return user;
    }

     /**
     * Cursor method
     *
     * @param c
     * @return
     */
    protected User fillCursor(Cursor c) {
        String username = c.getString(c.getColumnIndex("user_name"));
        String realname = c.getString(c.getColumnIndex("real_name"));

        User u = new User();
        u.setRealname(realname);
        u.setUsername(username);
        return u;
    }
}
