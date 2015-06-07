package com.meteorology.meteorology.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by lizhe on 2015/6/7.
 */
@Table(name="City")
public class City extends Model{
    @Column(name="name")
    public String name;

    public City() {
        super();
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
