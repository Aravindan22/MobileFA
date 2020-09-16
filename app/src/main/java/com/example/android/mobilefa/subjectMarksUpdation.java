package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

public class subjectMarksUpdation extends AppCompatActivity {
    ListView mlistView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_marks_updation);
        mlistView = findViewById(R.id.list_view_subject_marks);
        btn = findViewById(R.id.btn_subject_marks_updation);
        final String type_of_xam = "";   //pass value from bfre activity

        //get Array from api and create objects
//        ArrayList<Subjects> subjectsArrayList = getData();
        ArrayList<Subjects> subjectsArrayList = new ArrayList<>();

        subjectsArrayList.add(new Subjects("TECHNICAL ENGLISH"));
        subjectsArrayList.add(new Subjects("TECHNICAL MATHS"));
        subjectsArrayList.add(new Subjects("MATERIAL PHYSICS"));

        SubjectListAdapter adapter = new SubjectListAdapter(this, R.layout.adapter_view_layout_subject_mark, subjectsArrayList);
        mlistView.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, Constants.SUBJECT_MARK_UPDATION_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "Subjects and marks Updated") {
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        for (String x : Constants.subjectandmark.keySet()) {
//                            Toast.makeText(subjectMarksUpdation.this, Constants.subjectandmark.get(x).toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(Constants.subjectandmark);
                            params.put(type_of_xam, jsonObject.toString());
                        }
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

                Constants.subjectandmark.clear();
            }
        });
    }
    private  ArrayList<String> getData(int sem,String type_of_xam){
        ArrayList<String> arr=new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_SUBJECTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               //Get response, convert to arrays and return
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        return  arr;
    }
}