package com.meteorology.meteorology.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhe on 2015/6/13.
 */
@Table(name="Advertising")
public class Advertising extends Model{
    @Column(name="ad_id")
    public int ad_id;
    @Column(name="name")
    public String name;
    @Column(name="is_showed")
    public boolean is_showed;

    public Advertising() { super(); }

    public static List<Advertising> getAll() {
        return new Select().from(Advertising.class)
                           .execute();
    }

    public static List<Advertising> getAdsForNotShowed() {
        return new Select().from(Advertising.class)
                           .where("is_showed = ?", 0)
                           .execute();
    }

    public static List<Advertising> getAdsForShowed() {
        return new Select().from(Advertising.class)
                           .where("is_showed = ?", 1)
                           .execute();
    }

    public static boolean isAllShowed() {
        List<Advertising> all_ads = new ArrayList<Advertising>();
        List<Advertising> showed_ads = new ArrayList<Advertising>();
        all_ads = getAll();
        showed_ads = getAdsForShowed();

        if( showed_ads.size() == all_ads.size() ) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void reset() {
        List<Advertising> all_ads = new ArrayList<Advertising>();
        all_ads = getAll();
        for( Advertising ad : all_ads ) {
            ad.is_showed = false;
            ad.save();
        }
    }
}
