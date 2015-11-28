package edu.npu.capstone.capstone.Data;

import android.content.Context;
import android.graphics.PointF;
import android.telephony.TelephonyManager;

import edu.npu.capstone.capstone.Activity.MainActivity;

/**
 * Created by Nick on 11/1/15.
 */
public class Location {
    private PointF location;
    private String id;

    public Location(PointF location, String id) {
        this.location = location;
        this.id = id;
    }

    public PointF getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }
}
