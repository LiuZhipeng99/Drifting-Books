package com.frist.drafting_books.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//在每个需要用的网络交互的activity里初始化一次，因为需要context//或者改为单例模式，全局只有一个queue,getInstance(ctx)但软件只有addbook才会调用故暂时不改
//在官方文档中，可以通过标记此act里的请求，在onStop函数里调用queue的cancel方法
public class VolleyHelper {
    public RequestQueue mQueue;
    private static String RTAG;
    public VolleyHelper(Context ctx) {
        mQueue = Volley.newRequestQueue(ctx);
        RTAG = ctx.getPackageName();
    }

    //构建请求，如string
    public void stringGETTEST(){
        String URL = "https://m.weathercn.com/citysearchajax.do";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volleyt", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volleyt",error.getMessage(),error);
            }
        }
        );
        stringRequest.setTag(RTAG);
        mQueue.add(stringRequest);
    }

    //目前仅支持扫码获取，以后可以加搜索获取
    //因为需要加header要自定义request
    public void jsonGETBOOK(String ISBN, GetBookFromNetCallback callback){
//        String URL = "http://api.feelyou.top/isbn/9787101052039";
        String URL = "http://api.feelyou.top/isbn/" + ISBN + "?apikey=OWHagO3Wmkt0FLaZJTtgHxCzGDxHt0Uu"; //吐了加header没用不如直接改url
//        String URL = "https://m.weathercn.com/citysearchajax.do";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley", response.toString());
                //多线程之回调
                callback.querySuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley",error.getMessage(),error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params = super.getHeaders(); //木大木大*2
                params.put("q","上海"); //都是木大木大
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("q","北京"); //post方面的
                return params;
            }
        };
        jsonRequest.setTag(RTAG);
        mQueue.add(jsonRequest);
    }

}
