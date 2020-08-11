package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Semester_2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem2);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SelectSem.class));
        finish();
    }
}
