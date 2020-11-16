package com.example.android.mobilefa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class SelectCie extends AppCompatActivity  {

    int cie,sem;
    Button select_cie_submit_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cie);

        NumberPicker spin_cie = findViewById(R.id.activity_select_cie_cieSpin);
        NumberPicker spin_sem = findViewById(R.id.activity_select_cie_semSpin);

        select_cie_submit_button = findViewById(R.id.activity_select_cie_submit_button);

        spin_cie.setMinValue(1);
        spin_cie.setMaxValue(3);

        spin_sem.setMinValue(1);
        spin_sem.setMaxValue(8);

        spin_cie.setOnValueChangedListener(o1);
        spin_sem.setOnValueChangedListener(o2);

        select_cie_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("type_of_exam", "cie");
                b.putInt("cie", cie);
                b.putInt("sem", sem);
                Intent i = new Intent(getApplicationContext(), subjectMarksUpdation.class);
                i.putExtra("semester", b);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }

    NumberPicker.OnValueChangeListener o1 = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            cie = picker.getValue() - 1;
            Log.d("Cie : ", String.valueOf(cie));
        }
    };

    NumberPicker.OnValueChangeListener o2 = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
             sem = picker.getValue() - 1;
            Log.d("Sem : ", String.valueOf(sem));
        }
    };


}

