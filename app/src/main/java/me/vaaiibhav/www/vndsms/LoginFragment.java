package me.vaaiibhav.www.vndsms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginFragment extends Fragment {


    EditText etuname, etpass;
    String etName,etPass,n, newlink;
    Button loginButton;
    Constants cs;
    SharedPreferences sp ;
    public LoginFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_login, container, false);
        sp = getActivity().getSharedPreferences("userloginPref",0);
        n = DataHolderClass.getInstance().getSerialNo();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
    etuname = (EditText) view.findViewById(R.id.et_Username);
    etpass = (EditText)view.findViewById(R.id.et_Password);
    loginButton= (Button)view.findViewById(R.id.btn_login);
    cs = new Constants();
    loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etName=etuname.getText().toString();
            etPass = etpass.getText().toString();
           if(!etName.isEmpty() && !etPass.isEmpty()){
               //Log.d(cs.TAG,"username: "+ etName+" Pass: "+etPass+ " serial: "+n);
                loginProcessor(etName, etPass);

               newlink=String.format("https://vndinfomedia.com/SmsApi/userlogin.php?username=%1$s&password=%2$s&serialno=%3$s",etName,etPass,n);
             //  newlink=String.format("https://vndinfomedia.com/SmsApi/userlogin.php?username=vaibhav&password=pass123&serialno=8991900992079275161f");
              // String searcherulr=String.format("https://vndinfomedia.com/SmsApi/selector.php?number=%1$s",s);
               Log.d(cs.TAG,newlink);
            }
            else{}
        }
    });
    }

    private void loginProcessor(final String etName, final String etPass) {
        final RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest srq = new StringRequest(Request.Method.GET, newlink, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(cs.TAG,response);
               if( response.trim().equals("gotit")){
                SharedPreferences.Editor sped = sp.edit();
                   sped.putString(cs.PREF_USERNAME,etName);
                   sped.putString(cs.PREF_PASSWORD,etPass);
                   sped.commit();
                   gotoSearch();
               }
               rq.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d(cs.TAG,error.toString());
                rq.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                // map.put(cs.NUMBER,s);


                return  map;

            }
        }; rq.add(srq);
        }

    private void gotoSearch() {
        Intent searcher = new Intent(getActivity(),SearchActivity.class);
        startActivity(searcher);
    }


}
