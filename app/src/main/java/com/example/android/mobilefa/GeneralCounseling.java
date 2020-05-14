package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GeneralCounseling extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_counseling);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }
}
