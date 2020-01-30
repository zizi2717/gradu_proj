package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2018-05-17.
 */

public class NewBoardRequest extends StringRequest {

    final static private String URL = "http://cho1719.vps.phps.kr/Board0.php";
    private Map<String, String> parametets;

    public NewBoardRequest(String ID, String BoardTitle, String BoardContent, String Date,String image1,String image2,String image3,String image4,String image5,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("게시글만들기 리퀘스트 확인!!",ID);
        Log.d("게시글만들기 리퀘스트 확인!!",BoardContent);
        Log.d("게시글만들기 리퀘스트 확인!!",BoardTitle);
        Log.d("게시글만들기 리퀘스트 확인!!",Date);
        Log.d("게시글만들기 리퀘스트 확인!!",image1);
        parametets = new HashMap<>();

        parametets.put("userID", ID);
        parametets.put("BoardTitle",BoardTitle);
        parametets.put("BoardContent",BoardContent);
        parametets.put("Date","test");
        parametets.put("Image1","test");
        parametets.put("Image2","test");
        parametets.put("Image3","test");
        parametets.put("Image4","test");
        parametets.put("Image5","test");


    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }
}