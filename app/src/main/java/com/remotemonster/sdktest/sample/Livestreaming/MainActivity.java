package com.remotemonster.sdktest.sample.Livestreaming;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.remon.sdktest.R;
import com.remotemonster.sdktest.sample.Livestreaming.Fragment.Frag2;
import com.remotemonster.sdktest.sample.Livestreaming.Board.Frag3;
import com.remotemonster.sdktest.sample.Livestreaming.Profile.Frag4;
import com.remotemonster.sdktest.sample.Livestreaming.Livestream.ListFragment;
import com.remotemonster.sdktest.sample.Livestreaming.Profile.Frag4;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt1,bt2,bt3,bt4;
    FragmentManager fm;
    FragmentTransaction tran;
    ListFragment frag1;
    Frag2 frag2;
    Frag3 frag3;
    Frag4 frag4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        frag1 = new ListFragment(); //프래그먼트 객채셍성
        frag2 = new Frag2(); //프래그먼트 객채셍성
        frag3 = new Frag3(); //프래그먼트 객채셍성
        frag4 = new Frag4();
        setFrag(0); //프래그먼트 교체
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt1:
                setFrag(0);
                break;
            case R.id.bt2:
                setFrag(1);
                break;
            case R.id.bt3:
                setFrag(2);
            case R.id.bt4:
                setFrag(3);
                break;
        }
    }
    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.main_frame, frag1);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.main_frame, frag3);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.main_frame, frag3);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 3:
                tran.replace(R.id.main_frame, frag4);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("프로필사진","프로필사진체크");
    }
}