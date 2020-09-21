package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class subjectMarksUpdation extends AppCompatActivity {
    ListView mlistView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_marks_updation);
        mlistView = findViewById(R.id.list_view_subject_marks);
        btn = findViewById(R.id.btn_subject_marks_updation);

        Intent i = getIntent();
        Bundle b = i.getBundleExtra("semester");

        String dep = Constants.DEP;
        final int sem = b.getInt("sem");
        final int cie = b.getInt("cie");
        final String type_of_xam = b.getString("type_of_exam");

        //get Array from api and create objects
//        ArrayList<Subjects> subjectsArrayList = getData(sem, dep);

        ArrayList<Subjects> subjectsArrayList = new ArrayList<>();
        subjectsArrayList.add(new Subjects("TECHNICAL ENGLISH"));
        subjectsArrayList.add(new Subjects("TECHNICAL MATHS"));
        subjectsArrayList.add(new Subjects("MATERIAL PHYSICS"));

        SubjectListAdapter adapter = new SubjectListAdapter(this, R.layout.adapter_view_layout_subject_mark, subjectsArrayList);
        mlistView.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject jsonObject = new JSONObject(Constants.subjectandmark);
                Toast.makeText(subjectMarksUpdation.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                Log.d("DATA",jsonObject.toString());
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
                        params.put("regno",String.valueOf(Constants.REG_NO));
                        params.put("type",type_of_xam);
                        params.put("semester",String.valueOf(sem));
                        params.put("num",String.valueOf(cie));
                        params.put("subject",jsonObject.toString());
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

                Constants.subjectandmark.clear();
            }
        });
    }
    private ArrayList<Subjects> getData(int sem, String dep){


        ArrayList<String> arr=new ArrayList<>();

        final ArrayList<Subjects> subjectList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_SUBJECTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               //Get response, convert to arrays and return

                String[] S = response.split(",");
                for(int i=0; i<S.length; i++) {
                    Subjects sub = new Subjects(S[i]);
                    subjectList.add(sub);
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
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        return subjectList;
    }
}
