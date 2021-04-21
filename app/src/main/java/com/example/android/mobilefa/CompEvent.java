package com.example.android.mobilefa;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CompEvent extends AppCompatActivity {

    EditText event_name, organization, date_attended;
    Button event_submit;
    String image_url;
    Spinner prize_won;
    TextView event_comp_prize_certificate_info;
    ImageView event_comp_prize_certificate_upload;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    SharedPreferences sharedPreferences ;
    protected boolean Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s)) {
            et.setError("This field cannot be empty!");
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_competiton);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        event_name = findViewById(R.id.activity_event_comp_topic_edittext);
        organization = findViewById(R.id.activity_event_comp_clg_edittext);
        date_attended = findViewById(R.id.activity_event_comp_date_edittext);
        prize_won = findViewById(R.id.activity_event_comp_prize);
        event_submit = findViewById(R.id.activity_event_comp_submit_button);
        event_comp_prize_certificate_info=findViewById(R.id.activity_event_comp_prize_upload_info);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        event_comp_prize_certificate_upload= findViewById(R.id.activity_event_comp_prize_upload_button);


        ArrayAdapter<CharSequence> prizeAdapter = ArrayAdapter.createFromResource(this, R.array.prize_spin, R.layout.spinner_item);
        prizeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        prize_won.setAdapter(prizeAdapter);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener eventDate = new DatePickerDialog.OnDateSetListener() {

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                date_attended.setText(sdf.format(myCalendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
       event_comp_prize_certificate_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();


            }
        });
        date_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CompEvent.this, eventDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        event_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String eventName = event_name.getText().toString().trim();
                final String eventDate = date_attended.getText().toString().trim();
                final String organizationName = organization.getText().toString().trim();
                final String eventPrize = prize_won.getSelectedItem().toString();
                final boolean flag_eventName = Empty(eventName, event_name);
                final boolean flag_eventDate = Empty(eventDate, date_attended);
                final boolean flag_organizationName = Empty(organizationName, organization);
                boolean flag_eventPrize = false;
                if(!flag_eventName && !flag_eventDate && !flag_organizationName && !flag_eventPrize)
                upload(eventName,eventDate,organizationName);


                if(eventPrize.equals("Select")) {
                    ((TextView)prize_won.getSelectedView()).setError("Please select one!");
                    flag_eventPrize = true;
                }



            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EventUpdater.class));
        finish();
    }
    private String upload(final String eventName, final String eventDate, final String organizationName) {
        if (filePath != null) {
            final android.app.ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("images/" + sharedPreferences.getInt("REG_NO", 0));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CompEvent.this, "Uploaded", Toast.LENGTH_SHORT).show();

                          ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_url=uri.toString();
                                    Log.e("Tuts+", "uri: " + uri.toString());
                                    StringRequest request = new StringRequest(Request.Method.POST, Constants.EVENT_URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("Event Updated")) {
                                                Toast.makeText(getApplicationContext(), "Event Included", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                                finish();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("COMPETITION_EVENT ERROR", error.toString());
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                                            params.put("eventName", eventName);
                                            params.put("eventDate", eventDate);
                                            params.put("organizationName", organizationName);
                                            params.put("eventPrize", prize_won.getSelectedItem().toString());
                                            params.put("competition_certificate_image_url",image_url);
                                            Log.d("Competition details : ", String.valueOf(params));
                                            return params;
                                        }
                                    };
                                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                                }


                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CompEvent.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
        return image_url;
    }

    private void chooseImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            if (filePath.toString().contains("image")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                event_comp_prize_certificate_info.setText("Selected");


            } else if (filePath.toString().contains("video")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                event_comp_prize_certificate_info.setText("Selected");

            }


        }

    }
}
