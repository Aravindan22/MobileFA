package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    EditText mname,mregno , mpassw , mdep, memail;
    Button btn;
    Spinner msection,myear;
    TextView mlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mregno =  findViewById(R.id.activity_register_regno_edittext);
        mpassw = findViewById(R.id.activity_register_password_edittext);
        mdep = findViewById(R.id.activity_register_dept_edittext);
        mname = findViewById(R.id.activity_register_name_edittext);
        memail = findViewById(R.id.activity_register_emailid_edittext);
        btn = findViewById(R.id.activity_register_register_button);
        mlogin = findViewById(R.id.activity_register_loginacc_textview);
        myear = findViewById(R.id.activity_register_year);
        msection = findViewById(R.id.activity_register_section);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewuser();
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    private void  registerNewuser(){

        StringRequest request = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Registered")){
                    Toast.makeText(RegisterUser.this, "Registered", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(RegisterUser.this, response, Toast.LENGTH_SHORT).show();
                    Log.d("Register error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterUser.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("REG ERR",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name",mname.getText().toString().trim());
                params.put("regno", mregno.getText().toString().trim());
                params.put("year",myear.getSelectedItem().toString());
                params.put("password", mpassw.getText().toString().trim());
                params.put("dept", mdep.getText().toString().trim());
                params.put("mail",memail.getText().toString().trim());
                params.put("section",msection.getSelectedItem().toString());
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
