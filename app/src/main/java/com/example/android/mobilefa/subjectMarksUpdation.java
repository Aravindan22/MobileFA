package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.Subject;

public class subjectMarksUpdation extends AppCompatActivity {
    ListView mlistView;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_marks_updation);
        mlistView= findViewById(R.id.list_view_subject_marks);
        btn = findViewById(R.id.btn_subject_marks_updation);
        Subjects s1 = new Subjects("TECHNICAL ENGLISH");
        Subjects s2 = new Subjects("TECHNICAL MATHS");
        Subjects s3 = new Subjects("MATERIAL PHYSICS");
        ArrayList<Subjects> subjectsArrayList = new ArrayList<>();
        subjectsArrayList.add(s1);subjectsArrayList.add(s2);subjectsArrayList.add(s3);
        SubjectListAdapter adapter = new SubjectListAdapter(this,R.layout.adapter_view_layout_subject_mark,subjectsArrayList);
        mlistView.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String x:Constants.subjectandmark.keySet()){
                    Toast.makeText(subjectMarksUpdation.this, Constants.subjectandmark.get(x).toString(), Toast.LENGTH_SHORT).show();
                }
                Constants.subjectandmark.clear();
            }
        });
    }
}