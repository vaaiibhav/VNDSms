package me.vaaiibhav.www.vndsms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {
    SharedPreferences userloginPrefs;
    String Username, n;
    RequestQueue rq;
    Constants cs;
    TextView tvserialno;
    SharedPreferences.Editor speditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
                cs = new Constants();
        tvserialno=(TextView)findViewById(R.id.tv_SerialNo);

        userloginPrefs =getApplicationContext().getSharedPreferences("userloginPref",0);
        Username= userloginPrefs.getString(cs.PREF_USERNAME,"");
       // speditor = userloginPrefs.edit();


        checkAndRequestPermissions();
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
         n = tm.getSimSerialNumber();
       // n="123";
        DataHolderClass.getInstance().setSerialNo(n);
        if(!n.isEmpty()){        tvserialno.setText("UCode: "+n);}

        rq = Volley.newRequestQueue(LoginActivity.this);

        if (!Username.isEmpty()){

            netlogger();
        }
        else {
            Fragment fraglogin = new LoginFragment();
            FragmentManager fr = getSupportFragmentManager();
            FragmentTransaction ft = fr.beginTransaction();
             ft.replace(R.id.loggerScreen,fraglogin);
            ft.commit();


        }
    }

    private void netlogger() {
        String prefname =userloginPrefs.getString(cs.PREF_USERNAME,"");
        String prefpass =userloginPrefs.getString(cs.PREF_PASSWORD,"");
      String newlink=String.format("https://vndinfomedia.com/SmsApi/userlogin.php?username=%1$s&password=%2$s&serialno=%3$s",prefname,prefpass,n);
        StringRequest sr = new StringRequest(Request.Method.GET, newlink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("gotit")){
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    rq.stop();
                    Intent Searchactivity = new Intent(LoginActivity.this,SearchActivity.class);
                    startActivity(Searchactivity);

                }
                else{
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    Fragment fraglogin = new LoginFragment();
                    FragmentManager fr = getSupportFragmentManager();
                    FragmentTransaction ft = fr.beginTransaction();
                    ft.replace(R.id.loggerScreen,fraglogin);
                    ft.commit();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error Logging in", Toast.LENGTH_SHORT).show();
                rq.stop();
                Fragment fraglogin = new LoginFragment();
                FragmentManager fr = getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                ft.replace(R.id.loggerScreen,fraglogin);
                ft.commit();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
//                map.put(cs.KEY_PASSWORD,userloginPrefs.getString(cs.PREF_PASSWORD,""));
  //              map.put(cs.KEY_SERIALNUMBER,n);
                return  map;

            }
        }; rq.add(sr);
        Log.d(cs.TAG,sr.toString());
    }

    private  boolean checkAndRequestPermissions() {
        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),1);
            return false;
        }
        return true;
    }
}
