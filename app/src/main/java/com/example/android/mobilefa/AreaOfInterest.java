package com.example.android.mobilefa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.mobilefa.Constants.*;

public class AreaOfInterest extends AppCompatActivity {
EditText aoi_description;
Button aoi_upload_media,aoi_submit_button;
TextView aoi_upload_info;
Spinner aoi_category_spinner;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    String image_url;
    SharedPreferences sharedPreferences ;
    protected boolean Empty(String s, EditText et) {
        if(TextUtils.isEmpty(s)) {
            et.setError("This field cannot be empty!");
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_of_interest);
        sharedPreferences = getApplicationContext().getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        aoi_category_spinner = findViewById(R.id.activity_aoi_category_spinner);
        aoi_description = findViewById(R.id.activity_aoi_description_edittext);
        aoi_upload_media = findViewById(R.id.activity_aoi_uploadmedia_button);
        aoi_submit_button = findViewById(R.id.activity_aoi_submit_Button);
        //aoi_upload_info=findViewById(R.id.activity_aoi_upload_info);

        final ArrayList<String> categoryArrayList = new ArrayList<String>();
        categoryArrayList.add("Select");

        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] S = response.split(",");
                for (String s: S) {
                    categoryArrayList.add(s);
                }
                Log.d("Categories.........", String.valueOf(categoryArrayList));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryArrayList);

        aoi_category_spinner.setAdapter(categoryAdapter);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

      aoi_submit_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final String Category = aoi_category_spinner.getSelectedItem().toString();
              final String Description = aoi_description.getText().toString().trim();
              final boolean flag_Description = Empty(Description, aoi_description);
              boolean flag_Category = false;
              if(Category.equals("Select")) {
                  ((TextView) aoi_category_spinner.getSelectedView()).setError("Please select one!");
                  flag_Category = true;
              }
              if(!flag_Category && !flag_Description)
                  upload(Category,Description);
          }
      });

        aoi_upload_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    private String upload(final String Category, final String Description) {
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
                            Toast.makeText(AreaOfInterest.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_url=uri.toString();
                                    Log.e("Tuts+", "uri: " + uri.toString());
                                    StringRequest request = new StringRequest(Request.Method.POST, AOI_URL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("Area of Interest Updated")) {
                                                Toast.makeText(getApplicationContext(), "Activity Included", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), ChoosingActivity.class));
                                                finish();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("AOI_EVENT ERROR", error.toString());
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("regno", String.valueOf(sharedPreferences.getInt("REG_NO",0)));
                                            params.put("Aoi_Category", Category.toLowerCase());
                                            params.put("Aoi_Description", Description);
                                            params.put("Aoi_media_image_url",image_url);
                                            Log.d("Aoi details : ", String.valueOf(params));
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
                            Toast.makeText(AreaOfInterest.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

            /*if (filePath.toString().contains("image")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                aoi_upload_info.setText("Selected");


            } else if (filePath.toString().contains("video")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
               aoi_upload_info.setText("Selected");

            }*/
        }

        }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ChoosingActivity.class));
        finish();
    }
}