package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RegisterUser extends AppCompatActivity {
    EditText mregno , mpassw , mdep;
    Button btn;
    TextView mlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mregno =  findViewById(R.id.reg_edit_text);
        mpassw = findViewById(R.id.pass_edit_text);
        mdep = findViewById(R.id.dep_edit_text);
        mlogin = findViewById(R.id.login_text_view);
        btn = findViewById(R.id.reg_btn);
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
        final String regno = mregno.getText().toString().trim();
        final String passw =mpassw.getText().toString().trim();
        final String dep = mdep.getText().toString().trim();


        String url = Constants.REGISTER_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Registered")){
                    Toast.makeText(RegisterUser.this, "Registered", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(RegisterUser.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterUser.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("regno", regno);
                params.put("password", passw);
                params.put("dep", dep);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
