package com.example.ishanvadwala.cmpe_273;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    boolean bleStatus = true;
    Button sendButton;
    String className;
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    private ScanFilter mScanFilter;
    private ScanSettings mScanSettings;
    private TextView BLEStatus,signOut;
    FloatingActionButton FAB;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
int REQUEST_ENABLE_BT;
    ScanCallback mScanCallback;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        REQUEST_ENABLE_BT=1;
      //  className  ="273";
        sharedpreferences = MainActivity.this.getSharedPreferences("Hello", Context.MODE_PRIVATE);
       editor=sharedpreferences.edit();
        Log.d("MainAct","GoestoMainFirst");
        Log.d("SharedPreferences",sharedpreferences.getString("SessionEnabled","NotTrue"));
        try{
        if(sharedpreferences.getString("SessionEnabled",null).equals(null)) {
            Intent Registration = new Intent(this, RegisterStudent.class);
            startActivity(Registration);
        }
        }catch (Exception E){}
if(sharedpreferences.getString("SessionEnabled",null)!=null){
    Log.d("SharedPreferences",sharedpreferences.getString("SessionEnabled","NotTrue"));

}else{
    Intent Registration = new Intent(this, RegisterStudent.class);
    startActivity(Registration);
}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        BLEStatus=(TextView)findViewById(R.id.DisplayText);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mScanCallback = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                ScanRecord mScanRecord = result.getScanRecord();

                List<ADStructure> structures =
                        ADPayloadParser.getInstance().parse(mScanRecord.getBytes());
                for (ADStructure structure : structures)
                {

                    // If the ADStructure instance can be cast to IBeacon.
                    if (structure instanceof IBeacon)
                    {
                        // An iBeacon was found.
                        String localClassName=volleyGetClass();
                        IBeacon iBeacon = (IBeacon)structure;
                            Log.d("UUID", iBeacon.getUUID().toString());
                        BLEStatus.setText("Raspberry Detected.\n You are in Class: "+localClassName);
                        sendButton.setVisibility(View.VISIBLE);
                        sendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 VolleySendAttendance(v);

                                }
                        });
                    }              /*  mBluetoothAdapter.getAddress();
                mScanRecord.getDeviceName();
             byte[] manufacturerData = mScanRecord.getBytes();
                int mRssi = result.getRssi();
                    Log.d("FML", bytesToHex(manufacturerData));
            */}
        }};
        signOut = (TextView)findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SessionEnabled", null);
                editor.commit();
                volleyDelete(v);
              //  finish();
            }
        });
        sendButton =(Button)findViewById(R.id.clicker);
        FAB = (FloatingActionButton)findViewById(R.id.scan);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bleStatus) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }else{
                    AnimationSet set = new AnimationSet(false);
                        set.addAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.pulse));
                        FAB.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_cancel_white_24dp));
                    FAB.startAnimation(set);
                    bleStatus=false;
                    StartBLEScan(v);}
                }else{
                    FAB.setAnimation(null);
                    FAB.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_searching_white_48dp));
                    Log.d("INSIDE FALSE", "DOES THIS WORK?");
                    sendButton.setVisibility(View.GONE);
                 //   FAB.setAnimation(null);
                    StopBLEScan(v);
                }

            }
        });
        setScanSettings();
       setScanFilter();
    }
    private void setScanFilter() {
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();
        try{
        for(int i=0;i<sharedpreferences.getInt("Length",2);i++) {
            mBuilder.setDeviceAddress(sharedpreferences.getString("MAC"+i,""));
            Log.d("Added to Filter",sharedpreferences.getString("MAC"+i,""));
        }}catch (Exception E){E.printStackTrace();}
//        mBuilder.setManufacturerData()
        mScanFilter = mBuilder.build();
    }
    private void setScanSettings() {
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();
        mBuilder.setReportDelay(0);
        mBuilder.setScanMode(ScanSettings.SCAN_MODE_BALANCED);
        mScanSettings = mBuilder.build();
    }
