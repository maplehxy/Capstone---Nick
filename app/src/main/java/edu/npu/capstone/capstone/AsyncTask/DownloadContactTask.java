package edu.npu.capstone.capstone.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import edu.npu.capstone.capstone.Data.Company;
import edu.npu.capstone.capstone.Data.Contact;

/**
 * Created by Nick on 11/2/15.
 */
// Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
public class DownloadContactTask extends AsyncTask<String, Void, String> {
    TextView webText;
    ArrayList<Contact> list;

    public DownloadContactTask(TextView webText, ArrayList<Contact> list) {
        this.webText = webText;
        this.list = list;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        jsonToArray(result);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("GET CONTACT", "The response is: " + response);
            is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private void jsonToArray(String string) {
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {

                String id = jsonArray.getJSONObject(i).getString("_id");
                String number = jsonArray.getJSONObject(i).getString("MobileNumber");
                String udid = jsonArray.getJSONObject(i).getString("MobileUniqueID");
                String firstName = jsonArray.getJSONObject(i).getString("Firstname");
                String lastName = jsonArray.getJSONObject(i).getString("Lastname");
                Contact contact = new Contact(id, number,udid,firstName,lastName);
                list.add(contact);

                Log.d("GET CONTACT", contact.getNumber());

            }
        } catch (JSONException e) {
            Log.d("GET CONTACT", "Wrong data format");
        }
    }
}