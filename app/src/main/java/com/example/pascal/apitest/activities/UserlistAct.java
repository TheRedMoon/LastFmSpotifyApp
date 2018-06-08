package com.example.pascal.apitest.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pascal.apitest.R;
import com.example.pascal.apitest.model.Playlist;
import com.example.pascal.apitest.model.User;
import com.example.pascal.apitest.util.BaseApp;
import com.example.pascal.apitest.util.DataBaseAdaptar;
import com.example.pascal.apitest.util.PrefUtils;
import com.example.pascal.apitest.util.SpinAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserlistAct extends AppCompatActivity {
    private EditText realname, nickname;
    private Button deleteuser, adduser;
    private ArrayList<User> userlistvalues;
    private ListView listView;
    private DataBaseAdaptar adapter;
    private ArrayList<String> showlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlists);
        realname = findViewById(R.id.user_realname);
        nickname = findViewById(R.id.user_nickname);
        deleteuser = findViewById(R.id.delete_user);
        adduser = findViewById(R.id.add_user);
//        playlist =  findViewById(R.id.txt_playlist);

        adduser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userrealname = realname.getText().toString();
                String usernickname = nickname.getText().toString();
                if(!usernickname.equals("")|| !userrealname.equals(""))
                addUser(userrealname, usernickname);
            }
        });
        deleteuser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userrealname = realname.getText().toString();
                String usernickname = nickname.getText().toString();
                deleteUser(userrealname, usernickname);
            }
        });
        userlistvalues = getUserValues();
        showlist = new ArrayList<>();
        if(userlistvalues != null && userlistvalues.size() != 0) {
            for (User us : userlistvalues) {
                showlist.add(us.toString());
            }
        }

        adapter = new DataBaseAdaptar(showlist, this);

        listView = ( ListView ) findViewById(R.id.user_list);
        listView.setChoiceMode(2);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String user = (String) listView.getItemAtPosition(position);
                TextView textView = (TextView) view.findViewById(R.id.text1);
                String parts[] = user.split("-");
                User u = BaseApp.userlistDAO.selectByUserName(parts[1].trim()).get(0);
                if(u.getActive() != 0){
//                    textView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    textView.setTextColor(Color.RED);
                    u.setActive(0);
                    Log.e("Test", "Light");
                    BaseApp.userlistDAO.update(u);
                }
                else{
//                    textView.setTypeface(Typeface.SANS_SERIF);
                    textView.setTextColor(Color.GREEN);
                    u.setActive(1);
                    Log.e("Test", "Full");
                    BaseApp.userlistDAO.update(u);
                }
//                listView.setItemChecked(position, !wasClicked);
            }
        });



    }

    private void deleteUser(String realname, String nickname) {
        if(realname == null) {
            BaseApp.userlistDAO.deleteByUserName(nickname);
        }
        else if(nickname == null){
            BaseApp.userlistDAO.deleteByRealName(realname);
        }
        else{
            Toast.makeText(this, "Enter valid user/real name", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    private void addUser(String realname, String nickname) {
        User u = new User();
        u.setUsername(nickname);
        u.setRealname(realname);
        u.setActive(1);
        BaseApp.userlistDAO.insert(u);
        userlistvalues = getUserValues();
        showlist = new ArrayList<>();
        if(userlistvalues != null && userlistvalues.size() != 0) {
            for (User us : userlistvalues) {
                showlist.add(us.toString());
            }
        }
        adapter.refreshUsers(showlist);
        Toast.makeText(this, "Sucessfully added a user! Please reload this to update the list", Toast.LENGTH_SHORT).show();
}

    private ArrayList<User> getUserValues() {
        List<User> userdatabase = BaseApp.userlistDAO.selectAll();
        ArrayList<User> result = new ArrayList<User>(userdatabase);
        return result;
    }

}
