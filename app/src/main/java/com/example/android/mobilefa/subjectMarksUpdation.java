package com.example.android.mobilefa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class subjectMarksUpdation extends AppCompatActivity {

    RecyclerView mRecylcerView;
    Button btn;
    SharedPreferences sharedPreferences ;
    SubjectListAdapter adapter;
    ProgressDialog progressDialog = new ProgressDialog(subjectMarksUpdation.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);


        Intent i = getIntent();
        Bundle b = i.getBundleExtra("semester");
        assert b != null;
        final int sem = b.getInt("sem");
        final int cie = b.getInt("cie");
        final String type_of_xam = b.getString("type_of_exam");

        DataHolder.getInstance().setType(type_of_xam);

        //get Array from api and create objects
        final ArrayList<Subjects> subjectsArrayList = new ArrayList<>();

        progressDialog.startProgress();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismissProgress();
            }
        }, 1500);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_SUBJECTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                setContentView(R.layout.activity_subject_marks_updation);
                mRecylcerView = findViewById(R.id.list_view_subject_marks);
                btn = findViewById(R.id.btn_subject_marks_updation);

                //Get response, convert to arrays and return
                //Log.d("SUBJECT DATA : ", response);
                String[] S = response.split(",");

                for(String sub : S) {
                    subjectsArrayList.add(new Subjects(sub));
                }

                Log.d("Subjects Array : ", subjectsArrayList.toString());

                assert type_of_xam != null;
                if(type_of_xam.equals("cie"))
                    adapter = new SubjectListAdapter(subjectsArrayList);
                else
                    adapter = new SubjectListAdapter( subjectsArrayList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecylcerView.setLayoutManager(mLayoutManager);
                mRecylcerView.setAdapter(adapter);

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(SubjectListAdapter.getSubject().length()>0){

                            StringRequest request = new StringRequest(Request.Method.POST, Constants.SUBJECT_MARK_UPDATION_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("Subjects and marks Updated")) {
                                        SubjectListAdapter.clearHashMap();
                                        Toast.makeText(getApplicationContext(), "Marks Updated!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                        finish();
                                    }
                                    else {
                                        Log.d("Response : ", response);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("ERROR ", error.toString());
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject =SubjectListAdapter.getSubject();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    assert jsonObject != null;
                                    Log.d("SUBJECT & MARK JSONED ", jsonObject.toString());

                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("type", type_of_xam);
                                    params.put("semester", String.valueOf(sem+1));
                                    params.put("num", String.valueOf(cie+1));
                                    params.put("submarks", jsonObject.toString());
                                    params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                                    return params;
                                }
                            };

                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                        }
                        else{
                            Toast.makeText(subjectMarksUpdation.this, "Check All Fields", Toast.LENGTH_SHORT).show();
                        }
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
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("department", Objects.requireNonNull(sharedPreferences.getString("DEP", "")).toLowerCase());
                params.put("sem", String.valueOf(sem));
                params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    public static class DataHolder {

        protected String type;
        protected String getType() {
            return type;
        }
        protected void setType(String data) {
            this.type = data;
        }

        protected static final DataHolder holder = new DataHolder();
        protected static DataHolder getInstance() {
            return holder;
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }

}
