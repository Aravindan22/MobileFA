package com.example.android.mobilefa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        btn = findViewById(R.id.activity_main_login_button);
        mregno = findViewById(R.id.activity_main_regno_edittext);
        mpassw = findViewById(R.id.activity_main_password_edittext);
        mReg = findViewById(R.id.activity_main_newuser_textview);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        //startActivity(new Intent(this, ChoosingActivity.class));
        if (sharedPreferences.getBoolean("LOGGED_IN", false)) {
            startActivity(new Intent(this, ChoosingActivity.class));
            finish();
        }
    }

    private void loginUser() {
        regno = mregno.getText().toString().trim();
        final String passw = mpassw.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Logged in")) {
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    getDetails();
                    startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Something", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    Log.d("Not Crt", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Volley Error", error.toString());
            }
        }) {
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

    private void getDetails() {
        fetchData f = new fetchData();
        f.getCreds(regno, getApplicationContext());
        storeDetails();
    }

    private void storeDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGGED_IN", true);
        editor.putInt("REG_NO", Constants.REG_NO);
        editor.putString("NAME", Constants.NAME);
        editor.putString("DEP", Constants.DEP);
        editor.putInt("YEAR", Constants.YEAR);
        editor.putString("SECTION", Constants.SECTION);
        editor.putString("EMAIL", Constants.EMAIL);
        editor.putString("PASSWORD", Constants.PASSWORD);
        editor.commit();
        Toast.makeText(this, String.valueOf(sharedPreferences.getString("EMAIL", null)), Toast.LENGTH_SHORT).show();
    }
}
