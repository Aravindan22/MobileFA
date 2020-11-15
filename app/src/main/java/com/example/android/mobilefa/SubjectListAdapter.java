package com.example.android.mobilefa;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//For Filtering the Inputs in edit text..............
class InputFilterMinMax implements InputFilter {

    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.MyViewHolder> {

    List<Subjects> subjectList;
    static HashMap<String, String>hm = new HashMap<>();

    public ArrayAdapter<CharSequence> semesterAdapter;

    public SubjectListAdapter(Context applicationContext, int adapter_view_layout_subject_mark, ArrayList<Subjects> subjectsArrayList) {
        this.subjectList = subjectsArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectName;
        public EditText tvEdit;
        public Spinner spinner;

        public MyViewHolder(View view) {
            super(view);
            subjectName = view.findViewById(R.id.text_view_subject_name_adapter);
            tvEdit = view.findViewById(R.id.edittext_mark_adapter);
            spinner = view.findViewById(R.id.spinner_sem_adapter);

            semesterAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.spinner_grade, R.layout.spinner_item);
            semesterAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        }
    }

    public SubjectListAdapter(List<Subjects> subjectList) {
        this.subjectList = subjectList;
    }

    public String type = subjectMarksUpdation.DataHolder.getInstance().getType();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;

        if(type.equals("cie")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_layout_subject_mark, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_layout_subject_mark_sem, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    final boolean[] flag = {true, true, true, true};

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Subjects sub = subjectList.get(position);
        holder.subjectName.setText(sub.getSubject());

        if(type.equals("cie")) {
            if(sub.getSubject().contains("ELECTIVE")){
                holder.tvEdit.setVisibility(View.GONE);

                    holder.subjectName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if((sub.getSubject().contains("ELECTIVE")) && flag[0]) {
                                holder.tvEdit.setVisibility(View.VISIBLE);
                                flag[0] = false;
                            }
                            if(sub.getSubject().contains("ELECTIVE 2") && flag[1]) {
                                holder.tvEdit.setVisibility(View.VISIBLE);
                                flag[1] = false;
                            }
                            if(sub.getSubject().contains("ELECTIVE 3") && flag[2]) {
                                holder.tvEdit.setVisibility(View.VISIBLE);
                                flag[2] = false;
                            }
                            if(sub.getSubject().contains("OPEN ELECTIVE") && flag[3]) {
                                holder.tvEdit.setVisibility(View.VISIBLE);
                                flag[3] = false;
                            }

                            holder.tvEdit.setFilters(new InputFilter[]{new InputFilterMinMax(0, 50)});
                            holder.tvEdit.addTextChangedListener(new TextWatcher() {

                                public void afterTextChanged(Editable s) {

                                    String marks = holder.tvEdit.getText().toString();
                                    if (s.length() > 0) {
                                        Log.d("SUB Hm", hm.toString());
                                        hm.put(holder.subjectName.getText().toString(), marks);
                                    }
                                }

                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }
                            });
                        }
                    });

                holder.subjectName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        holder.tvEdit.setVisibility(View.GONE);

                        if(sub.getSubject().contains("ELECTIVE")) flag[0] = true;
                        if(sub.getSubject().contains("ELECTIVE 2")) flag[1] = true;
                        if(sub.getSubject().contains("ELECTIVE 3")) flag[2] = true;
                        if(sub.getSubject().contains("OPEN ELECTIVE")) flag[3] = true;
                        return true;
                    }
                });
            }
            else{
                holder.tvEdit.setVisibility(View.VISIBLE);
                holder.tvEdit.setFilters(new InputFilter[]{new InputFilterMinMax(0, 50)});

                holder.tvEdit.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {

                        String marks = holder.tvEdit.getText().toString();
                        if (s.length() > 0) {
                            Log.d("SUB Hm",hm.toString());
                            hm.put(holder.subjectName.getText().toString(), marks);
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                });
            }
        }
        else {
            holder.spinner.setAdapter(semesterAdapter);

            if(sub.getSubject().contains("ELECTIVE")){
                holder.spinner.setVisibility(View.GONE);

                holder.subjectName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((sub.getSubject().contains("ELECTIVE")) && flag[0]) {
                            holder.spinner.setVisibility(View.VISIBLE);
                            flag[0] = false;
                        }
                        if(sub.getSubject().contains("ELECTIVE 2") && flag[1]) {
                            holder.spinner.setVisibility(View.VISIBLE);
                            flag[1] = false;
                        }
                        if(sub.getSubject().contains("ELECTIVE 3") && flag[2]) {
                            holder.spinner.setVisibility(View.VISIBLE);
                            flag[2] = false;
                        }
                        if(sub.getSubject().contains("OPEN ELECTIVE") && flag[3]) {
                            holder.spinner.setVisibility(View.VISIBLE);
                            flag[3] = false;
                        }

                        final String[] grade = {""};

                        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                grade[0] = parent.getItemAtPosition(position).toString();
                                if(!grade[0].equals("Select")){
                                    hm.put(holder.subjectName.getText().toString(), grade[0]);
                                    Log.d("Selected Item",grade[0]);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                });

                holder.subjectName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        holder.spinner.setVisibility(View.GONE);

                        if(sub.getSubject().contains("ELECTIVE")) flag[0] = true;
                        if(sub.getSubject().contains("ELECTIVE 2")) flag[1] = true;
                        if(sub.getSubject().contains("ELECTIVE 3")) flag[2] = true;
                        if(sub.getSubject().contains("OPEN ELECTIVE")) flag[3] = true;
                        return true;
                    }
                });

            }
            else{
                final String[] grade = {""};
                holder.spinner.setVisibility(View.VISIBLE);

                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        grade[0] = parent.getItemAtPosition(position).toString();
                        if(!grade[0].equals("Select")){
                            hm.put(holder.subjectName.getText().toString(), grade[0]);
                            Log.d("Selcted Item",grade[0]);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static JSONObject getSubject(){
        JSONObject jsonObject =new JSONObject(hm);
        Log.d("HmToJson",jsonObject.toString());
        return jsonObject;
    }

    public  static  void clearHashMap(){
        hm.clear();
    }
}
