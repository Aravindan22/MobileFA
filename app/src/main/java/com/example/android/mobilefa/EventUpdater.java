package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.android.mobilefa.Constants.dep;
import static com.example.android.mobilefa.Constants.regno;

public class EventUpdater extends AppCompatActivity {
    Button workshop_btn, course_btn, comp_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_updater);

        workshop_btn = findViewById(R.id.activity_event_workshop_btn);
        course_btn = findViewById(R.id.activity_event_course_btn);
        comp_btn = findViewById(R.id.activity_event_event_btn);

        workshop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workshopIntent = new Intent(getApplicationContext(), WorkshopEvent.class);
                startActivity(workshopIntent);
                finish();
            }
        });

        course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent courseIntent = new Intent(getApplicationContext(), CourseEvent.class);
                startActivity(courseIntent);
                finish();
            }
        });

        comp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent compIntent = new Intent(getApplicationContext(), CompEvent.class);
                startActivity(compIntent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }



    /*public void eventUpdater(){
        String url = Constants.EVENT_URL;
        final String event = editText.getText().toString().trim();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Event Updated")){
                    Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    Log.d("Error php ",response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("regno", Constants.regno);
                params.put("event",event);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }*/
}
