package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class subjectMarksUpdation extends AppCompatActivity {
    RecyclerView mRecylcerView;
    Button btn;
    SharedPreferences sharedPreferences;
    SubjectListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("semester");
        final int sem = b.getInt("sem");
        final int cie = b.getInt("cie");
        final String type_of_xam = b.getString("type_of_exam");

        //get Array from api and create objects

        final ArrayList<Subjects> subjectsArrayList = new ArrayList<>();



        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_SUBJECTS, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                setContentView(R.layout.activity_subject_marks_updation);
                mRecylcerView = findViewById(R.id.list_view_subject_marks);
                btn = findViewById(R.id.btn_subject_marks_updation);
                //Get response, convert to arrays and return
                Log.d("SUBJECT DATA: ",response);
                String[] S = response.split(",");
                for(int i=0; i<S.length; i++) {
                    Subjects sub = new Subjects(S[i]);
                    subjectsArrayList.add(sub);
                    Log.d("SUBJECT "+i+" : ",S[i]);
                }

                Log.d("subsss: ",subjectsArrayList.toString());
                adapter = new SubjectListAdapter(getApplicationContext(), R.layout.adapter_view_layout_subject_mark, subjectsArrayList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecylcerView.setLayoutManager(mLayoutManager);
                mRecylcerView.setAdapter(adapter);

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(subjectMarksUpdation.this, Constants.subjectandmark.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("After Click ",Constants.subjectandmark.toString());
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
                                Log.d("ERROR . ", error.toString());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
//                                for (String x : Constants.subjectandmark.keySet()) {
////                            Toast.makeText(subjectMarksUpdation.this, Constants.subjectandmark.get(x).toString(), Toast.LENGTH_SHORT).show();
//                                    JSONObject jsonObject = new JSONObject(Constants.subjectandmark);
//                                    Log.d("SUBJECT & MARKs: ",jsonObject.toString());
//                                    params.put(type_of_xam, jsonObject.toString());
//                                    Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
//                                }
                                params.put("submarks","{'SOFTWARE ENGINEERING':11,'COMPUTER NETWORKS':10,'EMBEDDED SYSTEMS DESIGN':9,'THEORY OF COMPUTATION':8,'ELECTIVE - MULTIMEDIA SYSTEM':7,'ELECTIVE - DATA WAREHOUSING AND DATA MINING ':6,'ELECTIVE - C# AND DOTNET PROGRAMMING':5,'COMPUTER NETWORKS LABORATORY':4,'PYTHON PROGRAMMING LABORATORY':3,'PROFESSIONAL COMMUNICATION SKILLS LABORATORY':2,'SOFT SKILLS AND APTITUDE – III':1}");
                                params.put("semester", String.valueOf(sem));
                                params.put("num",String.valueOf(cie));
                                params.put("regno", String.valueOf(Constants.REG_NO));
                                params.put("type",type_of_xam);
                                return params;
                            }
                        };
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

                        Constants.subjectandmark.clear();
                    }
                });


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
                params.put("department",sharedPreferences.getString("DEP","").toLowerCase());
                params.put("sem",String.valueOf(sem));
                params.put("regno", String.valueOf(Constants.REG_NO));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
    private ArrayList<Subjects> getData(final int sem){
        final ArrayList<Subjects> subjectList = new ArrayList<>();

        return subjectList;
    }
}
