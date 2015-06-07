package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.City;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

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
        City city = new City();
        city.name = "Fuck u";
        city.save();
    }

    public void initMainView() {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        TextView console = new TextView(this);
        String all = "";
        List<City> cities = new ArrayList<>();
        cities = City.getAll();
        for( int i = 0; i < cities.size(); i++ ) {
            all += cities.get(i).name;
        }

        console.setText(all);
        container.addView(console);
    }
}
