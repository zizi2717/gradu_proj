package com.remotemonster.sdktest.sample.Livestreaming.Login;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2018-04-23.
 */

public class LoginRequest extends StringRequest {

    // Login.php파일로 통신
    final static private String URL = "http://cho1719.vps.phps.kr/Login.php";
    //파라메터 형식으로보냄
    private Map<String, String> parametets;


    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        Log.d("회원가입 리퀘스트 확인!!",userID);
        //php파일에서 키값을 읽고 데이터받을수 있도록함
        parametets = new HashMap<>();
        parametets.put("userID", userID);
        parametets.put("userPassword",userPassword);


    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }
}
