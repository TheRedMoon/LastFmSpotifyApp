package com.example.pascal.apitest.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;

import com.example.pascal.apitest.BuildConfig;
import com.example.pascal.apitest.dao.PlaylistDAO;
import com.example.pascal.apitest.dao.UserlistDAO;
import com.facebook.stetho.Stetho;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeroen on 18-5-2018.
 */

public class BaseApp extends Application{
    public static PlaylistDAO playlistDAO;
    public static UserlistDAO userlistDAO;
    public static String token;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        openDaos();
        String temp = PrefUtils.getStringPreference(this, "token", "");
        if(!temp.equals("")){
            token = temp;
        }
    }

    private void openDaos() {
        playlistDAO = new PlaylistDAO(this);
        playlistDAO.open();
        userlistDAO = new UserlistDAO(this);
        userlistDAO.open();
    }


    public static void setToken(Context context, String newtoken) {
        PrefUtils.setStringPreference(context, "token", newtoken);
        token = newtoken;
    }
}
