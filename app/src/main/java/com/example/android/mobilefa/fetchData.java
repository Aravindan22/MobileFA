package com.example.android.mobilefa;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.android.mobilefa.Constants.FETCHDATA_URL;

class fetchData {
    public void getCreds(final String regno, Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, FETCHDATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Constants.REG_NO = Integer.parseInt(String.valueOf(jsonObject.get("Student_RegisterNo")));
                    Constants.PASSWORD = jsonObject.getString("Student_Password");
                    Constants.DEP = jsonObject.getString("Student_Dept");
                    Constants.EMAIL = jsonObject.getString("Student_EmailID");
                    Constants.NAME = jsonObject.getString("Student_Name");
                    Constants.YEAR = Integer.parseInt(jsonObject.getString("Student_Year"));
                    Constants.SECTION = jsonObject.getString("Student_Section");
//                    Log.d("FetchedData", Constants.EMAIL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fetchdata", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("regno", regno);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
