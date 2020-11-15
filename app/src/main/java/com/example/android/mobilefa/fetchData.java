package com.example.android.mobilefa;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
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

    SharedPreferences sharedPreferences;

    private void storeDetails(Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGGED_IN", true);
        editor.putInt("REG_NO", Constants.REG_NO);
        editor.putString("NAME", Constants.NAME);
        editor.putString("DEP", Constants.DEP);
        editor.putInt("YEAR", Constants.YEAR);
        editor.putString("SECTION", Constants.SECTION);
        editor.putString("EMAIL", Constants.EMAIL);
        editor.putString("PASSWORD", Constants.PASSWORD);
        editor.commit();

        Toast.makeText(context, String.valueOf(sharedPreferences.getString("EMAIL","")), Toast.LENGTH_SHORT).show();

    }

    public boolean getCreds(final String regno, final Context context) {
        sharedPreferences = context.getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, FETCHDATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Response Fetch Data||{}",
                            jsonObject.getInt("reg_no")+"::::::" +
                                    jsonObject.getString("password")+"::::::"+
                                    jsonObject.getString("dept")+"::::::"+
                                    jsonObject.getString("mail")+"::::::"+
                                    jsonObject.getString("name")+"::::::"+
                                    jsonObject.getInt("year")+"::::::"+
                                    jsonObject.getString("section")
                            );
                    Constants.REG_NO = jsonObject.getInt("reg_no");
                    Constants.PASSWORD = jsonObject.getString("password");
                    Constants.DEP = jsonObject.getString("dept");
                    Constants.EMAIL = jsonObject.getString("mail");
                    Constants.NAME = jsonObject.getString("name");
                    Constants.YEAR = jsonObject.getInt("year");
                    Constants.SECTION = jsonObject.getString("section");
                    Log.d("FetchData 2 Constants:" ,Constants.REG_NO+" "+Constants.PASSWORD+" "+Constants.DEP+" "+Constants.EMAIL+" "+Constants.NAME+" "+Constants.YEAR+" "+Constants.SECTION+" ");
                    storeDetails(context);
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
                Log.d("Fetch Data: ",regno);
                params.put("regno", regno);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

        return true;
    }

}
