package com.remotemonster.sdktest.sample.Livestreaming.Login;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2019-03-29.
 */

public class SignupRequest extends StringRequest {
    final static private String URL = "http://cho1719.vps.phps.kr/Signup.php";
    private Map<String, String> parametets;

    public SignupRequest(String userID, String userPassword, String userName, String studentNum, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("회원가입 리퀘스트 확인!!","회원가입 리퀘스트 확인");
        parametets = new HashMap<>();
        parametets.put("userID", userID);
        parametets.put("userPassword",userPassword);
        parametets.put("userName", userName);
        parametets.put("studentNum", studentNum);


    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }

}