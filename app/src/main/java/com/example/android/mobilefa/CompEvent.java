package com.example.android.mobilefa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class CompEvent extends AppCompatActivity {

    EditText event_name, organization, date_attended;
    Button event_submit;
    Spinner prize_won;
    SharedPreferences sharedPreferences ;
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
        setContentView(R.layout.activity_event_competiton);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        event_name = findViewById(R.id.activity_event_comp_topic_edittext);
        organization = findViewById(R.id.activity_event_comp_clg_edittext);
        date_attended = findViewById(R.id.activity_event_comp_date_edittext);
        prize_won = findViewById(R.id.activity_event_comp_prize);
        event_submit = findViewById(R.id.activity_event_comp_submit_button);

        ArrayAdapter<CharSequence> prizeAdapter = ArrayAdapter.createFromResource(this, R.array.prize_spin, R.layout.spinner_item);
        prizeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        prize_won.setAdapter(prizeAdapter);

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
                new DatePickerDialog(CompEvent.this, eventDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        event_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String eventName = event_name.getText().toString();
                final String eventDate = date_attended.toString();
                final String organizationName = organization.getText().toString();
                final String eventPrize = prize_won.getSelectedItem().toString();

                final boolean flag_eventName = Empty(eventName, event_name);
                final boolean flag_eventDate = Empty(eventDate, date_attended);
                final boolean flag_organizationName = Empty(organizationName, organization);
                boolean flag_eventPrize = false;

                if(eventPrize.equals("Select")) {
                    ((TextView)prize_won.getSelectedView()).setError("Please select one!");
                    flag_eventPrize = true;
                }

                if(!flag_eventName && !flag_eventDate && !flag_organizationName && !flag_eventPrize) {
                    StringRequest request = new StringRequest(Request.Method.POST, Constants.EVENT_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("Event Updated")) {
                                Toast.makeText(getApplicationContext(), "Event Included", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("COMPETITION_EVENT ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                            params.put("eventName", eventName);
                            params.put("eventDate", eventDate);
                            params.put("organizationName", organizationName);
                            params.put("eventPrize", prize_won.getSelectedItem().toString());
                            Log.d("Competition details : ", String.valueOf(params));
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
