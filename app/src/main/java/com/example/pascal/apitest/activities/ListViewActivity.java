package com.example.pascal.apitest.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pascal.apitest.R;

import java.util.ArrayList;

/**
 * Created by jeroen on 23-2-2018.
 */

public class ListViewActivity extends ListActivity {
    private ArrayList<String> songs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songs = getIntent().getStringArrayListExtra(MainActivity.ARRAYLIST);

        // storing string resources into Array
        // here you store the array of string you got from the database

        // Binding Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item2, R.id.text1, songs));
        // refer the ArrayAdapter Document in developer.android.com
        ListView lv = getListView();
        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                // Launching new Activity on selecting single List Item
                }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
