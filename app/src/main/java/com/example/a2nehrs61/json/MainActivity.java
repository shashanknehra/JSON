package com.example.a2nehrs61.json;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    class MyTask extends AsyncTask<String,Void,String>
    {
        Context parent;
        public MyTask(Context p)
        {
            parent = p;
        }
        public String doInBackground(String... artists)
        {
            HttpURLConnection conn = null;
            try
            {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/hits.php?artist="+ URLEncoder.encode(artists[0], "UTF-8") +"&format=json");
                // Michael Jackson -> Michael%20Jackson
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                if(conn.getResponseCode() == 200)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    String text="";
                    String json="";

                    // [ { madonna songs } ] [ { oasis songs } ]
                    while((line = br.readLine()) !=null)
                    {
                        json += line;
                    }

                    JSONArray jsonarray = new JSONArray(json);
                    String song,artist,year,month,chart,quantity,id;
                    for (int i=0;i<jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        song = jsonObj.getString("song");
                        artist = jsonObj.getString("artist");
                        year = jsonObj.getString("year");
                        month = jsonObj.getString("month");
                        chart = jsonObj.getString("chart");
                        quantity = jsonObj.getString("quantity");
                        id = jsonObj.getString("ID");

                        text += "ID = " + id + "Song = " + song + " Artist Name = " + artist + " Release date = " + month + "-" + year + " Chart = " + chart + "Quantity = " + quantity + "\n";
                    }

                    return text;
                }
                else
                {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            }
            catch(IOException e)
            {
                return e.toString();
            }
            catch(JSONException e)
            {
                return e.toString();
            }

        }

        public void onPostExecute(String text)
        {
            TextView tv = (TextView)findViewById(R.id.textview);
            tv.setText(text);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go = (Button)findViewById(R.id.btn);
        go.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        EditText et = (EditText) findViewById(R.id.input);
        String input = et.getText().toString();
        MyTask t = new MyTask(this);
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(input).show();
        t.execute(input);
    }
}