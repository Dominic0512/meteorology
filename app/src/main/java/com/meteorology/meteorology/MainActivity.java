package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import com.meteorology.meteorology.Class.Seeder;
import com.meteorology.meteorology.Class.UpdateDatabase;
import com.meteorology.meteorology.Class.Internet;

import com.meteorology.meteorology.CityDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends Activity {
    int SECOND_UNIT = 1000;

    Timer timer;
    RelativeLayout ad_container, main;
    Button ad_close;
    int ad_times = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this.getApplication());
        main = (RelativeLayout) findViewById(R.id.main);
        ad_container = new RelativeLayout(this);
        ad_close = new Button(this);
        if(Internet.haveNetworkConnection(this)) {
            Log.d("Connected", "SUCCESSFUL!");
            try{
                new UpdateDatabase().execute();
            }
            catch (Exception e) {
                e.printStackTrace();
                new Seeder().execute();
            }
        }
        else {
            Log.d("Connected", "FAIL!");
        }

        initMainView();
        timer = new Timer();

        timer.schedule(new AdvertsingTask(), 5 * SECOND_UNIT, 10 * SECOND_UNIT);
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
        LinearLayout left_side_bar = (LinearLayout) findViewById(R.id.left_side_bar);
        List<City> cities = new ArrayList<>();
        cities = City.getAll();

        for( int i = 0; i < cities.size(); i++ ) {
            City city = cities.get(i);
            LinearLayout city_row = new LinearLayout(this);
            city_row.setLayoutParams(new LinearLayout.LayoutParams(300, 150));
            city_row.setOrientation(LinearLayout.HORIZONTAL);

            Button city_btn = new Button(this);
            city_btn.setId(city.c_id);
            city_btn.setText(city.name);
            city_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);
            city_btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            city_btn.setOnClickListener(new CityOnClickListener(city));

            city_row.addView(city_btn);
            left_side_bar.addView(city_row);
        }

        Button city_detail = (Button) findViewById(R.id.city_detail);
        city_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityDetail.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class AdvertsingTask extends TimerTask {
        @Override
        public void run () {
            if(ad_times < 2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ad_times++;
                        ad_container.removeAllViews();
                        main.removeView(ad_container);
                        ad_close.setText("Close");
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        ad_close.setLayoutParams(lp);
                        ad_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                main.removeView(ad_container);
                                main.removeView(ad_close);
                            }
                        });

                        ad_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        ad_container.setBackgroundColor(Color.BLUE);
                        ad_container.addView(ad_close);
                        main.addView(ad_container);
                    }
                });
            }
            else {
                if (timer != null) {
                    timer.cancel();
                }
            }

        }
    }



    private class CityOnClickListener implements View.OnClickListener {
        City city;
        public CityOnClickListener(City city) {
            this.city = city;
        }

        @Override
        public void onClick(View v){
            Calendar calendar = Calendar.getInstance();
            String today = calendar.get(calendar.MONTH) + 1 + "/" + calendar.get(calendar.DATE);
            int hour = calendar.get(calendar.HOUR_OF_DAY);

            TextView date = (TextView) findViewById(R.id.date);
            TextView city_name = (TextView) findViewById(R.id.city_name);
            TextView morning_temperature = (TextView) findViewById(R.id.morning_temperature);
            TextView night_temperature = (TextView) findViewById(R.id.night_temperature);
            TextView weather_detail = (TextView) findViewById(R.id.weather_detail);
            DayInfo today_morning = DayInfo.getByCityAndDate(this.city.c_id, today, "白天");
            DayInfo today_night = DayInfo.getByCityAndDate(this.city.c_id, today, "晚上");

            date.setText(today_morning.date);
            city_name.setText(this.city.name);
            morning_temperature.setText(today_morning.temperature);
            night_temperature.setText(today_night.temperature);
            if(hour <= 12) {
                weather_detail.setText(today_morning.weather);
            }
            else {
                weather_detail.setText(today_night.weather);
            }
        }
    }
}
