package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2018-06-18.
 */

public class RepleRequest extends StringRequest {

    final static private String URL = "http://cho1719.vps.phps.kr/Reple.php";
    private Map<String, String> parametets;

    public RepleRequest(String ID, String Content,String Boardnum,String Date,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        Log.d("댓글만들기 리퀘스트 확인!!",ID);
        Log.d("댓글만들기 리퀘스트 확인!!",Content);
        Log.d("댓글만들기 리퀘스트 확인!!",Date);
        Log.d("댓글만들기 리퀘스트 확인!!",Boardnum);
        parametets = new HashMap<>();
        parametets.put("userID", ID);
        parametets.put("RepleContent",Content);
        parametets.put("Boardnum",Boardnum);
        parametets.put("Date",Date);



    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }
}