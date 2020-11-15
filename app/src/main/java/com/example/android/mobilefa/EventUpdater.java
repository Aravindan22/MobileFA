package com.example.android.mobilefa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class EventUpdater extends AppCompatActivity {

    Button workshop_btn, course_btn, comp_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_updater);

        workshop_btn = findViewById(R.id.activity_event_workshop_btn);
        course_btn = findViewById(R.id.activity_event_course_btn);
        comp_btn = findViewById(R.id.activity_event_event_btn);

        workshop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workshopIntent = new Intent(getApplicationContext(), WorkshopEvent.class);
                startActivity(workshopIntent);
                finish();
            }
        });

        course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent courseIntent = new Intent(getApplicationContext(), CourseEvent.class);
                startActivity(courseIntent);
                finish();
            }
        });

        comp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compIntent = new Intent(getApplicationContext(), CompEvent.class);
                startActivity(compIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }
}
