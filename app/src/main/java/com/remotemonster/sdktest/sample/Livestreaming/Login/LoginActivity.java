package com.remotemonster.sdktest.sample.Livestreaming.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.remon.sdktest.R;
import com.remotemonster.sdktest.sample.Livestreaming.MainActivity;

import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samsung on 2019-03-26. HANSOL CHO
 * 로그인 및 회원가입 액티비티 여기 통해서 회원가입 액티비티나 메인액티비티 진입
 */

public class LoginActivity extends AppCompatActivity {

    //로그인 결과 나타내기 위해 다이어로그 선언
    private AlertDialog dialog;
    //자동로그인하기위한 꼼수를 위한 변수
    //false일때는 로그인하게하려고 선언한 변수
    String logincheck = "false";

    String userID;
    String userPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //아이디 비밀번호 입력하는 텍스트창 선언
        final EditText w_id = (EditText) findViewById(R.id.w_id);
        final EditText w_pw = (EditText) findViewById(R.id.w_pw);

        //로그인 버튼
        Button btn_login = (Button) findViewById(R.id.btn_login);
        //회원가입 버튼 텍스트뷰에 클릭속성줌
        TextView btn_signup = (TextView) findViewById(R.id.btn_signup);

        //SharedPreferences에  저장된 값 받아와야함
        //이때 key값은 "auto"로 저장했음
        // -> 이건 아래 로그인할떄 shared에 auto라는 이름으로 셰어드 선언했었음
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        // -> 아래 로그인할때 shared로 autologin이라는 key에 true를 입력시켜둠
        // 로그인을 한번도 안한 상태면 autologin값이 없이때문에 logincheck 기본값이 false 가 저장됨
        // 한번이라도 했으면 logincheck에 autologin값인 ture가 저장됨.
        logincheck = auto.getString("autologin", "false");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        //한번이라도 로그인을 했다면 logincheck = true이므로 if문 진입
        //바로 액티비티 넘어가도록 선언


        if (logincheck.equals("true")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent);
            finish();
        }
        //로그아웃을 통해 shared값을 false로 바꿧거나 한번도 로그인안했을경우엔 false값을 가짐
        //else문 진입
        else {

            //회원가입 버튼 리스너 이벤트
            btn_signup.setOnClickListener(new View.OnClickListener() {
                //클릭시 회원가입 액티비티로 넘어감
                @Override
                public void onClick(View v) {
                    Intent signupintent = new Intent(LoginActivity.this, SignupActivity.class);
                    LoginActivity.this.startActivity(signupintent);
                }
            });

            //로그인버튼 리스너 이벤트
            btn_login.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //로그인 버튼을 누르면 userID에는 idtext에 입력한 값이 저장
                    //userPassword에는 pwtext에 입력한 값이 저장
                    //즉 스트링으로 선언한 변수마다 연결된 레이아웃의 입력값을 받아서 대입
                    userID = w_id.getText().toString();
                    userPassword = w_pw.getText().toString();
                    sendRequest();
                }


            }));
        }


    }

    public void sendRequest() {

        // 로그인 정보가 db에있는 지 확인하기 위한 volley통신
        // Response가 적용안되는데 아마 볼리통신을 위한 라이브러리 없어서 에러인듯 혹은 아래에 LoginRequest가 아직 완성 안되서 그럴수도
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //volley통신을 통해서 서버에 저장된 php파일로 db에 저장된 id와 pw값을 json형태로 받아옴
                    //response값이 정상적으로 오는지 확인
                    //이 부분은 php파일도 확인해야 정확히 기억날듯
                    Log.d("login response", response);
                    //왜 두개지? 로그로 나중에 확인 기억이 안난다....
                    //일단 jsonResponse에는 php에서 response라는 배열에 userID랑success 담아옴
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject idtest = new JSONObject(response);
                    Log.d("idtest", idtest.getString("userID"));
                    // sucees에 successㄷ마겨서오면 자동로그인하도록
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        //성공적으로 아이디와 비밀번호를 받아오면 sharedpreference에 key:autologin value: true 저장
                        //이후에 자동로그인 가능하도록
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

                        //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("autologin", "true");
                        //꼭 commit()을 해줘야 값이 저장됩니다
                        autoLogin.commit();


                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인 성공")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        //session처리가 아니므로 다음 액티비티에서 id값이 필요해서 로그인한 id를 shared에 저장해둠
                        //필요한곳에서 key값 id로 불러서 사용하면됨
                        SharedPreferences idspf = getSharedPreferences("id", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor saveid = idspf.edit();
                        saveid.putString("id", userID);
                        saveid.commit();

                        //main액티비티로 넘어가는거
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인 실패")
                                .setNegativeButton("다시 시도", null)
                                .create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //볼리 통신하는 곳
        // 이거부터 실행되고 위의 Respon.Listener 실행되는거임
        //LoginRequest 클래스에서 서버의 php파일로 데이터 보내고 원하는 데이터 받아옴
        //php파일에서 select문 결과가 있으면 succes변수가 true 없으면 false반환되도록되있음
        LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
        Log.d("login response", "ddd");
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
