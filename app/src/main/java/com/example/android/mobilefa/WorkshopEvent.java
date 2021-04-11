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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.UUID;

public class WorkshopEvent extends AppCompatActivity {

    EditText workshop_topic, organization_name, date_attended;
    Button workshop_submit;
    String image_url;
    SharedPreferences sharedPreferences;
    ImageView workshop_certificate_upload;
    TextView workshop_certificate_info;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

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
        setContentView(R.layout.activity_event_workshop);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        workshop_topic = findViewById(R.id.activity_event_workshop_topic_edittext);
        organization_name = findViewById(R.id.activity_event_workshop_clg_edittext);
        date_attended = findViewById(R.id.activity_event_workshop_date_edittext);
        workshop_submit = findViewById(R.id.activity_event_workshop_submit_button);
        workshop_certificate_info=findViewById(R.id.activity_workshop_course_upload_info);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
       workshop_certificate_upload = findViewById(R.id.activity_workshop_course_upload_button);
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

        date_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(WorkshopEvent.this, eventDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        workshop_certificate_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        workshop_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String workshopTopic = workshop_topic.getText().toString().trim();
                final String organizationName = organization_name.getText().toString().trim();
                final String dateAttended = date_attended.getText().toString().trim();
                Log.d("DAte from  workshop ",dateAttended);
                final boolean flag_workshopTopic = Empty(workshopTopic, workshop_topic);
                final boolean flag_organizationName = Empty(organizationName, organization_name);
                final boolean flag_dateAttended = Empty(dateAttended, date_attended);
                if(!flag_workshopTopic && !flag_organizationName && !flag_dateAttended)
                 upload(workshopTopic,organizationName,dateAttended);
                }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EventUpdater.class));
        finish();
    }
    synchronized private String upload(final String workshopTopic, final String organizationName, final String dateAttended) {
        if (filePath != null) {
            final android.app.ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(WorkshopEvent.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_url=uri.toString();

                                    Log.e("Tuts+", "uri: " + uri.toString());
                                    StringRequest request = new StringRequest(Request.Method.POST, Constants.WORKSHOP_URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("WorkShop event Updated")) {
                                                Toast.makeText(getApplicationContext(), "Workshop Included", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                                finish();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("WORKSHOP ERROR", error.toString());
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                                            params.put("workshopTopic", workshopTopic);
                                            params.put("organizationName", organizationName);
                                            params.put("workshopDate", dateAttended);
                                            params.put("workshop_certificate_image_url",image_url);
                                            Log.d("Workshop details : ", String.valueOf(params));
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
                            Toast.makeText(WorkshopEvent.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                workshop_certificate_info.setText("Selected");


            } else if (filePath.toString().contains("video")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                workshop_certificate_info.setText("Selected");

            }

        }

    }
}
