package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    EditText mname, mregno , mpassw , mdep, memail;
    Button btn;
    Spinner msection, myear;
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    protected void Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s))
            et.setError("This field cannot be empty!");
    }

    private void  registerNewuser(){

        final String regno = mregno.getText().toString().trim();
        final String name = mname.getText().toString().trim();
        final String email = memail.getText().toString().trim();
        final String dept = mdep.getText().toString().trim();
        final String year = myear.getSelectedItem().toString();
        final String section = msection.getSelectedItem().toString();
        final String password = mpassw.getText().toString().trim();

        Empty(name, mname);
        Empty(regno, mregno);
        Empty(email, memail);
        Empty(dept, mdep);
        Empty(password, mpassw);

        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if(!(password.matches(pattern)))
            mpassw.setError("Not a strong password!");

        if(year.equals("Select"))
            ((TextView)myear.getSelectedView()).setError("Please select one!");
        if(section.equals("Select"))
            ((TextView)msection.getSelectedView()).setError("Please select one!");

        StringRequest request = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Registered")){
                    //Toast.makeText(RegisterUser.this, "Registered", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(RegisterUser.this, response, Toast.LENGTH_SHORT).show();
                    Log.d("Register error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegisterUser.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("REG ERR",error.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("regno", regno);
                params.put("name",name);
                params.put("mail", email);
                params.put("dept", dept);
                params.put("year",year);
                params.put("section", section);
                params.put("password", password);

                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
