package edu.npu.capstone.capstone.AsyncTask;

import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import edu.npu.capstone.capstone.Data.Poi;

/**
 * Created by Nick on 11/2/15.
 */
// Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
public class DownloadPOITask extends AsyncTask<String, Void, String> {
    TextView webText;
    ArrayList<Poi> list;

    public DownloadPOITask(TextView webText, ArrayList<Poi> list) {
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
        Log.d("GET", result);
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
            Log.d("GET", "The response is: " + response);
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
            string = "[{\"_id\":\"564d12d7811c7e1100e5d0ad\",\"Name\":\"Windows Phone\",\"Category\":\"Phone\",\"LocX\":185,\"LocY\":267,\"_company\":\"564d108e811c7e1100e5d0a7\",\"__v\":0},{\"_id\":\"564d1303811c7e1100e5d0b3\",\"Name\":\"Microsoft Office\",\"Category\":\"Application\",\"LocX\":292,\"LocY\":213,\"_company\":\"564d108e811c7e1100e5d0a7\",\"__v\":0},{\"_id\":\"564d1313811c7e1100e5d0b4\",\"Name\":\"XBox\",\"Category\":\"Games\",\"LocX\":335,\"LocY\":398,\"_company\":\"564d108e811c7e1100e5d0a7\",\"__v\":0},{\"_id\":\"564d1567a9329f110033a49a\",\"Name\":\"Bing\",\"Category\":\"Search\",\"LocX\":253,\"LocY\":572,\"_company\":\"564d108e811c7e1100e5d0a7\",\"__v\":0},{\"_id\":\"564d15b0a9329f110033a49b\",\"Name\":\"iPhone 6S\",\"Category\":\"Phone\",\"LocX\":400,\"LocY\":235,\"_company\":\"564d10c3811c7e1100e5d0a8\",\"__v\":0},{\"_id\":\"564d1627a9329f110033a49c\",\"Name\":\"Apple Store\",\"Category\":\"Application\",\"LocX\":387,\"LocY\":419,\"_company\":\"564d10c3811c7e1100e5d0a8\",\"__v\":0},{\"_id\":\"564d1651a9329f110033a49d\",\"Name\":\"Facebook Games\",\"Category\":\"Games\",\"LocX\":446,\"LocY\":470,\"_company\":\"564d1105811c7e1100e5d0a9\",\"__v\":0},{\"_id\":\"564d17afa9329f110033a49e\",\"Name\":\"Facebook Search\",\"Category\":\"Search\",\"LocX\":368,\"LocY\":695,\"_company\":\"564d1105811c7e1100e5d0a9\",\"__v\":0},{\"_id\":\"564d17e1a9329f110033a49f\",\"Name\":\"Google Search\",\"Category\":\"Search\",\"LocX\":542,\"LocY\":221,\"_company\":\"564d1212811c7e1100e5d0ac\",\"__v\":0},{\"_id\":\"564d1810a9329f110033a4a0\",\"Name\":\"Android Phone\",\"Category\":\"Phone\",\"LocX\":544,\"LocY\":409,\"_company\":\"564d1212811c7e1100e5d0ac\",\"__v\":0},{\"_id\":\"564d182ca9329f110033a4a1\",\"Name\":\"Amazon Search\",\"Category\":\"Search\",\"LocX\":632,\"LocY\":455,\"_company\":\"564d1163811c7e1100e5d0aa\",\"__v\":0},{\"_id\":\"564d1850a9329f110033a4a2\",\"Name\":\"Amazon Fire Phone\",\"Category\":\"Phone\",\"LocX\":543,\"LocY\":605,\"_company\":\"564d1163811c7e1100e5d0aa\",\"__v\":0},{\"_id\":\"564d18c6a9329f110033a4a3\",\"Name\":\"Lobby\",\"Category\":\"Utility\",\"LocX\":797,\"LocY\":119,\"_company\":\"564d11c9811c7e1100e5d0ab\",\"__v\":0},{\"_id\":\"564d18d0a9329f110033a4a4\",\"Name\":\"VIP\",\"Category\":\"Utility\",\"LocX\":697,\"LocY\":348,\"_company\":\"564d11c9811c7e1100e5d0ab\",\"__v\":0},{\"_id\":\"564d18dda9329f110033a4a5\",\"Name\":\"Dinning Area\",\"Category\":\"Utility\",\"LocX\":697,\"LocY\":414,\"_company\":\"564d11c9811c7e1100e5d0ab\",\"__v\":0},{\"_id\":\"5658f69f727656110087f96c\",\"Name\":\"Room 311\",\"Category\":\"Room\",\"LocX\":237,\"LocY\":267,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f6bf727656110087f992\",\"Name\":\"Room 527\",\"Category\":\"Room\",\"LocX\":325,\"LocY\":330,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f711727656110087f9ee\",\"Name\":\"Women's Restroom\",\"Category\":\"Room\",\"LocX\":466,\"LocY\":242,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f722727656110087fa02\",\"Name\":\"Men's Restroom\",\"Category\":\"Room\",\"LocX\":568,\"LocY\":256,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f745727656110087fa2d\",\"Name\":\"Men's Restroom\",\"Category\":\"Room\",\"LocX\":234,\"LocY\":501,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f751727656110087fa3b\",\"Name\":\"Women's Restroom\",\"Category\":\"Room\",\"LocX\":241,\"LocY\":547,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f77c727656110087fa6e\",\"Name\":\"Room 312\",\"Category\":\"Room\",\"LocX\":231,\"LocY\":383,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5658f7a3727656110087fa9b\",\"Name\":\"Room 530\",\"Category\":\"Room\",\"LocX\":702,\"LocY\":138,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659e8868735771100fb97a2\",\"Name\":\"Room 523\",\"Category\":\"Room\",\"LocX\":404,\"LocY\":495,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659e91e8735771100fb9854\",\"Name\":\"Room 304\",\"Category\":\"Room\",\"LocX\":135,\"LocY\":651,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659e9c38735771100fb9928\",\"Name\":\"Room 308\",\"Category\":\"Room\",\"LocX\":112,\"LocY\":330,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659e9d58735771100fb9941\",\"Name\":\"Room 309\",\"Category\":\"Room\",\"LocX\":146,\"LocY\":330,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659e9f88735771100fb996a\",\"Name\":\"Room 310\",\"Category\":\"Room\",\"LocX\":172,\"LocY\":281,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea078735771100fb997c\",\"Name\":\"Room 307\",\"Category\":\"Room\",\"LocX\":108,\"LocY\":387,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea158735771100fb9987\",\"Name\":\"Room 306\",\"Category\":\"Room\",\"LocX\":117,\"LocY\":473,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea298735771100fb99a5\",\"Name\":\"Room 305\",\"Category\":\"Room\",\"LocX\":143,\"LocY\":580,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea3d8735771100fb99bc\",\"Name\":\"Room 303\",\"Category\":\"Room\",\"LocX\":204,\"LocY\":627,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea5f8735771100fb99e4\",\"Name\":\"Room 302\",\"Category\":\"Room\",\"LocX\":255,\"LocY\":597,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea7d8735771100fb9a09\",\"Name\":\"Room 301\",\"Category\":\"Room\",\"LocX\":266,\"LocY\":663,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ea948735771100fb9a22\",\"Name\":\"Room 316\",\"Category\":\"Room\",\"LocX\":330,\"LocY\":585,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659eaac8735771100fb9a49\",\"Name\":\"Room 315\",\"Category\":\"Room\",\"LocX\":352,\"LocY\":550,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659eacc8735771100fb9a70\",\"Name\":\"Room 314\",\"Category\":\"Room\",\"LocX\":330,\"LocY\":512,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659eb058735771100fb9ab5\",\"Name\":\"Room 520\",\"Category\":\"Room\",\"LocX\":418,\"LocY\":362,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659eca18735771100fb9cac\",\"Name\":\"Room 522\",\"Category\":\"Room\",\"LocX\":431,\"LocY\":462,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659ecb88735771100fb9ccf\",\"Name\":\"Room 521\",\"Category\":\"Room\",\"LocX\":474,\"LocY\":458,\"_company\":\"5658f55e727656110087f909\",\"__v\":0},{\"_id\":\"5659eccb8735771100fb9ce8\",\"Name\":\"Room 519\",\"Category\":\"Room\",\"LocX\":466,\"LocY\":392,\"_company\":\"5658f55e727656110087f909\",\"__v\":0}]";


            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                String id = jsonArray.getJSONObject(i).getString("_id");
                String name = jsonArray.getJSONObject(i).getString("Name");
                float locX = Float.parseFloat(jsonArray.getJSONObject(i).getString("LocX"));
                float locY = Float.parseFloat(jsonArray.getJSONObject(i).getString("LocY"));
                PointF location = new PointF (locX, locY);
                String company = jsonArray.getJSONObject(i).getString("_company");
                String category = jsonArray.getJSONObject(i).getString("Category");
                Poi poi = new Poi(id, name, category, company,location);
                list.add(poi);
                Log.d("GET POI", poi.getName()+" | "+poi.getCategory()+" | "+poi.getCompany());
            }
        } catch (JSONException e) {
            Log.d("GET POI", "Wrong data format");
        }
    }
}