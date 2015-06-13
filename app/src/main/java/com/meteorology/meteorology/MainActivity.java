package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import com.meteorology.meteorology.Class.Seeder;
import com.meteorology.meteorology.Class.UpdateDatabase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this.getApplication());
//        new UpdateDatabase().execute();
//        new Seeder().execute();
//        List<City> test = City.getCityWithDayInfo();
//        for( int i = 0; i < test.size(); i++ )
//        {
//            Log.d("hihi", test.get(i).name);
//        }

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

    public void initMainView() {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        TextView console = new TextView(this);
        String all = "";
        List<City> cities = new ArrayList<>();
        cities = City.getAll();
        for( int i = 0; i < cities.size(); i++ ) {
            all += cities.get(i).getId();
            all += cities.get(i).name;
        }
        console.setText(all);
        container.addView(console);
    }
}
