package com.example.pascal.apitest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pascal.apitest.R;
import com.example.pascal.apitest.model.Playlist;
import com.example.pascal.apitest.util.BaseApp;
import com.example.pascal.apitest.util.PrefUtils;
import com.example.pascal.apitest.util.SpinAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAct extends AppCompatActivity {
    private EditText playlistid, playlistname;
    private Button setactivepl, delplaylist, addplaylist;
    private List<String> users;
    private SpinAdapter adapter;
    private ArrayList<Playlist> playlistvalues;
    private Playlist chosenactivePl;
    private Spinner plSpinner;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
//        playlistid = findViewById(R.id.playlist_id);
        playlistname = findViewById(R.id.playlist_name);
        setactivepl = findViewById(R.id.set_activeplaylist);
        delplaylist = findViewById(R.id.delete_playlist);
        addplaylist = findViewById(R.id.add_playlist);
        clear = findViewById(R.id.clear_playlists);
//        playlist =  findViewById(R.id.txt_playlist);
        plSpinner = (Spinner) findViewById(R.id.playlist_spinner);
        getSupportActionBar().setTitle("Playlists");
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BaseApp.playlistDAO.deleteAll();
                playlistvalues = new ArrayList<>();
                adapter = new SpinAdapter(PlaylistAct.this,
                        android.R.layout.simple_spinner_item, playlistvalues
                );
                plSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
//                PrefUtils.setStringPreference(PlaylistAct.this, "playlistname", "");
            }
        });
        addplaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String plid = playlistid.getText().toString();
                String plname = playlistname.getText().toString();
                addPlaylist(plname);
            }
        });
        delplaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String plid = playlistid.getText().toString();
                String plname = playlistname.getText().toString();
                deletePlaylist(plname);
            }
        });
        setactivepl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenactivePl != null){
//                    PrefUtils.setStringPreference(PlaylistAct.this, "playlistid", chosenactivePl.getId());
                    PrefUtils.setStringPreference(PlaylistAct.this, "playlistname", chosenactivePl.getName());
                    finish();
                }
                else{
                    Toast.makeText(PlaylistAct.this,"Choose a playlist in the spinner above",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        playlistvalues = getPlaylistValues();

        adapter = new SpinAdapter(this,
                android.R.layout.simple_spinner_item, playlistvalues
                );
        plSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        plSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Playlist p = adapter.getItem(position);
                chosenactivePl = p;
                // Here you can do the action you want to...

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    private void deletePlaylist(String plname) {
        if(plname != null){
            BaseApp.playlistDAO.deleteByName(plname);
            playlistvalues = getPlaylistValues();
            adapter = new SpinAdapter(PlaylistAct.this,
                    android.R.layout.simple_spinner_item, playlistvalues
            );
            plSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
//                PrefUtils.setStringPreference(PlaylistAct.this, "playlistname", "");
        }
        else{
            Toast.makeText(this, "Enter valid playlist id or name", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPlaylist(String plname) {
        Playlist p = new Playlist();
        p.setName(plname);
//        p.setId(plid);
        BaseApp.playlistDAO.insert(p);
        playlistvalues = getPlaylistValues();

        adapter = new SpinAdapter(this,
                android.R.layout.simple_spinner_item, playlistvalues
        );
        plSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        plSpinner.setSelection(playlistvalues.size()-1);
    }

    private ArrayList<Playlist> getPlaylistValues() {
        List<Playlist> playlistdatabase = BaseApp.playlistDAO.selectAll();
        ArrayList<Playlist> result = new ArrayList<Playlist>(playlistdatabase);
        return result;
    }
    private void delete() {
    }
}
