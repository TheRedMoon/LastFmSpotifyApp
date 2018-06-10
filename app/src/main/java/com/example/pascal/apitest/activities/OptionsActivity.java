package com.example.pascal.apitest.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pascal.apitest.Constants;
import com.example.pascal.apitest.R;
import com.example.pascal.apitest.model.User;
import com.example.pascal.apitest.util.BaseApp;
import com.example.pascal.apitest.util.DataBaseAdaptar;
import com.example.pascal.apitest.util.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends AppCompatActivity {
    private Button confirm;
    private EditText editLimit;
    private ArrayList<User> userlistvalues;
    private ListView listView;
    private Spinner periodLastfm;
    private DataBaseAdaptar adapter;
    private ArrayList<String> showlist;
    private String period, limit;
    private CheckBox overwrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        confirm = findViewById(R.id.confirm);
        periodLastfm = findViewById(R.id.period_lastfm);
        editLimit = findViewById(R.id.limit_lastfm);
        overwrite = findViewById(R.id.overwrite_playlist);
//        playlist =  findViewById(R.id.txt_playlist);

        overwrite.setChecked(PrefUtils.getBooleanPreference(OptionsActivity.this, Constants.EXTRA_REPLACE, overwrite.isChecked()));

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PrefUtils.setStringPreference(OptionsActivity.this, Constants.EXTRA_PERIOD, period);
                if(editLimit != null && !editLimit.toString().equals("")) {
                    limit = editLimit.getText().toString();
                }
                PrefUtils.setStringPreference(OptionsActivity.this, Constants.EXTRA_LIMIT, limit);
                PrefUtils.setBooleanPreference(OptionsActivity.this, Constants.EXTRA_REPLACE, overwrite.isChecked());
                finish();

            }
        });

        editLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLimit.getText().clear();
            }
        });

        ArrayList<String> periods = new ArrayList<String>( );
        periods.add("overall");
        periods.add("7day");
        periods.add("1month");
        periods.add("3month");
        periods.add("6month");
        periods.add("12month");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, periods);

//set the default according to value

        periodLastfm.setAdapter(adapter);
        periodLastfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                period = periodLastfm.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setDefaultValue();




    }

    private void setDefaultValue() {
        String limitdefaultvalue = PrefUtils.getStringPreference(OptionsActivity.this, Constants.EXTRA_LIMIT, "25");
        String perioddefaultvaulue = PrefUtils.getStringPreference(OptionsActivity.this, Constants.EXTRA_PERIOD, "overall");
        ArrayAdapter myAdap = (ArrayAdapter) periodLastfm.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(perioddefaultvaulue);
        periodLastfm.setSelection(spinnerPosition);
        editLimit.setText(limitdefaultvalue);
    }

}
