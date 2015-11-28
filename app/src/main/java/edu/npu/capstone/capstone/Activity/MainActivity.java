package edu.npu.capstone.capstone.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.npu.capstone.capstone.AsyncTask.DownloadCompanyTask;
import edu.npu.capstone.capstone.AsyncTask.DownloadContactTask;
import edu.npu.capstone.capstone.AsyncTask.DownloadImageTask;
import edu.npu.capstone.capstone.AsyncTask.DownloadPOITask;
import edu.npu.capstone.capstone.AsyncTask.UploadWebpageTask;
import edu.npu.capstone.capstone.Data.Company;
import edu.npu.capstone.capstone.Data.Contact;
import edu.npu.capstone.capstone.Data.Location;
import edu.npu.capstone.capstone.Data.Poi;
import edu.npu.capstone.capstone.View.BlueDotView;
import edu.npu.capstone.capstone.R;
import edu.npu.capstone.capstone.View.POIView;
import edu.npu.capstone.capstone.View.PathView;

public class MainActivity extends AppCompatActivity {
    public static String ROOM_902 = "f7145683-ee36-4bbf-a33c-1f0db5eb3d88";
    public static String F_308 = "29c9aca2-b0e1-40e7-a031-b8b2526cb982";
    public static String ROOM_503 = "102a2ae7-66dd-4602-83c6-b724713adaed";
    public String floorPlanId = ROOM_503;
    public static final float dotRadius = 0.5f;

    private IALocationManager mIALocationManager;
    private IALocationListener mIALocationListener;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mPendingAsyncResult;
    private IAFloorPlan mFloorPlan;
    private IALocation mLocation;
    private long mTime;
    private Context mContext;
    private PointF mPoint;

    private ArrayList<Company> companyList = new ArrayList<Company>();
    private ArrayList<Poi> poiList = new ArrayList<Poi> ();
    private ArrayList<Contact> contactList = new ArrayList<Contact> ();

