package com.example.pascal.apitest.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.pascal.apitest.R;
import com.example.pascal.apitest.model.Playlist;

import java.util.ArrayList;

/**
 * Created by jeroen on 18-5-2018.
 */

public class SpinAdapter extends ArrayAdapter<Playlist> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<Playlist> playlists;

    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Playlist> playlists) {
        super(context, textViewResourceId, playlists);
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public int getCount(){
        return playlists.size();
    }

    @Override
    public Playlist getItem(int position){
        return playlists.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(playlists.get(position).getName());
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(playlists.get(position).getName());

        return label;
    }
}
