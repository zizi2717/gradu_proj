package com.remotemonster.sdktest.sample.Livestreaming.Profile;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2018-06-19.
 */

public class ProfileRequest extends StringRequest {

    final static private String URL = "http://cho1719.vps.phps.kr/Updateprofile.php";
    private Map<String, String> parametets;

    public ProfileRequest(String ID, String image, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("게시글만들기 리퀘스트 확인!!",image);
        parametets = new HashMap<>();
        parametets.put("id",ID);
        parametets.put("image",image);

    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }
}