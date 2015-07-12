package com.meteorology.meteorology.Class;

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
}
