package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.remon.sdktest.R;
import com.remotemonster.sdktest.sample.Livestreaming.MainActivity;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gun0912.tedbottompicker.TedBottomPicker;


/**
 * Created by samsung on 2018-06-11.
 */

public class NewBoard extends AppCompatActivity {

    EditText boardtitle, boardcontent;
    String id, todaytime, BoardTitle, BoardContent;
    Button crebtn;

    int FOUR = 1;

    private AlertDialog dialog;

    String nowDate;

    ImageView iv_image;
    ArrayList<Uri> selectedUriList;
    private ViewGroup mSelectedImagesContainer;
    private RequestManager requestManager;


    int serverResponseCode = 0;

    ProgressDialog dialog2 = null;


    String upLoadServerUri = null;


    /**********  File Path *************/

    final String uploadFilePath = "storage/emulated/0/DCIM/Camera/";//경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보 "storage/emulated/0/DCIM/Camera/"

    final String uploadFileName = "20191113_170421.jpg"; //전송하고자하는 파일 이름

    String lastPath;

    ArrayList<String> namearr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newboard);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        boardtitle = (EditText) findViewById(R.id.boardtitle);
        boardcontent = (EditText) findViewById(R.id.boardcontent);

        crebtn = (Button) findViewById(R.id.addbtn);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
        nowDate = sdf.format(date);

        final SharedPreferences idsdf = getSharedPreferences("id", Activity.MODE_PRIVATE);


        iv_image = (ImageView) findViewById(R.id.iv_image);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        requestManager = Glide.with(this);

        setMultiShowButton();

        upLoadServerUri = "http://cho1719.vps.phps.kr/UploadToServer.php";//서버컴퓨터의 ip주소

        crebtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardTitle = boardtitle.getText().toString();
                BoardContent = boardcontent.getText().toString();
                id = idsdf.getString("id", "");

                if (selectedUriList != null) {

                    dialog2 = ProgressDialog.show(NewBoard.this, "", "Uploading file...", true);

                    new Thread(new Runnable() {

                        public void run() {

                            // selectedUriList(i).toString() = Path+Name
                            // file:///을 떼고 넣어야함..
                            //0 = file: 1=/ 2=/ 3=/ 4=path
                            //for문으로 i조건 주고 반복돌리면 될것같다
                            Log.d("사이즈찾기", Integer.toString(selectedUriList.size()));
                            for (int i = 0; i < selectedUriList.size(); i++) {
                                lastPath = selectedUriList.get(i).toString();
                                Log.d("beforelastPath", lastPath);
                                String usepath = lastPath.substring(8);
                                Log.d("afterlastPath", usepath);
                                uploadFile(usepath);
                            }
                        }

                    }).start();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("게시글 response",response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("boardsuccess");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewBoard.this);
                                dialog = builder.setMessage("게시글 작성")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                Intent intent = new Intent(NewBoard.this, MainActivity.class);
                                setResult(333, intent);
                                //  NewRoom.this.startActivity(intent);
                                dialog.dismiss();
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewBoard.this);
                                dialog = builder.setMessage("게시글 작성 실패")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                if(selectedUriList!=null) {
                    if (selectedUriList.size() < 6) {
                        while (selectedUriList.size() < 6) {
                            String a = "NULLNULLNULL";
                            Uri myuri = Uri.parse(a);
                            Log.d("uri확인", myuri.toString());
                            selectedUriList.add(myuri);
                        }
                        for (int i = 0; i < selectedUriList.size(); i++) {
                            Log.d("selecturi", selectedUriList.get(0).toString());
                        }
                        for (int i = 0; i < selectedUriList.size(); i++) {
                            if (selectedUriList.get(i).toString().contains("/")) {
                                Log.d("이미지있을때들어와", "이미지 있을때 들어와");
                                //이미지의 경로 저장될 공간
                                String[] namelist = selectedUriList.get(i).toString().split("/");
                                //이미지 경로에서 이름 위치 찾는것
                                int imagesize = namelist.length;
                                //이미지의 이름만 저장할 공간
                                String img0 = namelist[imagesize - 1];
                                namearr.add(img0);
                            } else {
                                Log.d("이미지없을때들어와", "이미지 없을때 들어와");
                                String img0 = "NULL";
                                namearr.add(img0);
                            }
                        }

                    }
                }
                else{
                    while (namearr.size()<6) {
                        String img0 = "NULL";
                        namearr.add(img0);
                    }
                }
                NewBoardRequest newBoardRequest = new NewBoardRequest(id, BoardTitle, BoardContent, nowDate,namearr.get(0), namearr.get(1), namearr.get(2), namearr.get(3), namearr.get(4), responseListener);
                RequestQueue queue = Volley.newRequestQueue(NewBoard.this);
                queue.add(newBoardRequest);
            }
        }));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void setMultiShowButton() {

        //사진 불러오기 버튼
        Button btn_multi_show = (Button) findViewById(R.id.btn_multi_show);
        //클릭이벤트 리스너
        btn_multi_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //권한 허가 혹은 거부를 알려주는 리스너
                //박상권의 삽질 블로그에서 받아온것,
                PermissionListener permissionlistener = new PermissionListener() {
                    //퍼미션 허가시..
                    @Override
                    public void onPermissionGranted() {
                        //tedbottompicker라이브러리
                        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(NewBoard.this)
                                .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                                    @Override
                                    public void onImagesSelected(ArrayList<Uri> uriList) {
                                        //url리스트 담는 부분
                                        selectedUriList = uriList;

                                        //이 로그가 done클릭하고 나서 보여집니다.
                                        //이 위치에서 파일 올려도 괜찮을듯??
                                        Log.d("url리스트", selectedUriList.toString());
                                        // Log.d("url리스트",selectedUriList(0).toString());
                                        //담은 리스트 보여주는 부분
                                        showUriList(uriList);

                                    }
                                })
                                //.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
                                //아마도 다이얼로그 설정
                                .setPeekHeight(1600)
                                .showTitle(false)
                                .setCompleteButtonText("Done")
                                .setEmptySelectionText("No Select")
                                .setSelectedUriList(selectedUriList)
                                .create();

                        bottomSheetDialogFragment.show(getSupportFragmentManager());


                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(NewBoard.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };

                TedPermission.with(NewBoard.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();

            }
        });

    }


    private void showUriList(ArrayList<Uri> uriList) {
        // Remove all views before
        // adding the new ones.

        //담은 url 이미지로 보여주는 부분
        //아마도... 선택한 이미지 보여주는 컨테이너같음..
        mSelectedImagesContainer.removeAllViews();

        iv_image.setVisibility(View.GONE);
        mSelectedImagesContainer.setVisibility(View.VISIBLE);

        //실제 보여지는 이미지 뷰 크기
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (Uri uri : uriList) {

            //이미지 홀더
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            requestManager
                    .load(uri.toString())
                    .apply(new RequestOptions().fitCenter())
                    .into(thumbnail);
            //여기가 실제로 보여지는 이미지 연결 부분
            mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


        }

    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;


        HttpURLConnection conn = null;

        DataOutputStream dos = null;

        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(sourceFileUri);


        if (!sourceFile.isFile()) {


            dialog2.dismiss();


            Log.e("uploadFile", "Source File not exist :"

                    + uploadFilePath + "" + uploadFileName);


            runOnUiThread(new Runnable() {

                public void run() {


                }

            });


            return 0;


        } else

        {

            try {


                // open a URL connection to the Servlet

                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(upLoadServerUri);

                Log.d("파일경로확인",sourceFileUri);
                Log.d("url확인",upLoadServerUri);


                // Open a HTTP  connection to  the URL

                conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true); // Allow Inputs

                conn.setDoOutput(true); // Allow Outputs

                conn.setUseCaches(false); // Don't use a Cached Copy

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");

                conn.setRequestProperty("ENCTYPE", "multipart/form-data");

                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("uploaded_file", fileName);


                dos = new DataOutputStream(conn.getOutputStream());


                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""

                        + fileName + "\"" + lineEnd);


                dos.writeBytes(lineEnd);


                // create a buffer of  maximum size

                bytesAvailable = fileInputStream.available();


                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                buffer = new byte[bufferSize];


                // read file and write it into form...

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                while (bytesRead > 0) {


                    dos.write(buffer, 0, bufferSize);

                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                }


                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();

                String serverResponseMessage = conn.getResponseMessage();


                Log.i("uploadFile", "HTTP Response is : "

                        + serverResponseMessage + ": " + serverResponseCode);


                if (serverResponseCode == 200) {


                    runOnUiThread(new Runnable() {

                        public void run() {


                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"

                                    + uploadFileName;


                            Toast.makeText(NewBoard.this, "File Upload Complete.",

                                    Toast.LENGTH_SHORT).show();

                        }

                    });

                }


                //close the streams //

                fileInputStream.close();

                dos.flush();

                dos.close();


            } catch (MalformedURLException ex) {


                dialog2.dismiss();

                ex.printStackTrace();


                runOnUiThread(new Runnable() {

                    public void run() {


                        Toast.makeText(NewBoard.this, "MalformedURLException",

                                Toast.LENGTH_SHORT).show();

                    }

                });


                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {


                dialog2.dismiss();

                e.printStackTrace();


                runOnUiThread(new Runnable() {

                    public void run() {


                        Toast.makeText(NewBoard.this, "Got Exception : see logcat ",

                                Toast.LENGTH_SHORT).show();

                    }

                });

                Log.e("Upload server Exception", "Exception : "

                        + e.getMessage(), e);

            }

            dialog2.dismiss();

            return serverResponseCode;


        } // End else block

    }


}
