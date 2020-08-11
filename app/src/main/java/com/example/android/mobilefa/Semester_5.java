package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Semester_5 extends AppCompatActivity {

    private Spinner elective_I;
    private EditText elective_I_edittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem5);

        elective_I = (Spinner) findViewById(R.id.activity_sem5_elective_I_spinner);
        String elective_I_name = (String) elective_I.getSelectedItem();

        elective_I_edittext = (EditText) findViewById(R.id.activity_sem5_elective_edittext);
        elective_I_edittext.setText(elective_I_name);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SelectSem.class));
        finish();
    }
}