public void StartBLEScan(View view){
    mBluetoothLeScanner.startScan(Arrays.asList(mScanFilter), mScanSettings, mScanCallback);
    Snackbar s1 = Snackbar.make(view, "Bluetooth Scanning", Snackbar.LENGTH_LONG);
    s1.show();
}
    public void StopBLEScan(View view){
        bleStatus = true;
        Log.d("STOPBLE","Does this even work?");
        Snackbar s1 = Snackbar.make(view, "Stopped Bluetooth Scanning", Snackbar.LENGTH_LONG);
        s1.show();
        mBluetoothLeScanner.stopScan(mScanCallback);
        mBluetoothAdapter.cancelDiscovery();
    }
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


public void VolleySendAttendance(final View v){
    String URL="http://Go-Load-Balanacer-1907013323.us-west-1.elb.amazonaws.com:3001/markpresent/"+sharedpreferences.getString("StudentId","")+"/"
            +sharedpreferences.getString("deviceid", "1993")+"/"+className;
    Log.d("MarkAttendance",URL);
        JSONObject jj = new JSONObject();
        JsonObjectRequest volleyStringRequest = new JsonObjectRequest(Request.Method.POST, URL, jj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        Log.d("Goes to Response", response.toString());
                    Snackbar s1 = Snackbar.make(v, "Attendance Logged", Snackbar.LENGTH_LONG);
                    s1.show();
                    sendButton.setVisibility(View.GONE);
                } catch (Exception E) {
                    Snackbar s1 = Snackbar.make(v, "Error occured", Snackbar.LENGTH_LONG);
                    s1.show();
                    Log.d("EXCEPTION", E.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROROROR", error.toString());

            }
        }) {


        };
        AppController.getInstance().addToRequestQueue(volleyStringRequest, "REQ");
    }

    public void volleyDelete(final View v){
        String URL="http://Go-Load-Balanacer-1907013323.us-west-1.elb.amazonaws.com:3001/deletestudent/"+Integer.parseInt(sharedpreferences.getString("StudentId", ""))+"/"
                +sharedpreferences.getString("StudentPass", "1993");
        Log.d("This is the DeviceId",sharedpreferences.getString("deviceid","LALALLAL"));
        Log.d("MarkAttendance",URL);
        JSONObject jj = new JSONObject();
        JsonObjectRequest volleyStringRequest = new JsonObjectRequest(Request.Method.DELETE, URL, jj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Goes to Response", response.toString());
                    Snackbar s1 = Snackbar.make(v, "Attendance Logged", Snackbar.LENGTH_LONG);
                    s1.show();
                    sendButton.setVisibility(View.GONE);
                    Intent restart = new Intent(getApplicationContext(),RegisterStudent.class);
                    startActivity(restart);

                } catch (Exception E) {
                    Snackbar s1 = Snackbar.make(v, "Error occured", Snackbar.LENGTH_LONG);
                    s1.show();
                    Log.d("EXCEPTION", E.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROROROR", error.toString());

            }
        }) {


        };
        AppController.getInstance().addToRequestQueue(volleyStringRequest, "REQ");
    }
    public String volleyGetClass(){
        //default val
      //  final String[] classArray = {"273"};
        String URL= "http://Go-Load-Balanacer-1907013323.us-west-1.elb.amazonaws.com:3001/studentenrolled/"+Integer.parseInt(sharedpreferences.getString("StudentId", ""));
                Log.d("getClass",URL);
        JSONObject jj = new JSONObject();
        JsonObjectRequest volleyStringRequest = new JsonObjectRequest(Request.Method.DELETE, URL, jj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                  className =  response.getJSONArray("value").get(0).toString();
              } catch (Exception E) {
                    Log.d("EXCEPTION", E.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROROROR", error.toString());

            }
        }) {


        };
        AppController.getInstance().addToRequestQueue(volleyStringRequest, "REQ");
        return className;
    }
}

