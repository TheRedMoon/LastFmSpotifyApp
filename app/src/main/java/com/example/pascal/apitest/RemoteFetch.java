package com.example.pascal.apitest;

/**
 * Created by jeroen on 6-2-2018.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class RemoteFetch {
    private static final String TAG = "Remotefetch" ;
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    private static final String GOOGLE_DIRECTIONS_API = "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&mode=%s";

    private static final String LASTFM_API = "http://ws.audioscrobbler.com/2.0/?method=user.gettoptracks&user=%s&period=%s&limit=%s&api_key=%s&format=json";

    //https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
    public static JSONObject getOpenweathermapJson(Context context, String city){
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID="+ context.getResources().getString(R.string.open_weather_maps_app_id));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));
            Log.e(TAG, "URL: "+ url.toString());


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

    public static String getWeatherBuienradar(Context context, String lat, String lon){
        try {
            URL url = new URL("https://br-gpsgadget-new.azurewebsites.net/data/raintext?lat=" + lat + "&lon=" + lon);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));
            Log.e(TAG, "URL: "+ url.toString());


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                stringBuffer.append(tmp).append("\n");
            reader.close();

            String data = stringBuffer.toString();

            return data;
        }catch(Exception e){
            return null;
        }
    }



    //https://developers.google.com/maps/documentation/directions/intro#TravelModes
    public static JSONObject getNavigationJson(Context context, String origin, String destination, String mode){
        try {

            String urlstring = String.format(GOOGLE_DIRECTIONS_API, origin, destination, mode);
            Log.e(TAG, urlstring);

            URL url = new URL(urlstring);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
//            if(data.getInt("cod") != 200){
//                return null;
//            }

            return data;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static JSONObject getLastFmJson(Context context, String user, String period, String limit){
        try {

            String urlstring = String.format(LASTFM_API, user, period, limit, context.getString(R.string.lastfm_api_key));

            URL url = new URL(urlstring);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
//            if(data.getInt("cod") != 200){
//                return null;
//            }

            return data;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String updateWeatherFar(final Context context, final String city){
                final JSONObject json = RemoteFetch.getOpenweathermapJson(context, city);
                if(json == null){
                    Toast.makeText(context,
                            context.getString(R.string.place_not_found),
                            Toast.LENGTH_LONG).show();
                    return null;
                } else {
                    return renderWeatherFar(json);
        }
    }

    public String updateWeatherDetailed(final Context context, final String lat, final String lon){
        final String json = RemoteFetch.getWeatherBuienradar(context, lat, lon);
        if(json == null){
            Toast.makeText(context,
                    context.getString(R.string.place_not_found),
                    Toast.LENGTH_LONG).show();
            return null;
        } else {
            return renderWeatherDetail(json);
        }
    }

    public ArrayList<String> updateLastFm(final Context context, final List<String> users, final String period, final String limit){
        ArrayList<ArrayList<String>> full = new ArrayList<ArrayList<String>>();
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < users.size(); i++){
            String user = users.get(i);
            JSONObject json = RemoteFetch.getLastFmJson(context, user, period, limit);
            if(json == null){
                Toast.makeText(context,
                        context.getString(R.string.place_not_found),
                        Toast.LENGTH_LONG).show();
            }
            else {
                full.add(renderLastFm(json, user));
//                result.addAll(renderLastFm(json));
            }
        }
        result = makeOne(full,limit);
        return result;
    }


    private ArrayList<String> makeOne(ArrayList<ArrayList<String>> full, String limit) {
        int lim = Integer.parseInt(limit);
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        for(int j = 0; j < lim; j++){
            for(int i = 0; i < full.size(); i++){
                temp = full.get(i);
                result.add(temp.get(j));
            }
        }
        return result;
    }

    public String updateEtaData(final Context context, final String origin, final String destination, final String mode) {
        final JSONObject json = RemoteFetch.getNavigationJson(context, origin, destination, mode);
        if (json == null) {
            Toast.makeText(context,
                    context.getString(R.string.place_not_found),
                    Toast.LENGTH_LONG).show();
            return null;

        } else {
            return renderEta(json);

        }
    }

    private String renderEta(JSONObject json){
        try {
            JSONArray stopArray = new JSONArray();
            if (json != null) {
                stopArray.put(json.getJSONObject("routes"));
            }
            JSONObject route = stopArray.getJSONObject(0); //take first element
            JSONObject leg = route.optJSONObject("legs");
            JSONArray legs;
            if (leg == null) {
                legs = route.optJSONArray("legs");
            } else {
                legs = new JSONArray().put(leg);
            }
            JSONObject act = legs.getJSONObject(0); //take first element

            JSONObject duration = act.optJSONObject("duration");
            String eta = duration.getString("text");
            return eta;

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
        return null;
    }

    private ArrayList<String> renderLastFm(JSONObject json, String user){
        ArrayList<String> result = new ArrayList<>();
        try {
            JSONObject toptracks = json.getJSONObject("toptracks");
            JSONArray stopArray = toptracks.getJSONArray("track");
            for (int i = 0; i < stopArray.length(); i++) {
                JSONObject stuff = new JSONObject(stopArray.get(i).toString());
                String res = stuff.optString("name");
                String number = stuff.optString("playcount");
                JSONObject artist = stuff.optJSONObject("artist");
                String art = artist.optString("name");
                String end = i+1 + "/" + number + ". " + user + ":" + art + "-" + res;
                result.add(i, end);
            }
            return result;
        }catch(Exception e){
            Log.e("LastFm", "One or more fields not found in the JSON data");
            Log.e("LastFm", "JSON:" + json.toString());
        }
        return null;
    }

    private String renderWeatherFar(JSONObject json){
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            String data = details.getString("main");
            return data;
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
        return null;
    }

    private String renderWeatherDetail(String json) {
        return "Empty";
    }
}
