package com.meteorology.meteorology.Class;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;


/**
 * Created by lizhe on 15/7/11.
 */
public class Config {
    private Context context;
    private DisplayMetrics displaymetrics = new DisplayMetrics();

    public Config(Context context) {
        this.context = context;
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
