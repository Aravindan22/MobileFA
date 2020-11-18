package com.example.android.mobilefa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WorkshopEvent extends AppCompatActivity {

    EditText workshop_topic, organization_name, date_attended;
    Button workshop_submit;
    SharedPreferences sharedPreferences;

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
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        workshop_topic = findViewById(R.id.activity_event_workshop_topic_edittext);
        organization_name = findViewById(R.id.activity_event_workshop_clg_edittext);
        date_attended = findViewById(R.id.activity_event_workshop_date_edittext);
        workshop_submit = findViewById(R.id.activity_event_workshop_submit_button);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener eventDate = new DatePickerDialog.OnDateSetListener() {

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                date_attended.setText(sdf.format(myCalendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(WorkshopEvent.this, eventDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                            params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
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
