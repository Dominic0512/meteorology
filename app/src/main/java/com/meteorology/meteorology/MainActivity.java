package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.City;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    String test = "ttt  ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this.getApplication());

        updateDatabase();
        initMainView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void updateDatabase() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String url = "http://www.google.com/";
                    String USER_AGENT = "Mozilla/5.0";

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", USER_AGENT);

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    test = response.toString();
                    //print result
                    Log.d("HTML", test);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void initMainView() {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        TextView console = new TextView(this);
        String all = "";
//        List<City> cities = new ArrayList<>();
//        cities = City.getAll();
//        for( int i = 0; i < cities.size(); i++ ) {
//            all += cities.get(i).name;
//        }

        console.setText(test);
        container.addView(console);
    }
}
