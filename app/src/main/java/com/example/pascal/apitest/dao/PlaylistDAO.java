package com.example.pascal.apitest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pascal.apitest.BaseDbHelper;
import com.example.pascal.apitest.model.Playlist;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeroen on 02/02/16.
 */
public class PlaylistDAO {
    // Database fields
    protected SQLiteDatabase database;
    private BaseDbHelper dbHelper;

    //static vars about our table
    protected static final String TABLE_NAME = "playlists";

    public PlaylistDAO(Context context) {
        dbHelper = BaseDbHelper.getInstance(context);
    }

    protected static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(Playlist playlist){
        ContentValues values = new ContentValues();
        values.put("playlist_id", playlist.getId());
        values.put("playlist_name", playlist.getName());
        values.put("playlist_desc", playlist.getDesc());
        // long insertId =
        database.insert(TABLE_NAME, null, values);
    }

    public void deleteById(String id) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE playlist_id=?",
                new String[]{id});
    }
    public void deleteByName(String name) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE playlist_name=?",
                new String[]{name});
    }
    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }


    /**
     * Selects the entities by its flag
     * @return
     */
    public List<Playlist> selectByName(String name) {
        if(name == null) return null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE playlist_name=? ORDER BY " + "id" + " ASC",
                new String[]{name});
        return fillList(cursor);
    }

    public List<Playlist> selectAll() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return fillList(cursor);
    }

    protected List<Playlist> fillList(Cursor cursor) {
        List<Playlist> playlists = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Playlist playlist = fillCursor(cursor);
                    playlists.add(playlist);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("fillList() Exception", e.getMessage() + "");
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return playlists;
    }

    /**
     * Fills a single client
     *
     * @param cursor
     * @return
     */
    protected Playlist fill(Cursor cursor) {
        Playlist playlist = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                playlist = fillCursor(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return playlist;
    }

     /**
     * Cursor method
     *
     * @param c
     * @return
     */
    protected Playlist fillCursor(Cursor c) {
        String id = c.getString(c.getColumnIndex("playlist_id"));
        String name = c.getString(c.getColumnIndex("playlist_name"));
        String desc = c.getString(c.getColumnIndex("playlist_desc"));

        Playlist p = new Playlist();
        p.setId(id);
        p.setName(name);
        p.setDesc(desc);

        return p;
    }
}
