package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.remon.sdktest.R;
import com.squareup.picasso.Picasso;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by samsung on 2018-06-11.
 */

public class Board extends AppCompatActivity implements View.OnClickListener{

    String mJsonString,mJsonString2;

    ArrayList<HashMap<String, String>> mArrayList;
    ListView m_ListView;

    RelpleAdapter m_Adapter;

    TextView nameview = null;
    TextView titleview = null;
    TextView timeview = null;
    TextView contentview = null;

    EditText repleview = null;

    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,proimgview;

    String name, title, content, time, image1,image2,image3,image4,image5,proimg ;

    String rename,recontent,retime,reimg;

    Button edibtn,replebtn,deletebtn;

    String uname;

    String boardnum;

    private AlertDialog dialog;

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_JSON2 = "webnautes2";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_TIME = "nowdate";
    private static final String TAG_IMAGE1 = "image1";
    private static final String TAG_IMAGE2 = "image2";
    private static final String TAG_IMAGE3 = "image3";
    private static final String TAG_IMAGE4 = "image4";
    private static final String TAG_IMAGE5 = "image5";
    private static final String TAG_PROFILE = "proimg";


    HashMap<String, String> hashMap,rehashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        String num = intent.getStringExtra("itemnum");
        boardnum = num;
        // Log.d("아이템의 auto_key값",num);

        SharedPreferences idsdf = getSharedPreferences("id", Activity.MODE_PRIVATE);
        uname = idsdf.getString("id", ""); //id넘어오도록

        GetData task = new GetData();
        //num 조건 동일한거 가져오도록 설정해주기
        //우선 num.을 넘겨줘야겠네
        task.execute("http://cho1719.vps.phps.kr/BoardContent0.php", num);

        m_ListView = (ListView) findViewById(R.id.listView1);

        nameview = (TextView) findViewById(R.id.name);
        titleview = (TextView) findViewById(R.id.title);
        contentview = (TextView) findViewById(R.id.content);
        timeview = (TextView) findViewById(R.id.time);

        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);
        imageView5 = (ImageView)findViewById(R.id.imageView5);
        proimgview = (ImageView)findViewById(R.id.proimg);

        repleview = (EditText)findViewById(R.id.reple);

        edibtn = (Button)findViewById(R.id.edtbtn);
        replebtn = (Button)findViewById(R.id.replebtn);
        deletebtn = (Button)findViewById(R.id.deltebtn) ;

        nameview.setText("test");
        titleview.setText("test 글 제목입니다.");
        contentview.setText("test 글 내용입니다.");
        timeview.setText(" ");
        imageView1.setImageResource(R.drawable.one);
        imageView2.setImageResource(R.drawable.two);
        imageView3.setImageResource(R.drawable.three);
        imageView4.setImageResource(R.drawable.four);
        imageView5.setImageResource(R.drawable.five);
        proimgview.setImageResource(R.drawable.kakao);


        edibtn.setOnClickListener(this);
        replebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);


