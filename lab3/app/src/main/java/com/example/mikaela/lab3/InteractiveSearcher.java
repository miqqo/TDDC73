package com.example.mikaela.lab3;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class InteractiveSearcher extends EditText {
    private ListAdapter adapter;
    ListPopupWindow popupWindow;
    int numberOfResults = 8, counter = 0;
    InteractiveSearcher is = InteractiveSearcher.this;
    int count = 0;
    boolean clicked = false;


    public InteractiveSearcher(Context context) {
        super(context);

        addTextChangedListener(watch);

        adapter = new ListAdapter(context, numberOfResults);

        popupWindow = new ListPopupWindow(context);
        popupWindow.setAdapter(adapter);
        popupWindow.setAnchorView(is);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                clicked = true;
                is.setText(adapterView.getItemAtPosition(position).toString());

            }
        });
    }

    public InteractiveSearcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InteractiveSearcher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!clicked) {
                if (start > 0 || count > 0) {
                    counter++;
                    String searchLink = String.format("http://flask-afteach.rhcloud.com/getnames/%d/%s", counter, s);

                    RequestTask task = new RequestTask();
                    task.execute(searchLink);
                } else popupWindow.dismiss();
            }
            else{
                popupWindow.dismiss();
                clicked = false;
            }



        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private class RequestTask extends AsyncTask<String, String, JSONObject > {

        JSONObject jObj = null;


        @Override
        protected JSONObject doInBackground(String... url) {

            JSONObject j = null;
            try {
                j = getObjectFromUrl(url[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return j;

        }
        @Override
        protected void onPostExecute(JSONObject j) {

            int n = getnumberOfResults();

            try {
                int id = j.getInt("id");
                JSONArray tempArray = j.getJSONArray("result");

                if(id == counter) adapter.updateNames(tempArray, n);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            popupWindow.show();
        }


        private JSONObject getObjectFromUrl(String url) throws JSONException {
            URL url2 = null;
            String json = "";
            JSONObject obj = null;
            try {
                url2 = new URL(url);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url2.openConnection();

                if (!(urlConnection instanceof HttpURLConnection)) {
                    throw new IOException("Connection not working");
                }
                urlConnection.connect();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;

                if(is != null) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "n");
                        count++;
                    }

                    urlConnection.disconnect();
                    json = sb.toString();

                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());

                    }

                    return jObj;
                }

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }


    }

    public int getnumberOfResults(){
        return numberOfResults;
    }

}
