package com.example.android.mobilefa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Semester_6 extends AppCompatActivity {

    private Spinner elective_II, elective_III;
    private EditText elective_II_edittext, elective_III_edittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem6);

        elective_II = (Spinner) findViewById(R.id.activity_sem6_elective_II_spinner);
        String elective_II_name = (String) elective_II.getSelectedItem();
        elective_III = (Spinner) findViewById(R.id.activity_sem6_elective_III_spinner);
        String elective_III_name = (String) elective_III.getSelectedItem();

        elective_II_edittext = (EditText) findViewById(R.id.activity_sem6_elective_II_edittext);
        elective_II_edittext.setText(elective_II_name);
        elective_III_edittext = (EditText) findViewById(R.id.activity_sem6_elective_III_edittext);
        elective_III_edittext.setText(elective_III_name);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SelectSem.class));
        finish();
    }
}
