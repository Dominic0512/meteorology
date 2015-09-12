package com.meteorology.meteorology.Class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.meteorology.meteorology.Model.Advertising;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by lizhe on 15/7/11.
 */
public class Config {
    private Calendar calendar;
    private Context context;
    private DisplayMetrics displaymetrics = new DisplayMetrics();
    public static String am_or_pm = "";
    public static int textColor = 0;
    public static String ad_id = "";

    public Config(Context context) {
        this.context = context;
        this.am_or_pm = getAmOrPm();
        this.textColor = getTextColor();
        this.ad_id = getRandomAd();
    }

    public int getTextColor() {
        calendar = Calendar.getInstance();
        if(calendar.get(calendar.HOUR_OF_DAY) <= 18 && calendar.get(calendar.HOUR_OF_DAY) >= 6) {
            return Color.BLACK;
        }
        else {
            return Color.BLACK;
        }
    }

    public String getAmOrPm() {
        calendar = Calendar.getInstance();
        if(calendar.get(calendar.HOUR_OF_DAY) <= 18 && calendar.get(calendar.HOUR_OF_DAY) >= 6) {
            return "白天";
        }
        else {
            return "晚上";
        }
    }

    public String getRandomAd() {
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

    public int getScreenHigh() {
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics.heightPixels;
    }

    public int getScreenWidth() {
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
}
