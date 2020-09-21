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

public class CourseEvent extends AppCompatActivity {

    EditText subject_name, organization_name, marks_obtained;
    Button course_submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_course);

        subject_name = findViewById(R.id.activity_event_course_topic_edittext);
        organization_name = findViewById(R.id.activity_event_course_org_edittext);
        marks_obtained = findViewById(R.id.activity_event_course_marks_edittext);
        course_submit = findViewById(R.id.activity_event_course_submit_button);

        final String subjectName = subject_name.getText().toString().trim();
        final String organizationName = organization_name.getText().toString().trim();
        final String marksObtained = String.valueOf(marks_obtained);

        course_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, Constants.COURSE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "Course event Updated") {
                            Toast.makeText(getApplicationContext(), "Course Event Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("COURSE_EVENT ERROR",error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("regno", String.valueOf(Constants.REG_NO));
                        params.put("coursename",subjectName);
                        params.put("organizationName",organizationName);
                        params.put("marks",marksObtained);
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
