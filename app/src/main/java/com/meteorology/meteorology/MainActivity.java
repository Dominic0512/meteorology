package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.Advertising;
import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import com.meteorology.meteorology.Class.Seeder;
import com.meteorology.meteorology.Class.UpdateDatabase;
import com.meteorology.meteorology.Class.Internet;
import com.meteorology.meteorology.Class.Config;
import com.meteorology.meteorology.Class.HelpService;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends Activity {
    Config config = new Config(this);
    HelpService hs = new HelpService();
    List<City> cities = new ArrayList<>();
    int SECOND_UNIT = 1000, ad_times = 0;
    Timer ad_timer, clock_timer;
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

        remoteDataSync();
        advertisingSync();
        initSideBar();
        initMainContainer();

        ad_timer = new Timer();
        ad_timer.schedule(new AdvertisingTask(this), 5 * SECOND_UNIT, 10 * SECOND_UNIT);
        clock_timer = new Timer();
        clock_timer.schedule(new clockTask(), 1 * SECOND_UNIT, 60 * SECOND_UNIT);
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

    private void advertisingSync() {
        if(Advertising.getAll().size() == 0) {
            for( int i = 1; i <= 8; i++ ) {
                Advertising ad = new Advertising();
                ad.ad_id = i;
                ad.name = "ad" + i;
                ad.is_showed = false;
                ad.save();
            }
        }
        else {
            if(Advertising.isAllShowed()) {
                Advertising.reset();
            }
        }
    }

    private void remoteDataSync() {
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
    }

    private void initMainContainer() {
        RelativeLayout main_container = (RelativeLayout) findViewById(R.id.main);

        if(Config.am_or_pm == "白天") {
            main_container.setBackgroundResource(R.drawable.morning);
        }
        else {
            main_container.setBackgroundResource(R.drawable.night);
        }
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
            city_btn.setTextColor(Config.textColor);
            city_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.getScreenHigh()/15);
            city_btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            city_btn.setOnClickListener(new CityOnClickListener(city, this));

            city_row.addView(city_btn);
            left_side_bar.addView(city_row);
        }
    }

    private class clockTask extends TimerTask {
        @Override
        public void run () {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clockTimerTracker();
                }
            });
        }
        private void clockTimerTracker() {
            TextView clock_timer_view = (TextView) findViewById(R.id.timer);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            clock_timer_view.setText(dateFormat.format(calendar.getTime()));
        }
    }

    private class AdvertisingTask extends TimerTask {
        Context context;
        public AdvertisingTask(Context context) {
            this.context = context;
        }

        @Override
        public void run () {
            if(ad_times < 2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ad_times++;
                        ad_container.removeAllViews();

                        main.removeView(ad_container);
                        ad_close.setText("關閉廣告");
                        ad_close.setTextSize(30);
                        ad_close.setBackgroundColor(Color.argb(200, 128, 128, 128));
                        ad_close.setTextColor(Color.WHITE);

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
                        ad_container.setBackgroundColor(Color.BLACK);

                        ImageView ad_img_view = new ImageView(context);
                        int ad_resId = getResources().getIdentifier(getRandomAd(), "drawable", getPackageName());
                        ad_img_view.setImageResource(ad_resId);

                        ad_container.addView(ad_img_view);
                        main.addView(ad_container);
                        Handler close_handler = new Handler();
                        close_handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ad_container.addView(ad_close);
                            }
                        }, 3000);
                    }
                });
            }
            else {
                if (ad_timer != null) {
                    ad_timer.cancel();
                }
            }
        }
        private String getRandomAd() {
            List<Advertising> not_showed_ads  = new ArrayList<>();
            Random rand = new Random();
            not_showed_ads = Advertising.getAdsForNotShowed();
            int rid = rand.nextInt(not_showed_ads.size());
            //-- Update ad attribute for is_showed
            Advertising show_ad = not_showed_ads.get(rid);
            show_ad.is_showed = true;
            show_ad.save();

            return not_showed_ads.get(rid).name;
        }
    }

    public class CityOnClickListener implements View.OnClickListener {
        Context context;
        City city;
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(calendar.MONTH) + 1 + "/" + calendar.get(calendar.DATE);

        public CityOnClickListener(City city, Context context) {
            this.city = city;
            this.context = context;
        }

        @Override
        public void onClick(View v){
            int TextColor = config.getTextColor();
            String AmOrPm = config.getAmOrPm();

            LinearLayout report_frame = (LinearLayout) findViewById(R.id.report_frame);
            TextView city_name = (TextView) findViewById(R.id.city_name);
            ImageView today_weather_img = (ImageView) findViewById(R.id.today_weather_img);
            TextView temperature = (TextView) findViewById(R.id.temperature);
            TextView today_weather = (TextView) findViewById(R.id.today_weather);

            LinearLayout weekly_report = (LinearLayout) findViewById(R.id.weekly_report);

            DayInfo today = DayInfo.getByCityAndDate(this.city.c_id, this.today, AmOrPm);
            List<DayInfo> week_morning = new ArrayList<>();
            List<DayInfo> week_night = new ArrayList<>();
            week_morning = DayInfo.getByCity(this.city.c_id, "白天");
            week_night = DayInfo.getByCity(this.city.c_id, "晚上");



            city_name.setText(this.city.name);
            city_name.setTextColor(TextColor);
            //-- Set image by resource file name
            String tw_img_name = hs.getImageName(today.weather, AmOrPm);
            int tw_resId = getResources().getIdentifier(tw_img_name, "drawable", getPackageName());
            today_weather_img.setImageResource(tw_resId);

            temperature.setText(today.temperature + "°c");
            temperature.setTextColor(TextColor);

            today_weather.setText(today.weather);
            today_weather.setTextColor(TextColor);

            int rps_width = report_frame.getWidth() / 7;
            int rps_height = report_frame.getHeight() - 20;

            weekly_report.removeAllViews();
            for(int i = 0; i < 7 ; i++)
            {
                LinearLayout report = new LinearLayout(context);
                LinearLayout.LayoutParams rp_LayoutParams = new LinearLayout.LayoutParams(rps_width, rps_height);
                rp_LayoutParams.setMargins(10, 10, 10, 10);
                report.setLayoutParams(rp_LayoutParams);
                report.setOrientation(LinearLayout.VERTICAL);
                report.setBackgroundColor(Color.argb(128, 128, 128, 128));

                TextView rp_date = new TextView(context);
                LinearLayout.LayoutParams rp_date_lp = new LinearLayout.LayoutParams(rps_width, hs.getScalar(rps_height, 1.0, 12.0));
                rp_date.setLayoutParams(rp_date_lp);
                rp_date.setBackgroundColor(Color.argb(200, 128, 128, 128));
                rp_date.setText(week_morning.get(i).date);
                rp_date.setTextSize(22);
                rp_date.setTextColor(TextColor);
                rp_date.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView rp_day = new TextView(context);
                LinearLayout.LayoutParams rp_day_lp = new LinearLayout.LayoutParams(rps_width, hs.getScalar(rps_height, 1.0, 12.0));
                rp_day.setLayoutParams(rp_day_lp);
                rp_day.setText(week_morning.get(i).day_of_week);
                rp_day.setTextColor(TextColor);
                rp_day.setTextSize(24);
                rp_day.setGravity(Gravity.CENTER_HORIZONTAL);

                LinearLayout rp_m_img_frame = new LinearLayout(context);
                LinearLayout.LayoutParams rp_m_img_lp = new LinearLayout.LayoutParams(rps_width, hs.getScalar(rps_height, 4.0, 12.0));
                rp_m_img_frame.setLayoutParams(rp_m_img_lp);
                rp_m_img_frame.setGravity(Gravity.CENTER_HORIZONTAL);
                ImageView rp_m_img = new ImageView(context);
                String m_img = hs.getImageName(week_morning.get(i).weather, "白天");
                int m_resId = getResources().getIdentifier(m_img, "drawable", getPackageName());
                rp_m_img.setImageResource(m_resId);
                rp_m_img_frame.addView(rp_m_img);

                TextView rp_m_temperature = new TextView(context);
                LinearLayout.LayoutParams rp_m_t_lp = new LinearLayout.LayoutParams(rps_width, hs.getScalar(rps_height, 1.0, 12.0));
                rp_m_temperature.setLayoutParams(rp_m_t_lp);
                rp_m_temperature.setText(week_morning.get(i).temperature);
                rp_m_temperature.setTextColor(TextColor);
                rp_m_temperature.setTextSize(24);
                rp_m_temperature.setGravity(Gravity.CENTER_HORIZONTAL);

                LinearLayout rp_n_img_frame = new LinearLayout(context);
                LinearLayout.LayoutParams rp_n_img_lp = new LinearLayout.LayoutParams(rps_width, hs.getScalar(rps_height, 4.0, 12.0));
                rp_n_img_frame.setLayoutParams(rp_n_img_lp);
                rp_n_img_frame.setGravity(Gravity.CENTER_HORIZONTAL);
                ImageView rp_n_img = new ImageView(context);
                String n_img = hs.getImageName(week_night.get(i).weather, "晚上");
                int n_resId = getResources().getIdentifier(n_img, "drawable", getPackageName());
                rp_n_img.setImageResource(n_resId);
                rp_n_img_frame.addView(rp_n_img);

                TextView rp_n_temperature = new TextView(context);
                LinearLayout.LayoutParams rp_n_t_lp = new LinearLayout.LayoutParams(rps_width, hs.getScalar(rps_height, 1.0, 12.0));
                rp_n_temperature.setLayoutParams(rp_n_t_lp);
                rp_n_temperature.setText(week_night.get(i).temperature);
                rp_n_temperature.setTextColor(TextColor);
                rp_n_temperature.setTextSize(24);
                rp_n_temperature.setGravity(Gravity.CENTER_HORIZONTAL);

                report.addView(rp_date);
                report.addView(rp_day);
                report.addView(rp_m_img_frame);
                report.addView(rp_m_temperature);
                report.addView(rp_n_img_frame);
                report.addView(rp_n_temperature);
                weekly_report.addView(report);
            }
        }
    }
}
