package com.example.pascal.apitest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pascal.apitest.activities.PlaylistAct;
import com.example.pascal.apitest.util.BaseApp;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jeroen on 8-3-2018.
 */
//https://programtalk.com/java-api-usage-examples/kaaes.spotify.webapi.android.models.Playlist/

public class MyTask extends AsyncTask<String, Integer, Pager<PlaylistSimple>> {
    private final String playlistname;
//    private final Context context;
    private ArrayList<String> artists;
    private ArrayList<String> notFoundSongs;
    private SpotifyService spotify;
    private SpotifyApi api;
    private ArrayList<String> songs;
    private List<String> users;

    public MyTask(ArrayList<String> tracks, ArrayList<String> artists, List<String> users , String playlistname) {
        this.playlistname = playlistname;
        this.songs = tracks;
//        this.context = context;
        this.artists = artists;
        this.users = users;
    }

    @Override
    protected Pager<PlaylistSimple> doInBackground(String... params) {
        String  playlistid = null;
        if((playlistname == null)) return null;
        api = new SpotifyApi();
        spotify = api.getService();
        notFoundSongs = new ArrayList<>();
        api.setAccessToken(BaseApp.token);
        Pager<PlaylistSimple> playlistPager = spotify.getMyPlaylists();
        if(playlistPager == null || playlistPager.items.size() == 0)return null;
        List<PlaylistSimple> playlists = playlistPager.items;
        for (PlaylistSimple p : playlists) {
            if(p.name.toLowerCase().equals(playlistname.toLowerCase())){
                Log.e("TEST", "playstid set " + p.name + "id: " + p.id);
                playlistid = p.id;
                break;
            }
        }
        String owner = spotify.getMe().id;
        if(playlistid == null || playlistid.equals("")){
            final Map<String, Object> optionsplaylist = new HashMap<String, Object>();
            optionsplaylist.put("name", playlistname);
            optionsplaylist.put("public", true);
            spotify.createPlaylist(owner, optionsplaylist); //create playlist when playlist not found
        }
        playlistPager = spotify.getMyPlaylists();
        if(playlistPager == null || playlistPager.items.size() == 0)return null;
        playlists = playlistPager.items;
        for (PlaylistSimple p : playlists) {
            if(p.name.toLowerCase().equals(playlistname.toLowerCase())){
                Log.e("TEST", "playstid set " + p.name + "id: " + p.id);
                playlistid = p.id;
                break;
            }
        }
        if(playlistid == null || playlistid.equals("")){
            return null;
        }
        Map<String, Object> options = new HashMap<String, Object>();
        final List<String> trackUris = getTracks();
        if(trackUris == null) return null;
        final int position = 0;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< trackUris.size(); i++){
            String s = trackUris.get(i);
            sb.append(s);
            if(i != trackUris.size()-1)
            sb.append(",");
        }

        options.put("uris", trackUris);

        final Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("position", String.valueOf(position));



        spotify.replaceTracksInPlaylist(owner, playlistid, sb.toString(), queryParameters);

//        spotify.addTracksToPlaylist(owner, playlistid, queryParameters, options);
//        spotify.addTracksToPlaylist(owner, playlistid, options);
//        Pager<PlaylistSimple> playlists = spotify.getPlaylists(spotify.getMe().id);
        return null;
    }

    private List<String> getTracks() {
        List <String> result = new ArrayList<String>();
        if(songs == null) return null;
        for(int i = 0; i<songs.size(); i++){
            boolean finished = false;
            Log.e("The song", songs.get(i));
            TracksPager tracks = null;
            if(artists.get(i).equals("박효신")){
//                artists.add(i, "Park Hyo Shin");
                artists.set(i, "Park Hyo Shin");
            }
            if(artists.get(i).contains("Bigbang")){
//                artists.add(i, "Park Hyo Shin");
                artists.set(i, "BIGBANG");
            }
            try{
            tracks = spotify.searchTracks("track:"+songs.get(i)+" artist:" + artists.get(i));
            }
            catch (Exception e){
                Log.e("Error", e.getMessage());
            }
            if(tracks == null){
            }
            else{
                List<Track> artist = tracks.tracks.items;
                int counter = 7;
                if(artist.size() < 5){
                    counter = artist.size();
                }
                for(int j = 0; j<artist.size(); j++){
                    if(!finished){
                        Log.e("Kots23", artist.get(j).artists.get(0).name + " other " + artists.get(i));
                        if(artist.get(j).artists.get(0).name.toLowerCase().contains(artists.get(i).toLowerCase())){
                            Log.e("Kots2", artist.get(j).uri);
                            result.add(artist.get(j).uri);
                            finished = true;
                        }
                    }
                }
                if(!finished){
                    notFoundSongs.add(songs.get(i));
                }
            }
        }
//        String s = result.get(result.size()-1);
//        result.remove(result.size()-1);
//        String string = s.replace(",", "");
//        result.add(string);
//        Intent i = new Intent();
//        context.sendBroadcast(i);
        return result;
    }

    @Override
    protected void onPostExecute(Pager<PlaylistSimple> playlistPager) {
        //do something with the playlists
    }
}
