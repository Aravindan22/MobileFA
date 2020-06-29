package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosingActivity extends AppCompatActivity {

    Button cie, sem, event, counselling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        cie = findViewById(R.id.cie_btn);
        sem = findViewById(R.id.sem_btn);
        event = findViewById(R.id.event_btn);
        counselling = findViewById(R.id.counselling_btn);

        cie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent cie_intent = new Intent(getApplicationContext(), SelectCie.class);
                startActivity(cie_intent);
                finish();
            }
        });

        sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent sem_intent = new Intent(getApplicationContext(), Sem.class);
//                startActivity(sem_intent);
                finish();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent event_intent = new Intent(getApplicationContext(), EventUpdater.class);
                startActivity(event_intent);
                finish();
            }
        });

        counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent counselling_intent = new Intent(getApplicationContext(), GeneralCounseling.class);
                startActivity(counselling_intent);
                finish();
            }
        });
    }
}
