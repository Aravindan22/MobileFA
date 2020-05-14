package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText mregno , mpassw;
    TextView mReg;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        btn = findViewById(R.id.login_btn);
        mregno =  findViewById(R.id.reg_edit_text_log);
        mpassw = findViewById(R.id.pass_edit_text_log);
        mReg = findViewById(R.id.reg_text_view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterUser.class));
                finish();
            }
        });


    }
//    private void  registerNewuser(){
//        final String regno ="13263838";
//        final String passw ="htgqwtgsbns";
//        final String dep = "IYT";
//
//
//        String url = Constants.REGISTER_URL;
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if(response.equals("Registered")){
//                    Toast.makeText(MainActivity.this, "Registered", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("regno", regno);
//                params.put("password", passw);
//                params.put("dep", dep);
//                return params;
//            }
//        };
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
//    }

    private void loginUser(){
        final String regno = mregno.getText().toString().trim();
        final String passw =mpassw.getText().toString().trim();

        String url = Constants.LOGIN_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Logged in")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getResources().getString(R.string.prefLoginState),"Logged in");
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    Constants.regno = regno;
                    startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("regno", regno);
                params.put("password", passw);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
