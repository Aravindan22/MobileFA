package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

    private Handler handler;
    private ProgressDialog progressDialog;
    private Context context;



    protected  JSONObject hashMapToJSON(HashMap<String,Integer> hm ) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for(String sub:hm.keySet()){
            jsonObject.put(sub,new Integer(hm.get(sub)));
        }return jsonObject;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(subjectMarksUpdation.this);

        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("semester");
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
                Log.d("SUBJECT DATA: ",response);
                String[] S = response.split(",");

                for(String sub : S) {
                    subjectsArrayList.add(new Subjects(sub));
                    Log.d("SUBJECT " +  " : " , sub);
                }


                Log.d("subsss: ",subjectsArrayList.toString());
                if(type_of_xam == "cie")
                    adapter = new SubjectListAdapter(getApplicationContext(), R.layout.adapter_view_layout_subject_mark, subjectsArrayList);
                else
                    adapter = new SubjectListAdapter(getApplicationContext(), R.layout.adapter_view_layout_subject_mark_sem, subjectsArrayList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecylcerView.setLayoutManager(mLayoutManager);
                mRecylcerView.setAdapter(adapter);

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        StringRequest request = new StringRequest(Request.Method.POST, Constants.SUBJECT_MARK_UPDATION_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Subjects and marks Updated")) {
                                    SubjectListAdapter.clearHashMap();
                                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                    finish();
                                }
                                else {
                                    Log.d("Response",response);
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
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject =SubjectListAdapter.getSubject();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("SUBJECT & MARK JSONED ",jsonObject.toString());

                                HashMap<String, String> params = new HashMap<>();
                                params.put("type",type_of_xam);
                                params.put("semester", String.valueOf(sem+1));
                                params.put("num",String.valueOf(cie+1));
                                params.put("submarks",jsonObject.toString());
                                params.put("regno",String.valueOf(sharedPreferences.getInt("REG_NO",0)));
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
