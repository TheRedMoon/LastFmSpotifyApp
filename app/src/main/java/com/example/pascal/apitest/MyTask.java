package com.example.pascal.apitest;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Jeroen on 8-3-2018.
 */
//https://programtalk.com/java-api-usage-examples/kaaes.spotify.webapi.android.models.Playlist/

public class MyTask extends AsyncTask<String, Integer, Pager<PlaylistSimple>> {
    private ArrayList<String> artists;
    private SpotifyService spotify;
    private String token, playlistid;
    private SpotifyApi api;
    private ArrayList<String> songs;
    private List<String> users;

    public MyTask(String token, ArrayList<String> tracks, ArrayList<String> artists, List<String> users , String playlistid) {
        this.playlistid = playlistid;
        this.token = token;
        this.songs = tracks;
        this.artists = artists;
        this.users = users;
    }

    private boolean checkSolo(){
        if(users.contains("SaintSiant")){
            if(users.size() == 1){
                return true;
            }
        }
        return false;
    }

    @Override
    protected Pager<PlaylistSimple> doInBackground(String... params) {
        if(playlistid == null){
            return null;
        }
        api = new SpotifyApi();
        spotify = api.getService();
        api.setAccessToken(token);
        Map<String, Object> options = new HashMap<String, Object>();
//        options.put("name", "testplaylist");
//        options.put("public", true);
        String owner = spotify.getMe().id;
//        spotify.createPlaylist(owner, options);
//        options = new HashMap<String, Object>();
        final List<String> trackUris = getTracks();
        if(trackUris == null) return null;

        final int position = 0;
//        String trackUri1 = "spotify:track:6BSNHSXrOVNnRcm85D4YIt";
//        String trackUri2=  "spotify:track:4z88dsPMPlCPakuhBYkcuP";
//        final List<String> trackUris = Arrays.asList(trackUri1, trackUri2);

//        List<List<String>> chunks = new ArrayList<List<String>>();
//        if(trackUris.size() > 50){
//            chunks = Lists.partition(trackUris, 50);
//        }

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
        Log.e("Gorekots", sb.toString());
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
                        if(artist.get(j).artists.get(0).name.contains(artists.get(i))){
                            Log.e("Kots2", artist.get(j).uri);
                            result.add(artist.get(j).uri);
                            finished = true;
                        }
                    }
                }
            }
        }
//        String s = result.get(result.size()-1);
//        result.remove(result.size()-1);
//        String string = s.replace(",", "");
//        result.add(string);
        return result;
    }

    @Override
    protected void onPostExecute(Pager<PlaylistSimple> playlistPager) {
        //do something with the playlists
    }
}
