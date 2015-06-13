package com.meteorology.meteorology.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by lizhe on 2015/6/9.
 */
public class DayInfo extends Model {
    @Column(name="c_id")
    public int c_id;
    @Column(name="d_id")
    public int d_id;
    @Column(name="date")
    public String date;
    @Column(name="week")
    public String week;
    @Column(name="am_or_pm")
    public String am_or_pm;
    @Column(name="temperature")
    public String temperature;
    @Column(name="weather")
    public String weather;

    public DayInfo() {
        super();
    }

    public static List<DayInfo> getByCity(int c_id, String am_or_pm) {
        return new Select()
                .from(DayInfo.class)
                .where("c_id = ? and am_or_pm = ?", c_id, am_or_pm)
                .execute();
    }

    public static List<DayInfo> getAll() {
        return new Select()
                .from(DayInfo.class)
                .execute();
    }

    public static void deleteAll() {
        new Delete()
                .from(DayInfo.class)
                .execute();
    }
}
