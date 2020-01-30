package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.remon.sdktest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * Created by samsung on 2019-04-16.
 */

public class Frag3 extends Fragment implements View.OnClickListener {
    ArrayList<HashMap<String, String>> mArrayList;
    Map<String, String> nummap = new HashMap<>();

    ListView m_ListView;

    BoardAdapter m_Adapter;

    Button addboard;

    String mJsonString;

    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_TIME = "nowdate";
    private static final String TAG_NUM = "num";
    private static final String TAG_IMAGE = "proimg";


    HashMap<String, String> hashMap;

    public Frag3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.frag3, container, false);

//        mArrayList = new ArrayList<>();
//
////        m_Adapter = new BoardAdapter();

//
        m_ListView = (ListView) layout.findViewById(R.id.listView1);

        addboard = (Button) layout.findViewById(R.id.addboard);

//        m_ListView.setAdapter(m_Adapter);


        //implement viewonclicklisener
        addboard.setOnClickListener(this);


        return layout;
    }



    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.addboard:
                intent = new Intent(getActivity(), NewBoard.class);
        }
        startActivityForResult(intent, 555);

    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //   mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null) {

                //   mTextViewResult.setText(errorString);
            } else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            String id, title, content, time, num,image;

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                id = item.getString(TAG_ID);
                title = item.getString(TAG_TITLE);
                content = item.getString(TAG_CONTENT);
                time = item.getString(TAG_TIME);
                num = item.getString(TAG_NUM);
                image = item.getString(TAG_IMAGE);

                hashMap = new HashMap<>();

                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_TITLE, title);
                hashMap.put(TAG_CONTENT, content);
                hashMap.put(TAG_TIME, time);
                hashMap.put(TAG_NUM, num);
                hashMap.put(TAG_IMAGE, image);
                Log.d("여기오나!!!!!!!!!!", hashMap.get(TAG_TITLE));
                mArrayList.add(hashMap);
                Log.d("널포인트체크!!!!!!!!!!", mArrayList.toString());
                refresh(mArrayList);
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void refresh(ArrayList<HashMap<String, String>> inputValue) {
        Log.d("널포인트체크!!!!!!!!!!", inputValue.toString());
        m_Adapter.add(inputValue);
        m_Adapter.notifyDataSetChanged();
        m_ListView.setSelection(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mArrayList = new ArrayList<>();

//        m_Adapter = new BoardAdapter();
        m_Adapter = new BoardAdapter();
        m_ListView.setAdapter(m_Adapter);
        GetData task = new GetData();
        task.execute("http://cho1719.vps.phps.kr/BoardList0.php");
    }

    private class GetData2 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //   mTextViewResult.setText(result);
            //Log.d(TAG, "response  - " + result);

            if (result == null) {

                //   mTextViewResult.setText(errorString);
            } else {
                //여기에 리퀘스트 넣어야되나?
                //five frag에서 num정보 받고 인텐트로 여기로 넘겨주도록 하자
                //여까지온다
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            // num 값 받아옴
            String number = params[1];

            HashMap<String, String> nummap;

            nummap = new HashMap<>();

            nummap.put("num", number);


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("test").append("=").append(number);

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                pw.write(stringBuffer.toString());
                pw.flush();

                int responseStatusCode = httpURLConnection.getResponseCode();
                //   Log.d(TAG, "response code - " + responseStatusCode);


                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                //    Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult2() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

//                userid = item.getString(TAG_ID);
//                profileimg = item.getString(TAG_IMAGE);
//                hashMap.put(userid,profileimg);
                // Log.d("채팅어뎁터프로필이미지이름",profileimg);

                Log.d("쇼리설트userid",hashMap.toString());
            }

        } catch (JSONException e) {

            // Log.d(TAG, "showResult : ", e);
        }

    }
}