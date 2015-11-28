package edu.npu.capstone.capstone.Data;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nick on 11/1/15.
 */
public class Poi {
    private String id;
    private String name;
    private String category;
    private String company;
    private PointF location;

    public Poi(String id, String name, String category, String company, PointF location) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.company = company;
        this.location = location;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getCategory() {return category;}
    public String getCompany() {return company;}
    public void setCompany(String company) {
        this.company = company;
    }
    public PointF getLocation() {
        return location;
    }

}
