package com.meteorology.meteorology.Class;

import com.activeandroid.ActiveAndroid;

import android.os.AsyncTask;
import android.util.Log;

import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

/**
 * Created by lizhe on 2015/6/13.
 * Delete origin data in the data base then insert new data into database
 */
public class Seeder extends AsyncTask<Void, Void, Void> {
    HelpService hs = new HelpService();
    @Override
    protected Void doInBackground(Void... arg) {
        ActiveAndroid.beginTransaction();
        try {
            City.deleteAll();
            DayInfo.deleteAll();
            String url = "http://www.cwb.gov.tw/V7/forecast/week/week.htm";
            Document doc = Jsoup.connect(url).get();
            Elements city_table = doc.select("table.BoxTableInside > tbody > tr");
            int index = 0;
            int d_index = 1;
            for(Element tr : city_table) {
                Elements th = tr.select("th");
                if(th.size() > 0 && th.get(0).text() != "") {
                    index++;
                    String name = th.get(0).text().toString();
                    City city = new City();
                    city.name = name;
                    city.c_id = index;
                    city.save();
                }
                Elements tds = tr.select("td");
                if(tds.size() > 0)
                {
                    String am_or_pm = tds.get(0).text().toString();
                    Log.d("Am Pm", am_or_pm);
                    Calendar calendar = Calendar.getInstance();
                    for(int i = 1; i < 8; i++) {
                        DayInfo dayInfo = new DayInfo();
                        dayInfo.c_id = index;
                        dayInfo.d_id = d_index;
                        dayInfo.date = "" + calendar.get(calendar.MONTH) + "/" + calendar.get(calendar.DATE);
                        dayInfo.day_of_week = hs.getDayOfWeekString(calendar.get(calendar.DAY_OF_WEEK));
                        dayInfo.am_or_pm = am_or_pm;
                        dayInfo.weather = tds.get(i).select("img").attr("title").toString();
                        dayInfo.temperature = tds.get(i).text().toString();
                        dayInfo.save();
                        calendar.add(calendar.DATE, 1);
                        d_index++;
                    }
                }

            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            ActiveAndroid.endTransaction();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Log.d("Stop", "");
    }
}
