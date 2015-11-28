package edu.npu.capstone.capstone.Data;

import android.graphics.PointF;

/**
 * Created by Nick on 11/1/15.
 */
public class Company {
    private String id;
    private String name;
    private String description;

    public Company(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
