package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.util.Log;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2018-06-17.
 */

public class DeleteBoardRequest extends StringRequest {

    final static private String URL = "http://cho1719.vps.phps.kr/DeleteBoard.php";
    private Map<String, String> parametets;

    public DeleteBoardRequest(String num,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("DELETEREQUEST","IN"+num);
        parametets = new HashMap<>();
        parametets.put("num", num);

    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }

}
