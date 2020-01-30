package com.remotemonster.sdktest.sample.Livestreaming.Profile;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.remon.sdktest.R;
import com.remotemonster.sdktest.sample.Livestreaming.Board.Board;
import com.remotemonster.sdktest.sample.Livestreaming.Board.ImageClick;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by samsung on 2019-04-16.
 */

public class Frag4 extends Fragment {
    View view;

    //이미지 뷰 선언
    ImageView profileimage = null;

    TextView idtext = null;
    TextView nametext = null;

    Button imgbtn;
    Button editbtn;

    String myid,imgname,id,name;

    //json으로 받아올 문자? 그리고 매칭 태그값
    String mJsonString;
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_IMAGE = "proimg";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    //카메라 모드 선택 번호
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int CROP_FROM_FACECAMERA = 3;

    //이미지 캠쳐시 저장될 uri인듯
    private Uri mImageCaptureUri;

    Uri ivuri;

    String absoultePath;
    String filePath, filename,fileurl;

    String upLoadServerUri = null;
    int serverResponseCode = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4, container, false);
        //레이아웃 선언
        profileimage = (ImageView)view.findViewById(R.id.profileimage);
        idtext = (TextView) view.findViewById(R.id.id);
        nametext = (TextView) view.findViewById(R.id.name);
        imgbtn = (Button)view.findViewById(R.id.imgbtn);
        editbtn = (Button)view.findViewById(R.id.editbtn);

        imgbtn.setOnClickListener(this::onClick);
        editbtn.setOnClickListener(this::onClick);

        //id값 쉐어드로 받아오기
        SharedPreferences idsdf = getActivity().getSharedPreferences("id", Activity.MODE_PRIVATE);
        myid = idsdf.getString("id", ""); //id넘어오도록

        //파일 업로드 php파일
        upLoadServerUri = "http://cho1719.vps.phps.kr/UploadToServer.php";//서버컴퓨터의 ip주소

        //gettask실행
        Frag4.GetData task = new Frag4.GetData();
        //num 조건 동일한거 가져오도록 설정해주기
        //우선 num.을 넘겨줘야겠네
        task.execute("http://cho1719.vps.phps.kr/profileimage.php", myid);


        return view;
    }

  //  @Override
    public void onClick(View v) {

        switch (v.getId()){
            //사진선택 버튼
            case R.id.imgbtn:
                //alert다이얼로그로 카메라,앨범,취소 선택지 제공
                //카메라 불러오기
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent faceintent = new Intent(Myprofile.this,facecamera.class);
//                        startActivityForResult(faceintent, CROP_FROM_FACECAMERA);
                        doTakePhotoAction();
                    }
                };
                //앨범 불러오기
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };
                //취소
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };

                new android.app.AlertDialog.Builder(v.getContext())
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();

                break;
            //완료버튼
            case  R.id.editbtn:
                //프로필 리퀘스트 클래스로 넘겨서 서버에 저장되도록
                new Thread(new Runnable() {

                    public void run() {
                        //뒷부분에 사진선택에 따른 프로필의 경로가 나오게됨
                        Log.d("프로필절대경로",absoultePath);
                        //파일의 정대경로를 업로드파일 메소드에 넘겨주고
                        //uploadtoserver.php파일을 통해서 저장되는 작업
                        uploadFile(absoultePath);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        };
                        ProfileRequest profileRequest = new ProfileRequest(myid,filename, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        queue.add(profileRequest);
                    }
                }).start();

              //  finish();
                break;
        }
    }

    //유저 아이디를 서버로 보내서 db작업하는 클래스
    //profile.php파일과 연결
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog= new ProgressDialog(getActivity());
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
//            progressDialog = ProgressDialog.show(getActivity().getApplicationContext(),
//                    "Please Wait", null, true, true);
        }


        //doinbackground의 결과값을 리턴받아 사용 즉. result = doinbackground의 결과값
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
                //php파일을 통해 디비값을 받아오기위한 메소드
                //즉 profile.php를 통해 돌려받은 값들을 mJsonString을 통해서 저장해두고 showReult에서 가공한다.
                showResult();
            }
        }


        //task의 실질적인 실행
        @Override
        protected String doInBackground(String... params) {

            //task 실행시 GetData(주소,아이디)입력하도록 선언한것
            //각 (0,1)자리 대로 배열로 사용
            String serverURL = params[0];
            // num 값 받아옴
            String userid = params[1];

            HashMap<String, String> nummap;

            nummap = new HashMap<>();
            //받아온 아이디를 해쉬맵에 키-밸류 구조로 사용
            nummap.put("userid", userid);

            //httpurlconnection 기능
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                //스트링 버퍼에 test라는 키값으로 userid 저장
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("test").append("=").append(userid);

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

                //onpostexeute의 파라미터값
                //아마도 파일업로드의 리턴값을 공백없이 저장한 변수일듯
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
            //jsonobject에 mjsonString의 값을 저장
            //json태그값으로 구분하여 배열로 저장한다.
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);


            for (int i = 0; i < jsonArray.length(); i++) {

                //item이라는 오브젝트에 배열의 값을 각각 순서대로 저장한다.
                JSONObject item = jsonArray.getJSONObject(i);

                //배열에 저장된값을 알맞은 키값으로 꺼내서 사용한다.
                imgname = item.getString(TAG_IMAGE);
                name = item.getString(TAG_NAME);

                nametext.setText(name);
                idtext.setText(myid);

                //이미지의 경우 디비에는 이미지의 이름만 저장해두었다가
                //이미지가 필요한 부분에 피카소 라이브러리로 해당 이름을 불러와서 사용한다.
                if(imgname.contains(".jpg")) {
                    Picasso.with(getActivity().getApplicationContext())
                            .load("http://cho1719.vps.phps.kr/uploads/"+imgname)
                            .into(profileimage);
                }
                else {
                    profileimage.setImageResource(R.drawable.ic_camera);
                }

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    //사진을 새로 찍을때 사용하는 메소드
    private void doTakePhotoAction() {
        /*
         * 참고 해볼곳
         * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
         * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
         * http://www.damonkohler.com/2009/02/android-recipes.html
         * http://www.firstclown.us/tag/android/
         */
        //사진찍는기능 부르는 인텐트
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        //절대경로
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        //절대경로 사진을 가져온다.
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        intent.putExtra("return-data", true);
        //이 경우 온 액티비티리설트의 값을 돌려받는다.
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        //이 경우 온 액티비티리설트의 값을 돌려받는다.
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //액티비티에 옮겨야되는듯
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                // 사진을 찍고 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                //이거그냥 쓰면 될듯?
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/";
                //이름  넘어오는지 확인
                //넘어오면 image uri  주소값부터 아래로 내려버리기
                //아닌가
                filename = Long.toString(System.currentTimeMillis()) + ".jpg";
                Log.d("파일경로", filePath);
                    Log.d("cropfromcamera data",data.toString());
                Log.d("cropfromcamera getextra",data.getExtras().toString());
          //      Log.d("extras?", extras.toString());
                //extras가 비어있다!!
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    //imgage uri 주소값
                    ivuri = getImageUri(getActivity(), photo);
                    profileimage.setImageURI(ivuri);
                    storeCropImage(photo, filePath + filename);
                    absoultePath = filePath+filename;

                    fileurl = ivuri.toString();

                    Log.d("프로필사진url값",fileurl);
                    Log.d("프로필사진url값",absoultePath);

                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                break;
            }

            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();

            }

            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                //크롭하는 부분으로 보낸다.
                Log.d("pickfromcamera data",data.toString());
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
            //얼굴인식카메라를 위해 넣어둔 부분
