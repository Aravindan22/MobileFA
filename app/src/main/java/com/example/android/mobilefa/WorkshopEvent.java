package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class WorkshopEvent extends AppCompatActivity {

    EditText workshop_topic, college_name, date_attended;
    Button workshop_submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_workshop);

        workshop_topic = findViewById(R.id.activity_event_workshop_topic_edittext);
        college_name = findViewById(R.id.activity_event_workshop_clg_edittext);
        date_attended = findViewById(R.id.activity_event_workshop_date_edittext);
        workshop_submit = findViewById(R.id.activity_event_workshop_submit_button);

        final String workshopTopic = workshop_topic.getText().toString().trim();
        final String collegeName = college_name.getText().toString().trim();
        final String dateAttended = date_attended.getText().toString().trim();

        workshop_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest  request = new StringRequest(Request.Method.POST, Constants.WORKSHOP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "WorkShop event Updated") {
                            Toast.makeText(WorkshopEvent.this, "Workshop Included", Toast.LENGTH_SHORT).show();
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
                        params.put("workshopTopic",workshopTopic);
                        params.put("collegeName",collegeName);
                        params.put("date",dateAttended);
                        return params;
                    }
                };
                Toast.makeText(WorkshopEvent.this, "Workshop Event Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EventUpdater.class));
        finish();
    }
}
