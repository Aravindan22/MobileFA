package com.example.android.mobilefa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btn;
    String regno;
    EditText mregno, mpassw;
    TextView mReg;
    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wakeBackend();
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        btn = findViewById(R.id.activity_main_login_button);
        mregno = findViewById(R.id.activity_main_regno_edittext);
        mpassw = findViewById(R.id.activity_main_password_edittext);
        mReg = findViewById(R.id.activity_main_newuser_textview);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.startProgress();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog !=null){
                            progressDialog.dismissProgress();
                        }
                    }
                }, 1500);
                loginUser();
            }
        });

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterUser.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPreferences.getBoolean("LOGGED_IN", false)) {
            Log.d("STORING DETAILS : ", sharedPreferences.getAll().toString());
            startActivity(new Intent(this, ChoosingActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog !=null){
            progressDialog.dismissProgress();
            progressDialog=null;
        }
    }

    protected boolean Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s)) {
            et.setError("This field cannot be empty!");
            return true;
        }
        return false;
    }

    private void loginUser() {
        regno = mregno.getText().toString().trim();
        final String passw = mpassw.getText().toString().trim();

        if(!(Empty(regno, mregno)) && !(Empty(passw, mpassw))){

            StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Logged in")) {
                        Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                        getDetails();
                        startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                        Log.d("Not Crt", response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Volley Error", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("regno", regno);
                    params.put("password", passw);
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }

    private void wakeBackend(){
        Log.d("Waking Backend server","");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            Log.d("Wake response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getDetails() {
        fetchData f = new fetchData();
        f.getCreds(regno, getApplicationContext());
        Log.d("STORING DETAILS : ", sharedPreferences.getAll().toString());
    }
}
