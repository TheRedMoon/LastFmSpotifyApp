package com.example.pascal.apitest.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pascal.apitest.R;
import com.example.pascal.apitest.model.User;

import java.util.ArrayList;

/**
 * Created by jeroen on 1-6-2018.
 */

public class DataBaseAdaptar extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;



    public DataBaseAdaptar(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.text1);
        String user = list.get(position);
        Log.e("Test0", user);
        listItemText.setText(user);
        Log.e("Test1", user);
        String parts[] = user.split("-");
        if(BaseApp.userlistDAO.selectByUserName(parts[1].trim()).size() != 0){
            User u = BaseApp.userlistDAO.selectByUserName(parts[1].trim()).get(0);
            if(u.getActive() != 0){
                listItemText.setTextColor(Color.GREEN);
            }
            else{
                listItemText.setTextColor(Color.RED);
            }
        }

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
//        Button addBtn = (Button)view.findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                String user = list.get(position);
                String parts[] = user.split("-");
                list.remove(position); //or some other task
                deleteUser(parts[1].trim());
                notifyDataSetChanged();
            }
        });
//        addBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                notifyDataSetChanged();
//            }
//        });

        return view;
    }

    private void deleteUser(String username) {
        if(!username.equals("")) {
            BaseApp.userlistDAO.deleteByUserName(username);
        }
    }

    public void refreshUsers(ArrayList<String> users) {
        this.list.clear();
        this.list.addAll(users);
        notifyDataSetChanged();
    }

}