//        nameview.setText(name);
//        titleview.setText(title);
//        contentview.setText(content);
//        titleview.setText(time);


    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Board.this,
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


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                name = item.getString(TAG_ID);
                title = item.getString(TAG_TITLE);
                content = item.getString(TAG_CONTENT);
                time = item.getString(TAG_TIME);
                image1 = item.getString(TAG_IMAGE1);
                image2 = item.getString(TAG_IMAGE2);
                image3 = item.getString(TAG_IMAGE3);
                image4 = item.getString(TAG_IMAGE4);
                image5 = item.getString(TAG_IMAGE5);
                proimg = item.getString(TAG_PROFILE);
                hashMap = new HashMap<>();

                hashMap.put(TAG_ID, name);
                hashMap.put(TAG_TITLE, title);
                hashMap.put(TAG_CONTENT, content);
                hashMap.put(TAG_TIME, time);


                nameview.setText("test");
                titleview.setText("test 글 제목입니다.");
                contentview.setText("test 글 내용입니다.");
                timeview.setText(" ");
                Picasso.with(Board.this)
                        .load("http://cho1719.vps.phps.kr/uploads/"+proimg)
                        .into(proimgview);

                if(image1.contains(".jpg")) {
                    Picasso.with(Board.this)
                            .load("http://cho1719.vps.phps.kr/uploads/"+image1)
                            .into(imageView1);
                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent Imageclick1 = new Intent(Board.this,ImageClick.class);
                            Imageclick1.putExtra("image1",image1);
                            Imageclick1.putExtra("image2",image2);
                            Imageclick1.putExtra("image3",image3);
                            Imageclick1.putExtra("image4",image4);
                            Imageclick1.putExtra("image5",image5);
                            startActivity(Imageclick1);
                        }
                    });
                }
                else{
                    imageView1.setVisibility(View.GONE);
                }
                if(image2.contains(".jpg")) {
                    Picasso.with(Board.this)
                            .load("http://cho1719.vps.phps.kr/uploads/"+image2)
                            .into(imageView2);
                }
                else{
                    imageView2.setVisibility(View.GONE);
                }
                if(image3.contains(".jpg")) {
                    Picasso.with(Board.this)
                            .load("http://cho1719.vps.phps.kr/uploads/"+image3)
                            .into(imageView3);
                }
                else{
                    imageView3.setVisibility(View.GONE);
                }
                if(image4.contains(".jpg")) {
                    Picasso.with(Board.this)
                            .load("http://cho1719.vps.phps.kr/uploads/"+image4)
                            .into(imageView4);
                }
                else{
                    imageView4.setVisibility(View.GONE);
                }
                if(image5.contains(".jpg")) {
                    Picasso.with(Board.this)
                            .load("http://cho1719.vps.phps.kr/uploads/"+image5)
                            .into(imageView5);
                }
                else{
                    imageView5.setVisibility(View.GONE);
                }
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private class GetData2 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Board.this,
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
                //여기에 리퀘스트 넣어야되나?
                //five frag에서 num정보 받고 인텐트로 여기로 넘겨주도록 하자
                //여까지온다
                Log.d("온포스트익스큐트", "!!!");
                mJsonString2 = result;
                showResult2();
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

    private void showResult2() {
        try {
            JSONObject jsonObject2 = new JSONObject(mJsonString2);
            JSONArray jsonArray2 = jsonObject2.getJSONArray(TAG_JSON2);

            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject item = jsonArray2.getJSONObject(i);

                rename = item.getString(TAG_ID);
                recontent = item.getString(TAG_CONTENT);
                reimg = item.getString(TAG_PROFILE);
                retime = item.getString(TAG_TIME);
                rehashMap = new HashMap<>();


                rehashMap.put(TAG_ID, rename);
                rehashMap.put(TAG_CONTENT, recontent);
                rehashMap.put(TAG_PROFILE, reimg);
                rehashMap.put(TAG_TIME, retime);

//어레이 리스트에 배열 집어넣고
                //텍스트에 입력한거 db에 저장하는 부분 아직 안함
              //  Log.d("쇼리설트투", "!!!");
                mArrayList.add(rehashMap);
                refresh(mArrayList);

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    public void onClick(View v){
        Intent numberintent = null;

        switch (v.getId()){
            case  R.id.edtbtn:
                Log.d("유저아이디",uname);
                Log.d("글쓴이",nameview.getText().toString());
                if(uname.equals(nameview.getText().toString())) {
                    numberintent = new Intent(this, EditBoard.class);
                    numberintent.putExtra("itemnum", boardnum);
                    startActivity(numberintent);
                    finish();
                }
                else{
                    Toast.makeText(Board.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.deltebtn:
                if(uname.equals(nameview.getText().toString())) {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    };
                    DeleteBoardRequest deleteBoardRequest = new DeleteBoardRequest(boardnum, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Board.this);
                    queue.add(deleteBoardRequest);
                    Toast.makeText(Board.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(Board.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.replebtn:

                String nowDate;
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
                nowDate = sdf.format(date);


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//                            if (success) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(Board.this);
//                                dialog = builder.setMessage("댓글 작성")
//                                        .setPositiveButton("확인", null)
//                                        .create();
//                                dialog.show();
//                                //  NewRoom.this.startActivity(intent);
//                                finish();
//                            } else {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(Board.this);
//                                dialog = builder.setMessage("댓글 작성 실패")
//                                        .setNegativeButton("다시 시도", null)
//                                        .create();
//                                dialog.show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                };
                RepleRequest repleRequest = new RepleRequest(uname,repleview.getText().toString(),boardnum,nowDate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Board.this);
                queue.add(repleRequest);
                mArrayList = new ArrayList<>();
                m_Adapter = new RelpleAdapter();
                m_ListView.setAdapter(m_Adapter);
                GetData2 task2 = new GetData2();
                task2.execute("http://cho1719.vps.phps.kr/RepleContent0.php", boardnum);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("보드온리쥼","!!!!!!!");
        mArrayList = new ArrayList<>();
        m_Adapter = new RelpleAdapter();
        m_ListView.setAdapter(m_Adapter);
        GetData2 task2 = new GetData2();
        task2.execute("http://cho1719.vps.phps.kr/RepleContent0.php", boardnum);
    }

    private void refresh(ArrayList<HashMap<String, String>> inputValue) {
        Log.d("댓글리프레시", inputValue.toString());
        m_Adapter.add(inputValue);
        m_Adapter.notifyDataSetChanged();
        m_ListView.setSelection(0);
    }

}