    private RelativeLayout mLayout;
    private ImageView mFloorPlanImage;
    private TextView mTextView;
    private BlueDotView preView;
    private POIView prePOIView;
    private PathView prePathView;
    private String id=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capstone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mContext = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManagerCapstone smsManager = new SmsManagerCapstone();
                try {
                    IALatLng latLng = new IALatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    PointF point = mFloorPlan.coordinateToPoint(latLng);

                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String name="";
                    if (contactList!=null) {
                        for (int i = 0; i < contactList.size(); i++) {
                            if (tm.getDeviceId().equals(contactList.get(i).getUdid())) {
                                name = contactList.get(i).getName();
                            }
                        }
                    }
                    String msg = name+"'s location: https://rocky-falls-3529.herokuapp.com/mylocation.html?pId=564d28a58282c911006003b6";


                    smsManager.sendSms(contactList.get(0).getNumber(), msg);
                    Toast.makeText(mContext, "Location shared via SMS!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                } catch (JSONException e) {

                }
            }
        });

        // Creates IndoorAtlas location manager
        mIALocationManager = IALocationManager.create(this);
        if (floorPlanId != null && !floorPlanId.isEmpty()) {
            final IALocation FLOOR_PLAN_ID = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManager.setLocation(FLOOR_PLAN_ID);
        }

        mTextView = (TextView) findViewById(R.id.location);
        mLayout = (RelativeLayout) findViewById(R.id.layout);
        mFloorPlanImage = (ImageView)findViewById(R.id.image);
        mResourceManager = IAResourceManager.create(getApplicationContext());
        mFloorPlanImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                mTextView.setText(x + "," + y);
                if (prePOIView!=null) {
                    mLayout.removeView(prePOIView);
                    prePOIView = null;
                }
                if (prePathView!=null) {
                    mLayout.removeView(prePathView);
                    prePathView = null;
                }
                return true;
            }
        });



        mTime = System.currentTimeMillis()/1000;


        fetchContactInfo();
        fetchCompanyInfo();
        fetchPoiInfo();





        mIALocationListener = new IALocationListener() {
            // Called when the location has changed.
            @Override
            public void onLocationChanged(IALocation location) {
                mLocation = location;
                uploadLocationInfo();
                updateBlueDot();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

        fetchFloorPlan(floorPlanId);





    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capstone, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIALocationManager.removeLocationUpdates(mIALocationListener);
    }

    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        super.onDestroy();
    }

    private void updateBlueDot() {
        if (mFloorPlanImage.getDrawable() != null) {
            IALatLng latLng = new IALatLng(mLocation.getLatitude(), mLocation.getLongitude());
            mPoint = mFloorPlan.coordinateToPoint(latLng);
            float accuracy = mLocation.getAccuracy();
            final BlueDotView view = new BlueDotView(MainActivity.this);
            view.setDotCenter(mPoint);
            view.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);
            view.setAccuracy(mLocation.getAccuracy() * mFloorPlan.getMetersToPixels());
            view.invalidate();
            if (preView != null) {
                mLayout.removeView(preView);
            }
            mLayout.addView(view);
            preView = view;

            float radius = 2f * mFloorPlan.getMetersToPixels();
            float distance = (float) Math.sqrt((mPoint.x - 341)*(mPoint.x - 341) + (mPoint.y - 522)*(mPoint.y - 522));
            if (distance < radius) {
                Log.d("TIME", Long.toString(System.currentTimeMillis()/1000));
                Log.d("TIME", Long.toString(mTime));
                if (System.currentTimeMillis()/1000 - mTime > 10) {
                    Toast.makeText(MainActivity.this, "Please be quiet, you are close to studying room 314.", Toast.LENGTH_LONG).show();
                    mTime = System.currentTimeMillis()/1000;
                }
            }

            float distance2 = (float) Math.sqrt((mPoint.x - 341)*(mPoint.x - 341) + (mPoint.y - 339)*(mPoint.y - 339));
            if (distance2 < radius) {
                Log.d("TIME", Long.toString(System.currentTimeMillis()/1000));
                Log.d("TIME", Long.toString(mTime));
                if (System.currentTimeMillis()/1000 - mTime > 10) {
                    Toast.makeText(MainActivity.this, "Are you thirsty? There is a vendor machine inside", Toast.LENGTH_LONG).show();
                    mTime = System.currentTimeMillis()/1000;
                }
            }
        }
    }


    private void fetchCompanyInfo() {
        // Gets the URL from the UI's text field.
        String stringUrl = "https://rocky-falls-3529.herokuapp.com/company";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadCompanyTask(mTextView, companyList).execute(stringUrl);
        } else {
            mTextView.setText("No network connection available.");
        }
    }

    private void fetchContactInfo() {
        // Gets the URL from the UI's text field.
        String stringUrl = "https://rocky-falls-3529.herokuapp.com/person";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadContactTask(mTextView, contactList).execute(stringUrl);
        } else {
            mTextView.setText("No network connection available.");
        }
        for (int i=0;i<contactList.size();i++) {
            Log.d("Contact",contactList.get(i).getNumber());
        }
    }

    private void fetchPoiInfo() {
        // Gets the URL from the UI's text field.
        String stringUrl = "https://rocky-falls-3529.herokuapp.com/poi";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadPOITask(mTextView, poiList).execute(stringUrl);
        } else {
            mTextView.setText("No network connection available.");
        }
        for (int i=0;i<poiList.size();i++) {
            for (int j=0;j<companyList.size();j++) {
                if (poiList.get(i).getCompany().equals(companyList.get(j).getId())) {
                    poiList.get(i).setCompany(companyList.get(j).getName());
                    Log.d("Company id -> name",poiList.get(i).getName());
                }
            }
        }
    }

    private void uploadLocationInfo() {
        String stringUrl = "https://rocky-falls-3529.herokuapp.com/location";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (mLocation != null) {
                IALatLng latLng = new IALatLng(mLocation.getLatitude(), mLocation.getLongitude());
                PointF point = mFloorPlan.coordinateToPoint(latLng);

                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                if (contactList!=null) {
                    for (int i = 0; i < contactList.size(); i++) {
                        if (tm.getDeviceId().equals(contactList.get(i).getUdid())) {
                            id = contactList.get(i).getId();
                        }
                    }
                }
                new UploadWebpageTask(new Location(point, id)).execute(stringUrl);
            }
        } else {
            mTextView.setText("No network connection available.");
        }
    }

    // fetches floor plan data from IndoorAtlas server
    private void fetchFloorPlan(String id) {
        final IATask<IAFloorPlan> asyncResult = mResourceManager.fetchFloorPlanWithId(id);
        mPendingAsyncResult = asyncResult;
        if (mPendingAsyncResult != null) {
            mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                @Override
                public void onResult(IAResult<IAFloorPlan> result) {
                    Log.d("MAIN", "fetch floor plan result:" + result);
                    if (result.isSuccess() && result.getResult() != null) {
                        mFloorPlan = result.getResult();
                        new DownloadImageTask(mFloorPlanImage).execute(mFloorPlan.getUrl());
                    } else {
                        // do something with error
                    }
                }
            }, Looper.getMainLooper()); // deliver callbacks in main thread
        }
    }

    public void search(MenuItem item){
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Search");
// Set an EditText view to get user input
        final EditText input = new EditText(mContext);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showResultList(input.getText().toString());
                Log.d("SHOW RESULT", input.getText().toString());
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void share(MenuItem item){
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Share");
// Set an EditText view to get user input
        final EditText input = new EditText(mContext);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void showResultList(String str) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        //builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.select_dialog_singlechoice);

        final ArrayList<Poi> resultList = new ArrayList<Poi>();
        for (int i=0; i<poiList.size(); i++) {

            if (poiList.get(i).getName().contains(str) ||
                    poiList.get(i).getCompany().contains(str) ||
                    poiList.get(i).getCategory().contains(str)) {
                resultList.add(poiList.get(i));
            }
        }

        for (int i=0; i<resultList.size(); i++) {
            arrayAdapter.add(resultList.get(i).getName());
        }

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final POIView view = new POIView(mContext);
                        view.setDotCenter(resultList.get(which).getLocation());
                        view.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);
                        view.invalidate();
                        if (prePOIView == null) {
                            mLayout.addView(view);
                            prePOIView = view;
                        } else {
                            mLayout.removeView(prePOIView);
                            mLayout.addView(view);
                            prePOIView = view;
                        }
                        Toast.makeText(mContext, resultList.get(which).getName(), Toast.LENGTH_SHORT).show();

                        final PathView pathView = new PathView(mContext,mPoint,resultList.get(which).getLocation());
                        pathView.invalidate();
                        if (prePathView == null) {
                            mLayout.addView(pathView);
                            prePathView = pathView;
                        } else {
                            mLayout.removeView(prePathView);
                            mLayout.addView(pathView);
                            prePathView = pathView;
                        }

                    }
                });
        builderSingle.show();
    }
}
