package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2018-06-18.
 */

public class EditBoardRequest extends StringRequest {

    final static private String URL = "http://cho1719.vps.phps.kr/EditBoard.php";
    private Map<String, String> parametets;

    public EditBoardRequest(String num,String ID, String BoardTitle, String BoardContent, String Date,String image1,String image2,String image3,String image4,String image5,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("게시글수정 리퀘스트 확인!!",image1);
        Log.d("게시글수정 리퀘스트 확인!!",BoardContent);
        parametets = new HashMap<>();
        parametets.put("num", num);
        parametets.put("userID", ID);
        parametets.put("BoardTitle",BoardTitle);
        parametets.put("BoardContent",BoardContent);
        parametets.put("Date",Date);
        parametets.put("Image1",image1);
        parametets.put("Image2",image2);
        parametets.put("Image3",image3);
        parametets.put("Image4",image4);
        parametets.put("Image5",image5);


    }

    @Override
    public Map<String, String> getParams(){
        return  parametets;
    }
}