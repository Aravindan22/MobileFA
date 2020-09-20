package com.example.android.mobilefa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class SelectSem extends AppCompatActivity  {

    int sem;
    Button select_submit_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sem);

        NumberPicker select_sem_spin_sem = findViewById(R.id.activity_select_sem_semSpin);

        select_submit_button = findViewById(R.id.activity_select_sem_submit_button);


        select_sem_spin_sem.setMinValue(1);
        select_sem_spin_sem.setMaxValue(8);

        //select_sem_spin_sem.setWrapSelectorWheel(true);

        select_sem_spin_sem.setOnValueChangedListener(o1);

        select_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("type_of_exam", "sem");
                b.putInt("sem", sem);
                Intent i = new Intent(getApplicationContext(),subjectMarksUpdation.class);
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
            sem = picker.getValue();


        }
    };
}

