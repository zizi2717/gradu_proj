package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.remon.sdktest.R;
import com.squareup.picasso.Picasso;

/**
 * Created by samsung on 2018-06-21.
 */

public class ImageClick extends Activity {



    ViewFlipper flipper;


    String image1,image2,image3,image4,image5;

    //자동 Flipping 선택 ToggleButton 참조변수

    ToggleButton toggle_Flipping;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clickimage);



        //ViewFlipper 객체 참조

        flipper= (ViewFlipper)findViewById(R.id.flipper);

        Intent imgintent = getIntent();

        image1 = imgintent.getStringExtra("image1");
        image2 = imgintent.getStringExtra("image2");
        image3 = imgintent.getStringExtra("image3");
        image4 = imgintent.getStringExtra("image4");
        image5 = imgintent.getStringExtra("image5");





        //총 10개의 이미지 중 3개만 XML에서 ImageView로 만들었었음.

        //소스코드에서 ViewFipper에게 나머지 7개의 이미지를 추가하기위해

        //ImageView 7개를 만들어서 ViewFlipper에게 추가함

        //layout_width와 layout_height에 대한 특별한 지정이 없다면

        //기본적으로 'match_parent'로 설정됨.


        ImageView img1= (ImageView)findViewById(R.id.img01);
        ImageView img2= new ImageView(this);
        ImageView img3= new ImageView(this);
        ImageView img4= new ImageView(this);
        ImageView img5= new ImageView(this);

        if(image1.contains(".jpg")) {
            Picasso.with(ImageClick.this)
                    .load("http://cho1719.vps.phps.kr/uploads/"+image1)
                    .into(img1);
           // flipper.addView(img1);
        }
        else{
            img1.setVisibility(View.GONE);
        }
        if(image2.contains(".jpg")) {
            Picasso.with(ImageClick.this)
                    .load("http://cho1719.vps.phps.kr/uploads/"+image2)
                    .into(img2);
            flipper.addView(img2);
        }
        else{
            img2.setVisibility(View.GONE);
        }
        if(image3.contains(".jpg")) {
            Picasso.with(ImageClick.this)
                    .load("http://cho1719.vps.phps.kr/uploads/"+image3)
                    .into(img3);
            flipper.addView(img3);
        }
        else{
            img3.setVisibility(View.GONE);
        }
        if(image4.contains(".jpg")) {
            Picasso.with(ImageClick.this)
                    .load("http://cho1719.vps.phps.kr/uploads/"+image4)
                    .into(img4);
            flipper.addView(img4);
        }
        else{
            img4.setVisibility(View.GONE);
        }
        if(image5.contains(".jpg")) {
            Picasso.with(ImageClick.this)
                    .load("http://cho1719.vps.phps.kr/uploads/"+image5)
                    .into(img5);
            flipper.addView(img5);
        }
        else{
            img5.setVisibility(View.GONE);
        }








        //ViewFlipper가 View를 교체할 때 애니메이션이 적용되도록 설정

        //애니메이션은 안드로이드 시스템이 보유하고 있는  animation 리소스 파일 사용.

        //ViewFlipper의 View가 교체될 때 새로 보여지는 View의 등장 애니메이션

        //AnimationUtils 클래스 : 트윈(Tween) Animation 리소스 파일을 Animation 객체로 만들어 주는 클래스

        //AnimationUtils.loadAnimaion() - 트윈(Tween) Animation 리소스 파일을 Animation 객체로 만들어 주는 메소드

        //첫번째 파라미터 : Context

        //두번재 파라미터 : 트윈(Tween) Animation 리소스 파일(여기서는 안드로이드 시스템의 리소스 파일을 사용

        //                    (왼쪽에서 슬라이딩되며 등장)

        Animation showIn= AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        //ViewFlipper에게 등장 애니메이션 적용

        flipper.setInAnimation(showIn);



        //ViewFlipper의 View가 교체될 때 퇴장하는 View의 애니메이션

        //오른쪽으로 슬라이딩 되면 퇴장하는 애니메이션 리소스 파일 적용.

        //위와 다른 방법으로 애니메이션을 적용해봅니다.

        //첫번째 파라미터 : Context

        //두번재 파라미터 : 트윈(Tween) Animation 리소스 파일(오른쪽으로 슬라이딩되며 퇴장)

        flipper.setOutAnimation(this, android.R.anim.slide_out_right);



        //자동 Flipping 선택 ToggleButton에 따른 작업

        toggle_Flipping= (ToggleButton)findViewById(R.id.toggle_auto);



        //ToggleButton에 Toggle상태 변경 리스너 세팅(OnCheckedChangedListener)

        toggle_Flipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            //ToggleButton의 선택 상태가 변경되면 자동으로 호출되는 메소드

            //첫번재 파라미터 : 선택의 변화가 생긴 CompoundButton(여기서는 toggle_Flipping)

            //두번째 파라미터 : Compoundbutton(toggle_Flipping)의 ON(true),OFF(false) 상태

            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub

                if(isChecked){//On 으로 바뀌었으므로 ..자동 Flipping 시작..



                    //1초의 간격으로 ViewFlipper의 View 자동 교체

                    flipper.setFlipInterval(1000);//플리핑 간격(1000ms)

                    flipper.startFlipping();//자동 Flipping 시작



                }else{//OFF로 변경되었으므로  Flipping 정지



                    flipper.stopFlipping();;//Flipping 정지



                }

            }

        });



    }



    //onClick속성이 지정된 View가 클릭되었을 때 자동으로 호출되는 메소드.

    public void mOnClick(View v){



        switch( v.getId() ){

            case R.id.btn_previous:

                flipper.showPrevious();//이전 View로 교체

                break;

            case R.id.btn_next:

                flipper.showNext();//다음 View로 교체

                break;

        }

    }

}