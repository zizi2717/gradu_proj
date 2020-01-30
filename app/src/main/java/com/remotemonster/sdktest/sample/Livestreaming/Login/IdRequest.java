package com.remotemonster.sdktest.sample.Livestreaming.Login;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2019-03-29.
 */

public class IdRequest extends StringRequest {

    final static private String URL = "http://cho1719.vps.phps.kr/UserValidate.php";
    private Map<String, String> parametets;

    public IdRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("아이디중복체크확인!!","아이디중복체크확인");
        parametets = new HashMap<>();
        parametets.put("userID", userID);

    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }
}
