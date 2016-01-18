package com.example.mikaela.lab3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {

    private Context context;
    private int nrOfNames, id, currentId = 0;
    JSONArray names;
    String currentName;


    public ListAdapter(Context con, int size) {
        nrOfNames = size;
        context = con;
    }
    public JSONArray getList() {

        return names;

    }

    @Override
    public int getCount() {

        return names.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return names.getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            currentName = names.getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Item currentItem = new Item(context, currentName);
        return currentItem;
    }

    public void updateNames(JSONArray tempArray, int n) throws JSONException {

        nrOfNames = n;
        names = new JSONArray();

        if(tempArray.length() < nrOfNames){
            nrOfNames = tempArray.length();
        }

        for ( int i = 0; i < nrOfNames ; i++) {
            String currentString = tempArray.getString(i);
            names.put(currentString);
        }

    }

}


