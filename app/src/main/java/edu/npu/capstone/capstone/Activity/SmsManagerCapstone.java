package edu.npu.capstone.capstone.Activity;

import android.telephony.SmsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Nick on 11/18/15.
 */
public class SmsManagerCapstone {
    private SmsManager smsManager;
    private final String serviceUrl = "https://rocky-falls-3529.herokuapp.com/person/";

    public SmsManagerCapstone() {
    }

    public void sendSms(String number, String msg) throws IOException, JSONException {
        smsManager = SmsManager
                .getDefault();
        ArrayList<String> contents = smsManager
                .divideMessage(msg);
        for (String str : contents) {
            smsManager.sendTextMessage(
                    number, null, str,
                    null, null);
        }
    }
}
