package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    EditText mRegNo,mPassword;
    Button mresetBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mRegNo = findViewById(R.id.activity_reset_password_regno_edittext);
        mPassword = findViewById(R.id.activity_reset_password_pass_edittext);
        mresetBtn = findViewById(R.id.activity_reset_password_reset_button);

        mresetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });
    }
    protected boolean Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s)) {
            et.setError("This field cannot be empty!");
            return true;
        }
        return false;
    }
    private void ResetPassword(){
        final String regno=mRegNo.getText().toString();
        final String password = mPassword.getText().toString();

        if(!Empty(regno,mRegNo) && !Empty(password,mPassword)){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.RESET_PASSWORD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Password Updated")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else {
                        Log.d("Response",response);
                        Toast.makeText(ResetPassword.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Reset err",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("regno", regno);
                    params.put("password", password);
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }
    }
}