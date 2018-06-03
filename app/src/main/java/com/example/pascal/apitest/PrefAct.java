package com.example.pascal.apitest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pascal.apitest.activities.PlaylistAct;
import com.example.pascal.apitest.activities.UserlistAct;

import java.util.List;

public class PrefAct extends AppCompatActivity {
    private EditText lastfmuser, fmdeleteuser;
    private Button deleteall, manageplaylists, manageusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        deleteall = findViewById(R.id.deleteall_btn);
//        playlist =  findViewById(R.id.txt_playlist);
        manageplaylists = findViewById(R.id.manage_playlists);
        manageusers = findViewById(R.id.manage_users);
        deleteall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });
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

    private void deleteUser() {
        String user = fmdeleteuser.getText().toString();
    }

    private void delete() {
    }
}