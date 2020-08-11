package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CompEvent extends AppCompatActivity {

    EditText event_name, organization;
    Button event_submit;
    Spinner prize_won;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_competiton);

        event_name = findViewById(R.id.activity_event_comp_topic_edittext);
        organization = findViewById(R.id.activity_event_comp_clg_edittext);
        prize_won = findViewById(R.id.activity_event_comp_prize);
        event_submit = findViewById(R.id.activity_event_comp_submit_button);

        /*final String eventName = event_name.getText().toString().trim();
        final String eventCollege = event_college.getText().toString().trim();
        final Spinner prizeWon = prize_won;*/
        
        event_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest  request = new StringRequest(Request.Method.POST, Constants.WORKSHOP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "WorkShop event Updated") {
                            Toast.makeText(getApplicationContext(), "Workshop Included", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WORKSHOP ERROR",error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("regno", String.valueOf(Constants.REG_NO));
                        params.put("eventName",event_name.getText().toString());
                        params.put("organization",organization.getText().toString());
                        params.put("prize",prize_won.getSelectedItem().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EventUpdater.class));
        finish();
    }
}
