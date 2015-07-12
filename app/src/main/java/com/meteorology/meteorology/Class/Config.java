package com.meteorology.meteorology.Class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Calendar;


/**
 * Created by lizhe on 15/7/11.
 */
public class Config {
    private static Calendar calendar = Calendar.getInstance();
    private Context context;
    private DisplayMetrics displaymetrics = new DisplayMetrics();
    public static String am_or_pm = "";
    public static int textColor = 0;

    public Config(Context context) {
        this.context = context;
        this.am_or_pm = getAmOrPm();
        this.textColor = getTextColor();
    }

    private static int getTextColor() {
        if(calendar.get(calendar.HOUR_OF_DAY) <= 12) {
            return Color.BLACK;
        }
        else {
            return Color.WHITE;
        }
    }

    private static String getAmOrPm() {
        if(calendar.get(calendar.HOUR_OF_DAY) <= 12) {
            return "白天";
        }
        else {
            return "晚上";
        }
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
