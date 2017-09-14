package me.vaaiibhav.www.vndsms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    EditText etSearcher;
    TextView tvresult;
    RequestQueue rqsearch;
    String url;
    Button Searcher;
    Constants cs;
    SharedPreferences spr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        etSearcher = (EditText) findViewById(R.id.et_searchNumber);
        tvresult = (TextView) findViewById(R.id.tv_searcher);
        tvresult.setText("Welcome:");
        cs= new Constants();
        spr = getApplicationContext().getSharedPreferences("userloginPref",0);
        Searcher= (Button)findViewById(R.id.btn_searchPhone);
        Searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneSearch = etSearcher.getText().toString();
                if(phoneSearch.isEmpty()){
                    Toast.makeText(SearchActivity.this, "Enter Number to Search", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(cs.TAG,phoneSearch);
                    rqsearch = Volley.newRequestQueue(SearchActivity.this);
                    findBumberfromServer(phoneSearch);

                }
            }
        });
}

    public void findBumberfromServer(final String s) {
        String user=spr.getString(cs.PREF_USERNAME,"");
        Log.d(cs.TAG,"Sending Request");
        String searcherulr=String.format("https://vndinfomedia.com/SmsApi/selector.php?number=%1$s&user=%2$s",s,user);
        Log.d(cs.TAG,cs.SEARCHLINK);
        StringRequest sr = new StringRequest(Request.Method.GET,searcherulr , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().isEmpty()){
                    Toast.makeText(SearchActivity.this, response, Toast.LENGTH_SHORT).show();
                    rqsearch.stop();

                    tvresult.setText(response);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                rqsearch.stop();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
               // map.put(cs.NUMBER,s);


                return  map;

            }
        }; rqsearch.add(sr);
    }
}
