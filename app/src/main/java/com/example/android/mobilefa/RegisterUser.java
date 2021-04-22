package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {

    EditText mname, mregno , mpassw , memail;
    Button btn;
    Spinner msection, myear, mdep;
    TextView mlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mregno =  findViewById(R.id.activity_register_regno_edittext);
        mpassw = findViewById(R.id.activity_register_password_edittext);
        mdep = findViewById(R.id.activity_register_dept);
        mname = findViewById(R.id.activity_register_name_edittext);
        memail = findViewById(R.id.activity_register_emailid_edittext);
        btn = findViewById(R.id.activity_register_register_button);
        mlogin = findViewById(R.id.activity_register_loginacc_textview);
        myear = findViewById(R.id.activity_register_year);
        msection = findViewById(R.id.activity_register_section);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.year_spin, R.layout.spinner_item);
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        myear.setAdapter(yearAdapter);

        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.createFromResource(this, R.array.section_spin, R.layout.spinner_item);
        sectionAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        msection.setAdapter(sectionAdapter);

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

    protected boolean Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s)) {
            et.setError("This field cannot be empty!");
            return true;
        }
        return false;
    }

    private void  registerNewuser(){

        final String regno = mregno.getText().toString().trim();
        final String name = mname.getText().toString().trim();
        final String email = memail.getText().toString().trim();
        final String dept = mdep.getSelectedItem().toString().trim();
        final String year = myear.getSelectedItem().toString();
        final String section = msection.getSelectedItem().toString();
        final String password = mpassw.getText().toString().trim();

        boolean flag_name = Empty(name, mname);
        boolean flag_regno = Empty(regno, mregno);
        boolean flag_email = Empty(email, memail);
        boolean flag_password = Empty(password, mpassw);

        boolean flag_dept = false;
        if(dept.equals("Select")) {
            ((TextView) myear.getSelectedView()).setError("Please select one!");
            flag_dept = true;
        }

        boolean flag_year = false;
        if(year.equals("Select")) {
            ((TextView) myear.getSelectedView()).setError("Please select one!");
            flag_year = true;
        }

        boolean flag_section = false;
        if(section.equals("Select")) {
            ((TextView) msection.getSelectedView()).setError("Please select one!");
            flag_section = true;
        }

        if(!flag_name && !flag_regno && !flag_email && !flag_dept && !flag_password && !flag_year && !flag_section) {

            StringRequest request = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Registered")) {
                        Toast.makeText(RegisterUser.this, "Registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else if(response.equals("User Already Present")) {
                        Toast.makeText(RegisterUser.this, "User Already Present!", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("Registration error", response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("REGISTER USER ERROR", error.toString());
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();

                    params.put("regno", regno);
                    params.put("name", name);
                    params.put("mail", email);
                    params.put("dept", dept);
                    params.put("year", year);
                    params.put("section", section);
                    params.put("password", password);

                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }
}
