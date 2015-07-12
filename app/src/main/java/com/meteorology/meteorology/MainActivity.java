package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import com.meteorology.meteorology.Class.Seeder;
import com.meteorology.meteorology.Class.UpdateDatabase;
import com.meteorology.meteorology.Class.Internet;
import com.meteorology.meteorology.Class.Config;


import android.app.Activity;
import android.content.Context;
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
    Config config = new Config(this);
    List<City> cities = new ArrayList<>();
    int SECOND_UNIT = 1000, ad_times = 0;
    Timer timer;
    RelativeLayout ad_container, main;
    Button ad_close;

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
            if(DayInfo.getAll().size() == 0 ) {
                new Seeder().execute();
            }

            try{
                new UpdateDatabase().execute();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("Connected", "FAIL!");
        }
        initSideBar();
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

    private void initSideBar() {
        LinearLayout left_side_bar = (LinearLayout) findViewById(R.id.left_side_bar);

        cities = City.getAll();

        for( int i = 0; i < cities.size(); i++ ) {
            City city = cities.get(i);
            LinearLayout city_row = new LinearLayout(this);
            city_row.setLayoutParams(new LinearLayout.LayoutParams(config.getScreenWidth()/5, config.getScreenHigh()/8));
            city_row.setOrientation(LinearLayout.HORIZONTAL);

            Button city_btn = new Button(this);
            city_btn.setId(city.c_id);
            city_btn.setText(city.name);
            city_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.getScreenHigh()/15);
            city_btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            city_btn.setOnClickListener(new CityOnClickListener(city, this));

            city_row.addView(city_btn);
            left_side_bar.addView(city_row);
        }
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

    public class CityOnClickListener implements View.OnClickListener {
        Context context;
        City city;
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(calendar.MONTH) + 1 + "/" + calendar.get(calendar.DATE);
        String am_or_pm;

        public CityOnClickListener(City city, Context context) {
            this.city = city;
            this.context = context;
            if(this.calendar.get(calendar.HOUR_OF_DAY) <= 12) {
                this.am_or_pm = "白天";
            }
            else {
                this.am_or_pm = "晚上";
            }
        }

        @Override
        public void onClick(View v){
            LinearLayout report_frame = (LinearLayout) findViewById(R.id.report_frame);
            TextView date = (TextView) findViewById(R.id.date);
            TextView city_name = (TextView) findViewById(R.id.city_name);
            TextView temperature = (TextView) findViewById(R.id.temperature);
            TextView weather_detail = (TextView) findViewById(R.id.weather_detail);
            LinearLayout weekly_report = (LinearLayout) findViewById(R.id.weekly_report);

            DayInfo today = DayInfo.getByCityAndDate(this.city.c_id, this.today, this.am_or_pm);
            List<DayInfo> week = new ArrayList<>();
            week = DayInfo.getByCity(this.city.c_id, this.am_or_pm);

            date.setText(today.date);
            city_name.setText(this.city.name);
            temperature.setText(today.temperature);
            weather_detail.setText(today.weather);

            int rps_width = report_frame.getWidth();
            Log.d("rps", "" + rps_width);
            weekly_report.removeAllViews();
            for(int i = 0; i < week.size() ; i++)
            {
                LinearLayout report = new LinearLayout(context);
                LinearLayout.LayoutParams rp_LayoutParams = new LinearLayout.LayoutParams(rps_width / 7, LinearLayout.LayoutParams.MATCH_PARENT);
                rp_LayoutParams.setMargins(10, 10, 10, 10);
                report.setLayoutParams(rp_LayoutParams);
                report.setOrientation(LinearLayout.VERTICAL);
                report.setBackgroundColor(Color.argb(128, 128, 128, 128));

                TextView rp_date = new TextView(context);
                rp_date.setBackgroundColor(Color.argb(200, 128, 128, 128));
                rp_date.setText(week.get(i).date);
                rp_date.setTextSize(24);
                rp_date.setTextColor(Color.WHITE);

                TextView rp_day = new TextView(context);
                rp_day.setText(week.get(i).day_of_week);

                report.addView(rp_date);
                report.addView(rp_day);
                weekly_report.addView(report);
            }
        }
    }
}
