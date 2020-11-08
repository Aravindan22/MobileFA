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

import org.json.JSONException;
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
        Log.d("Bundle",b.toString());
        //get Array from api and create objects

        final ArrayList<Subjects> subjectsArrayList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_SUBJECTS, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                setContentView(R.layout.activity_subject_marks_updation);
                mRecylcerView = findViewById(R.id.list_view_subject_marks);
                btn = findViewById(R.id.btn_subject_marks_updation);
                //Get response, convert to arrays and return
//                Log.d("SUBJECT DATA: ",response);
                final String[] S = response.split(",");
                for(int i=0; i<S.length; i++) {
                    Subjects sub = new Subjects(S[i]);
                    subjectsArrayList.add(sub);
//                    Log.d("SUBJECT "+i+" : ",S[i]);
                }


                adapter = new SubjectListAdapter(getApplicationContext(), R.layout.adapter_view_layout_subject_mark, subjectsArrayList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecylcerView.setLayoutManager(mLayoutManager);
                mRecylcerView.setAdapter(adapter);

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        StringRequest request = new StringRequest(Request.Method.POST, Constants.SUBJECT_MARK_UPDATION_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response == "Subjects and marks Updated") {
                                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                    SubjectListAdapter.hm.clear();
                                }else{
                                    Log.d("Response",response);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR  ", error.toString());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                    params.put("type",type_of_xam);
                                    params.put("semester", String.valueOf(sem));
                                    params.put("num",String.valueOf(cie));
                                    try {
                                        JSONObject jsonObject = SubjectListAdapter.getSubject();
                                        params.put("submarks",jsonObject.toString());
                                        Log.d("subMarksHM",jsonObject.toString());
                                    }catch (Exception e){
                                        Log.d("Exception ",e.toString());
                                    }
                                    params.put("regno", String.valueOf(Constants.REG_NO));
                                return params;
                            }
                        };
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


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
