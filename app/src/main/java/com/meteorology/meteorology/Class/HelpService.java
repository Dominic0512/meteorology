package com.meteorology.meteorology.Class;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;

/**
 * Created by lizhe on 15/7/12.
 */
public class HelpService {

    public String getDayOfWeekString(int day_of_week) {
        switch(day_of_week){
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "None";
        }
    }

    public String getImageName(String weather, String am_or_pm) {
        ArrayList<String> name_set = new ArrayList<String>();
        if(am_or_pm == "白天") {
            name_set.add("sun");
        }
        else {
            name_set.add("moon");
        }

        if(weather.contains("雲")) {
            name_set.add("cloud");
        }
        if(weather.contains("雨")) {
            name_set.add("rain");
        }
        if(weather.contains("雷")) {
            name_set.add("light");
        }

        return StringUtil.join(name_set, "_");
    }

    public int getScalar(int total_scalar, double scale, double total_scale) {
        return (int) ((double)total_scalar * (scale/total_scale));
    }

    public String temperture_formater(String temp) {
        String[] parts = temp.split("~");
        for(int i = 0; i < parts.length; i++) {
            parts[i] = parts[i] + "°";
        }

        temp = parts[0] + " ~" + parts[1];

        return temp;
    }
}
