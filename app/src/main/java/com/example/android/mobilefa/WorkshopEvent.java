package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class WorkshopEvent extends AppCompatActivity {

    EditText workshop_topic, organization_name, date_attended;
    Button workshop_submit;

    protected boolean Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s)) {
            et.setError("This field cannot be empty!");
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_workshop);

        workshop_topic = findViewById(R.id.activity_event_workshop_topic_edittext);
        organization_name = findViewById(R.id.activity_event_workshop_clg_edittext);
        date_attended = findViewById(R.id.activity_event_workshop_date_edittext);
        workshop_submit = findViewById(R.id.activity_event_workshop_submit_button);

        workshop_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String workshopTopic = workshop_topic.getText().toString().trim();
                final String organizationName = organization_name.getText().toString().trim();
                final String dateAttended = date_attended.getText().toString().trim();

                final boolean flag_workshopTopic = Empty(workshopTopic, workshop_topic);
                final boolean flag_organizationName = Empty(organizationName, organization_name);
                final boolean flag_dateAttended = Empty(dateAttended, date_attended);

                if(!flag_workshopTopic && !flag_organizationName && !flag_dateAttended) {
                    StringRequest request = new StringRequest(Request.Method.POST, Constants.WORKSHOP_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("WorkShop event Updated")) {
                                Toast.makeText(getApplicationContext(), "Workshop Included", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("WORKSHOP ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("regno", String.valueOf(Constants.REG_NO));
                            params.put("workshopTopic", workshopTopic);
                            params.put("organizationName", organizationName);
                            params.put("workshopDate", dateAttended);
                            Log.d("Workshop details : ", String.valueOf(params));
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
        startActivity(new Intent(getApplicationContext(),EventUpdater.class));
        finish();
    }
}
