package com.example.pascal.apitest;

/**
 * Created by jeroen on 6-2-2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Process extends AsyncTask<String, Void, Void> {
    private List<String> users;
    private String limit;
    private String user;
    private String period;
    private RemoteFetch fetch;
    private Context context;
    private ProgressDialog progressDialog;
    private String origin;
    private String city;
    private String destination;
    private String mode;
    private boolean etamodus, getsongs;
    private String eta = null;
    private String weatherFar, weatherDetailed = null;
    private ArrayList<String> songnames;
    private ArrayList<String> tracknames;
    private ArrayList<String> artistnames;

    public Process(RemoteFetch drivesCallback, Context context, String origin, String destination, String mode){
        this.fetch = drivesCallback;
        this.context = context;
        this.destination = destination;
        this.mode = mode;
        this.origin = origin;
        etamodus = true;
        getsongs = false;

    }

    public Process(RemoteFetch drivesCallback, Context context, String city){
        this.fetch = drivesCallback;
        this.context = context;
        this.city = city;
        etamodus = false;
        getsongs = false;
    }

    public Process(Context context, RemoteFetch drivesCallback, String user, String period, String limit){
        this.fetch = drivesCallback;
        this.context = context;
        this.user = user;
        this.period = period;
        this.limit = limit;
        getsongs = true;
    }
    public Process(Context context, RemoteFetch drivesCallback, List<String> users, String period, String limit){
        this.fetch = drivesCallback;
        this.context = context;
        this.users = users;
        this.period = period;
        this.limit = limit;
        getsongs = true;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(context.getResources().getString(R.string.process_eta_title));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            if(etamodus && !getsongs){
                eta = fetch.updateEtaData(context, origin, destination, mode);
            }
            else if(!etamodus && !getsongs){
                weatherFar = fetch.updateWeatherFar(context, city);
                weatherDetailed = fetch.updateWeatherDetailed(context, "52", "3");
            }
            else{
                songnames = fetch.updateLastFm(context, users, period, limit);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final Exception e) {
            // Handle or log or ignore
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } finally {
            progressDialog = null;
            if(etamodus && !getsongs){
                Intent intent = new Intent(Constants.ACTION_DONE_FETCHING_ETA);
                intent.putExtra(Constants.EXTRA_ETA, eta);
                context.sendBroadcast(intent);
            }
            else if (!etamodus && !getsongs){
                Intent intent = new Intent(Constants.ACTION_DONE_FETCHING_WEATHER);
                intent.putExtra(Constants.EXTRA_WEATHER, weatherFar);
                context.sendBroadcast(intent);
            }
            else{
                Intent intent = new Intent(Constants.ACTION_DONE_FETCHING_LASTFM);
                intent.putExtra(Constants.EXTRA_LASTFM, songnames);
                intent.putExtra(Constants.EXTRA_TRACKS, tracknames);
                intent.putExtra(Constants.EXTRA_ARTIST, artistnames);
                context.sendBroadcast(intent);
            }

        }
    }



}
