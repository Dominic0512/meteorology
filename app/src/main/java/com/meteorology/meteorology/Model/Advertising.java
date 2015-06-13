package com.meteorology.meteorology.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

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

}
