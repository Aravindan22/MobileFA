package com.example.android.mobilefa;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class WorkshopEvent extends AppCompatActivity {

    EditText workshop_topic, college_name, date_attended;
    Button workshop_submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_workshop);

        workshop_topic = findViewById(R.id.activity_event_workshop_topic_edittext);
        college_name = findViewById(R.id.activity_event_workshop_clg_edittext);
        date_attended = findViewById(R.id.activity_event_workshop_date_edittext);
        workshop_submit = findViewById(R.id.activity_event_workshop_submit_button);

        final String workshopTopic = workshop_topic.getText().toString().trim();
        final String collegeName = college_name.getText().toString().trim();
        final String dateAttended = date_attended.getText().toString().trim();

        workshop_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WorkshopEvent.this, "Workshop Event Upadted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
