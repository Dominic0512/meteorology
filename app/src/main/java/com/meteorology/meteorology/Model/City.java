package com.meteorology.meteorology.Model;

import android.database.Cursor;

import com.activeandroid.Model;
import com.activeandroid.Cache;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.From;

import java.util.List;

/**
 * Created by lizhe on 2015/6/7.
 */
@Table(name="City")
public class City extends Model {
    @Column(name="c_id")
    public int c_id;
    @Column(name="name")
    public String name;

    public City() {
        super();
    }

    public static boolean isExist(String name) {
        List<City> row_data = new Select().from(City.class).where("name = ?", name).execute();
        if(row_data.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

//    public static List getCityWithDayInfo()
//    {
//       From query = new Select()
//            .from(City.class)
//            .leftJoin(DayInfo.class)
//            .on("City.c_id = DayInfo.c_id");
//        Cursor cursor = Cache.openDatabase().rawQuery(query.toSql(), query.getArguments());
//
//    }


    public static City getCity(String name ) {
        List<City> row_data = new Select().from(City.class).where("name = ?", name).execute();
        return row_data.get(0);
    }

    public static List<City> getAll() {
        return new Select()
                .from(City.class)
                .execute();
    }

    public static void deleteAll() {
        new Delete()
                .from(City.class)
                .execute();
    }
}
