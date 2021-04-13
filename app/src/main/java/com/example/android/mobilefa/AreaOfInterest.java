package com.example.android.mobilefa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AreaOfInterest extends AppCompatActivity {
EditText aio_category,aio_description;
Button aio_upload_media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_of_interest);
        aio_category=findViewById(R.id.activity_aoi_category_edittext);
        aio_description=findViewById(R.id.activity_aoi_description_edittext);
        aio_upload_media=findViewById(R.id.activity_aoi_uploadmedia_button);
    }
}