package com.meteorology.meteorology;

import com.activeandroid.ActiveAndroid;

import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;


public class MainActivity extends Activity {
    String test = "ttt  ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this.getApplication());
//        updateDatabase();
        new SpiderTask().execute();
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

    private class SpiderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg) {
            try {
                City.deleteAll();
                DayInfo.deleteAll();
                String url = "http://www.cwb.gov.tw/V7/forecast/week/week.htm";
                Document doc = Jsoup.connect(url).get();
                Elements city_table = doc.select("table.BoxTableInside > tbody > tr");

                for(Element tr : city_table) {
                    Elements th = tr.select("th");
                    if(th.size() > 0 && th.get(0).text() != "") {
                        City city = new City();
                        city.name = th.get(0).text().toString();
                        city.save();

                        Log.d("City Name", city.name);
                    }
                    Elements tds = tr.select("td");
                    if(tds.size() > 0)
                    {
                        String am_or_pm = tds.get(0).text().toString();
                        Log.d("Am Pm", am_or_pm);
                        Calendar calendar = Calendar.getInstance();
                        for(int i = 1; i < 8; i++) {
                            DayInfo dayInfo = new DayInfo();
                            dayInfo.date = "" + calendar.get(calendar.MONTH) + "/" + calendar.get(calendar.DATE);
                            dayInfo.week = Integer.toString(calendar.DATE);
                            dayInfo.am_or_pm = am_or_pm;
                            dayInfo.weather = tds.get(i).select("img").attr("title").toString();
                            dayInfo.temperature = tds.get(i).text().toString();
                            dayInfo.save();
                            calendar.add(calendar.DATE, 1);
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.d("Stop", "");
        }
    }

}
