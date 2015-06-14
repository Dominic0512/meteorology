package com.meteorology.meteorology.Class;

import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.meteorology.meteorology.Model.City;
import com.meteorology.meteorology.Model.DayInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lizhe on 2015/6/13.
 */
public class UpdateDatabase extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... arg) {
        ActiveAndroid.beginTransaction();
        try {
            String url = "http://www.cwb.gov.tw/V7/forecast/week/week.htm";
            Document doc = Jsoup.connect(url).get();
            Elements city_table = doc.select("table.BoxTableInside > tbody > tr");
            int c_id = 0;
            for(Element tr : city_table) {
                Elements th = tr.select("th");
                if(th.size() > 0 && th.get(0).text() != "") {
                    String city_name = th.get(0).text().toString();
                    if(City.isExist(city_name)) {
                        c_id = City.getCity(city_name).c_id;
                    }
                }
                Elements tds = tr.select("td");
                if(tds.size() > 0)
                {
                    String am_or_pm = tds.get(0).text().toString();
                    Log.d("City id", Integer.toString(c_id));
                    Log.d("Am Pm", am_or_pm);
                    Calendar calendar = Calendar.getInstance();
                    List<DayInfo> dayInfos = DayInfo.getByCity(c_id, am_or_pm);
                    Log.d("Day size", Integer.toString(dayInfos.size()));
                    for(int i = 1; i < 8; i++) {
                        Log.d("Day id", Integer.toString(dayInfos.get(i-1).d_id));
                        dayInfos.get(i-1).date = "" + calendar.get(calendar.MONTH) + "/" + calendar.get(calendar.DATE);
                        dayInfos.get(i-1).week = Integer.toString(calendar.DATE);
                        dayInfos.get(i-1).am_or_pm = am_or_pm;
                        dayInfos.get(i-1).weather = tds.get(i).select("img").attr("title").toString();
                        dayInfos.get(i-1).temperature = tds.get(i).text().toString();
                        dayInfos.get(i-1).save();
                        calendar.add(calendar.DATE, 1);
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
