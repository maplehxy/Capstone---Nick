package edu.npu.capstone.capstone.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.npu.capstone.capstone.Data.Location;

/**
 * Created by Nick on 11/2/15.
 */
public class UploadWebpageTask extends AsyncTask<String, Void, String> {
    Location location;

    public UploadWebpageTask(Location location) {
        this.location = location;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return uploadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to post web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Log.d("POST LOC", result);
    }

    public String uploadUrl(String url) throws IOException {
        HttpsURLConnection httpcon;
        String data = jsonToString();
        String result = null;

        try{
            //Connect
            httpcon = (HttpsURLConnection) ((new URL (url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();

            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Unsupported Encoding Exceoption";
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        }
    }

    private String jsonToString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("LocX", location.getLocation().x);
            obj.put("LocY", location.getLocation().y);
            obj.put("_person", location.getId());
        } catch (JSONException e) {
            Log.d("POST", "Wrong data format");
        }
        return obj.toString();
    }
}