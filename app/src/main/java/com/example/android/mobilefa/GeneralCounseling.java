package com.example.android.mobilefa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GeneralCounseling extends AppCompatActivity {

    EditText improvement, issue, suggestion;
    Button counseling_submit;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_counseling);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        improvement = findViewById(R.id.activity_general_counselling_improvement_edittext);
        issue = findViewById(R.id.activity_general_counselling_issue_edittext);
        suggestion = findViewById(R.id.activity_general_counselling_suggestion_edittext);
        counseling_submit= findViewById(R.id.activity_general_counselling_submit_button);

        counseling_submit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                final String improvements = improvement.getText().toString();
                final String issues = issue.getText().toString();
                final String suggestions = suggestion.getText().toString();

                boolean flag_counselling = false;

                if(TextUtils.isEmpty(improvements) && TextUtils.isEmpty(issues) && TextUtils.isEmpty(suggestions)) {
                    flag_counselling = true;
                    Toast.makeText(getApplicationContext(), "Please fill atleast one field!", Toast.LENGTH_LONG).show();
                }

                if(!flag_counselling) {
                    StringRequest request = new StringRequest(Request.Method.POST, Constants.GENERAL_COUNSELING_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("General Counselling Data Filled")) {
                                Toast.makeText(getApplicationContext(), "General counselling Done", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("General Counselling", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                            params.put("improvement", improvements);
                            params.put("issue", issues);
                            params.put("suggestion", suggestions);
                            Log.d("General Counselling : ", String.valueOf(params));
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }
}
