package com.example.android.mobilefa;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

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

public class SubjectListAdapter extends ArrayAdapter<Subjects> {
    private  Context mContext;

    int mResource;
    public SubjectListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Subjects> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String sub = getItem(position).getSubject();
        Subjects subject = new Subjects(sub);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
        TextView tvSubj = convertView.findViewById(R.id.text_view_subject_name_adapter);
        EditText tvEdit= convertView.findViewById(R.id.edittext_mark_adapter);

        tvEdit.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 50)});
        tvEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    Constants.subjectandmark.put(getItem(position).getSubject(),Integer.parseInt(s.toString()));
                    Toast.makeText(mContext, String.valueOf(position)+";;;", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvSubj.setText(sub);
        return  convertView;
    }

}
