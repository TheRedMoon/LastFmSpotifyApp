package com.example.pascal.apitest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pascal.apitest.R;

public class PrefAct extends AppCompatActivity {
    private Button manageplaylists, manageusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
//        playlist =  findViewById(R.id.txt_playlist);
        manageplaylists = findViewById(R.id.manage_playlists);
        manageusers = findViewById(R.id.manage_users);
        manageplaylists.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(PrefAct.this, PlaylistAct.class);
                startActivity(i);
            }
        });

        manageusers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(PrefAct.this, UserlistAct.class);
                startActivity(i);
            }
        });

    }


}
