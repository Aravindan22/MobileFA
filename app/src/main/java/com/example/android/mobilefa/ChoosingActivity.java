package com.example.android.mobilefa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosingActivity extends AppCompatActivity {

    Button cie, sem, event, counselling,btn;
    Button logout;

    protected  void logOut() {
        Log.d("Logged out", String.valueOf(logout));
        SharedPreferences preferences = getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        cie = findViewById(R.id.activity_choosing_cie_btn);
        sem = findViewById(R.id.activity_choosing_sem_btn);
        event = findViewById(R.id.activity_choosing_event_btn);
        counselling = findViewById(R.id.activity_choosing_counselling_btn);
        logout = findViewById(R.id.activity_choosing_logout_btn);

        cie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Activity Chosen : ", "Update Cie Marks");
               Intent cie_intent = new Intent(getApplicationContext(), SelectCie.class);
                startActivity(cie_intent);
                finish();
            }
        });

        sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Activity Chosen : ", "Update Semester Marks");
                Intent sem_intent = new Intent(getApplicationContext(), SelectSem.class);
                startActivity(sem_intent);
                finish();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Activity Chosen : ", "Update Events");
               Intent event_intent = new Intent(getApplicationContext(), EventUpdater.class);
                startActivity(event_intent);
                finish();
            }
        });

        counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Activity Chosen : ", "General Counselling");
                Intent counselling_intent = new Intent(getApplicationContext(), GeneralCounseling.class);
                startActivity(counselling_intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

    }
}