//            case CROP_FROM_FACECAMERA: {
//                String faceuri = data.getStringExtra("result");
//
//                String[] filenamelist=faceuri.split("/");
//                filename = filenamelist[5];
//                absoultePath = faceuri;
//
//                Log.d("프로필액티비티얼굴사진경로",filename);
//                Uri myUri = Uri.parse(faceuri);
//                Log.d("프로필액티비티얼굴사진경로",myUri.toString());
//                profileimage.setImageURI(myUri);

          //  }
        }
    }
    //사진을 찍고나서 이미지의  uri값을 받는 메소드
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("URI변환", path);
        return Uri.parse(path);
    }
    //크롭이미지 작업을 할때 저장되는 메소드
    private void storeCropImage(Bitmap bitmap, String filePath) {
        //smartwheel이라는 폴더에 이미지 저장
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if (!directory_SmartWheel.exists()) {
            directory_SmartWheel.mkdir();
        }
//저장된 파일경로를 copyfile에 저장
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
//copifile을 새로운 파일로 만들어서 저장
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//새로저장된 사진정보를 브로드캐스트로 바로 보이게끔 설정
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //프로필 사진 저장시 업로드
    public int uploadFile(String sourceFileUri) {

//저장될 파일의 이름은 파일의 경로
        final String fileName = sourceFileUri;


        HttpURLConnection conn = null;

        DataOutputStream dos = null;

        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;
//파일경로를 통해 파일 생성
        File sourceFile = new File(sourceFileUri);


        if (!sourceFile.isFile()) {



           getActivity().runOnUiThread(new Runnable() {

                public void run() {


                }

            });


            return 0;


        } else

        {

            try {


                // open a URL connection to the Servlet
                //파일경로를 통해 만들어진 파일을 인풋스트림으로 업로드
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                //서버 uri주소
                URL url = new URL(upLoadServerUri);


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


                    getActivity().runOnUiThread(new Runnable() {

                        public void run() {


                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"

                                    + fileName;


                            Toast.makeText(getActivity(), "File Upload Complete.",

                                    Toast.LENGTH_SHORT).show();

                        }

                    });

                }


                //close the streams //

                fileInputStream.close();

                dos.flush();

                dos.close();


            } catch (MalformedURLException ex) {

                ex.printStackTrace();


                getActivity().runOnUiThread(new Runnable() {

                    public void run() {


                        Toast.makeText(getActivity(), "MalformedURLException",

                                Toast.LENGTH_SHORT).show();

                    }

                });


                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {

                e.printStackTrace();


                getActivity().runOnUiThread(new Runnable() {

                    public void run() {


                        Toast.makeText(getActivity(), "Got Exception : see logcat ",

                                Toast.LENGTH_SHORT).show();

                    }

                });

                Log.e("Upload server Exception", "Exception : "

                        + e.getMessage(), e);

            }

            return serverResponseCode;


        } // End else block

    }

}