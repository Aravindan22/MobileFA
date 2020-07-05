package com.example.android.mobilefa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class CompEvent extends AppCompatActivity {

    EditText event_name, event_college;
    Button event_submit;
    Spinner prize_won;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_competiton);

        event_name = findViewById(R.id.activity_event_comp_topic_edittext);
        event_college = findViewById(R.id.activity_event_comp_clg_edittext);
        prize_won = findViewById(R.id.activity_event_comp_prize);
        event_submit = findViewById(R.id.activity_event_comp_submit_button);

        final String eventName = event_name.getText().toString().trim();
        final String eventCollege = event_college.getText().toString().trim();
        final Spinner prizeWon = prize_won;

        event_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompEvent.this, "Competition Event Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
