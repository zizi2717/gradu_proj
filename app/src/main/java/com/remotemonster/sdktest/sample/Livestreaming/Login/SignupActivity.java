package com.remotemonster.sdktest.sample.Livestreaming.Login;

import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.remon.sdktest.R;

import org.json.JSONObject;

/**
 * Created by samsung on 2019-03-29.
 */

public class SignupActivity extends AppCompatActivity {

    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText w_id = (EditText)findViewById(R.id.w_id);
        final EditText w_pw = (EditText) findViewById(R.id.w_pw1);
        final EditText pw2text = (EditText) findViewById(R.id.w_pw2);
        final EditText nametext = (EditText) findViewById(R.id.nickname);
        final EditText studenttext = (EditText)findViewById(R.id.studentnum) ;

        Button signupbtn = (Button) findViewById(R.id.signupbtn);
        final Button idcheckbtn = (Button) findViewById(R.id.idcheckbtn);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        idcheckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = w_id.getText().toString();

                if (validate) {
                    return;
                }
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("아이디는 빈 칸 일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }
                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("validate respon",response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("사용 가능한 아이디.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                                w_id.setEnabled(false);
                                validate = true;
                                w_id.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                idcheckbtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("사용 할 수 없는 아이디.")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                //아이디 중복체크용 볼리통신
                IdRequest idRequest = new IdRequest(userID, responseListner);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(idRequest);
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = w_id.getText().toString();
                String userPassword = w_pw.getText().toString();
                String userPassword2 = pw2text.getText().toString();
                String userName = nametext.getText().toString();
                String studentId =  studenttext.getText().toString();
                Log.d("클릭 확인!!", "클릭확인");
                Log.d("ID확인", userID);
                Log.d("Pw확인", userPassword);

                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("중복 체크를 진행해주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                if (userID.equals("") || userPassword.equals("") || userPassword2.equals("") || userName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("빈칸을 모두 입력해주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                if(!userPassword.equals(userPassword2)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("비밀번호를 확인해주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                AlertDialog ad = builder.create();
                                ad.setMessage("회원가입에 성공했습니다.");
                                ad.show();
                                ad.dismiss();
//                                builder.setMessage("회원가입에 성공했습니다.")
//                                        .setPositiveButton("확인", null)
//                                        .create()
//                                        .show();

                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("회원가입에 실패했습니다..")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                //전체 회원가입을 위한 볼리통신
                SignupRequest signupRequest = new SignupRequest(userID, userPassword, userName, studentId, responseListner);
//                SignupRequest signupRequest = new SignupRequest(userID, userPassword, userPassword2, userName, studentId, responseListner);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(signupRequest);
            }

        });


    }


}
