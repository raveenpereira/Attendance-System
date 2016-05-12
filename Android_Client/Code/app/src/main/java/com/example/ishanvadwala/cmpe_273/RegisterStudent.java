package com.example.ishanvadwala.cmpe_273;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ishanvadwala on 5/7/16.
 */
public class RegisterStudent extends AppCompatActivity {
    Button Register;
    private SharedPreferences sharedpreferences;
    MaterialEditText metId,metPass;
    Snackbar s1;
    String URL;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = RegisterStudent.this.getSharedPreferences("Hello", Context.MODE_PRIVATE);
        editor=sharedpreferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editor.putString("LoggedIn","false");
        editor.commit();
        Initialize();
    }
    public void Initialize(){
        metId =(MaterialEditText)findViewById(R.id.StudentId);
        metPass=(MaterialEditText)findViewById(R.id.StudentPass);
        Register = (Button)findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((metId.getText().toString().equals("")||(metPass.getText().toString().equals("")))) {

                        Snackbar sx = Snackbar.make(v, "Empty Fields not allowed!", Snackbar.LENGTH_SHORT);
                    sx.show();
                    Log.d("This does not execute", "FAAAAAAAAAAa");

                }else if ((metId.getText().toString().length()<4)||(metPass.getText().length()<3)){


                    Snackbar sx = Snackbar.make(v, "Length less than 3. Try again!", Snackbar.LENGTH_SHORT);
                    sx.show();

                }else{
                    Log.d("LENLENLEN",String.valueOf(metId.getText().toString().length()));
                    URL = "http://Go-Load-Balanacer-1907013323.us-west-1.elb.amazonaws.com:3001/registerstudent/" + metId.getText().toString() + "/" + metPass.getText().toString();
                    SendVolleyRequest(v);
                    Log.d("This is the URL", URL);

                }
            }
        });
    }
    public void SendVolleyRequest(View view){
       final View vu = view;
        JSONObject jj = new JSONObject();
        JsonObjectRequest volleyStringRequest = new JsonObjectRequest(Request.Method.POST, URL, jj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("NewResponse", response.toString());
                try {
                    try{
                    if(response.get("error")!=null){
                         s1 = Snackbar.make(vu, response.get("error").toString(), Snackbar.LENGTH_LONG);
                            s1.show();
                    }}catch (Exception E){E.printStackTrace();}
                    try{
                    if(response.get("deviceid").toString()!=null){
                        editor.putString("deviceid", response.get("deviceid").toString());
                        JSONArray bluetoothid = response.getJSONArray("bluetoothids");
                        Log.d("BluetoothArray", bluetoothid.toString());
                        Log.d("DEVICEid",response.get("deviceid").toString());
                        editor.putInt("Length",bluetoothid.length());
                        for(int i=0;i<bluetoothid.length();i++){
                            editor.putString("MAC"+i,bluetoothid.get(i).toString());
                            Log.d("MAC"+i,bluetoothid.get(i).toString());
                        }
                        editor.putString("StudentPass",metPass.getText().toString());
                        editor.putString("StudentId",metId.getText().toString());
                        editor.putString("SessionEnabled", "true");
                        editor.commit();
                        Log.d("RegisterShared",sharedpreferences.getString("SessionEnabled","OnThis"));
                        Intent Main = new Intent(RegisterStudent.this, MainActivity.class);
                        startActivity(Main);
                        finish();
                    }}catch (Exception E){E.printStackTrace();}
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
    }
}
