package com.example.android.mobilefa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;

public class SelectCie extends AppCompatActivity  {

    int cie,sem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cie);

        NumberPicker spin_cie = findViewById(R.id.ciespin);
        NumberPicker spin_sem = findViewById(R.id.semspin);

        spin_cie.setMinValue(1);
        spin_cie.setMaxValue(3);

        spin_sem.setMinValue(1);
        spin_sem.setMaxValue(8);

        spin_cie.setOnValueChangedListener(o1);
        spin_sem.setOnValueChangedListener(o2);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }

    NumberPicker.OnValueChangeListener o1 = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            cie = picker.getValue();
        }
    };

    NumberPicker.OnValueChangeListener o2 = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
             sem = picker.getValue();
        }
    };
}

