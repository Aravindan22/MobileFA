package com.example.android.mobilefa;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static  MySingleton mySingleton;
    private RequestQueue requestQueue;
    private Context context;

    public MySingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);
            Network network= new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache,network);
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return  requestQueue;
    }
    public static  synchronized  MySingleton getInstance(Context context){
        if(mySingleton == null){
            mySingleton = new MySingleton(context);
        }
        return  mySingleton;
    }
    public  <T> void  addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
