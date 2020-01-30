package com.remotemonster.sdktest.sample.Livestreaming.Livestream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.remon.sdktest.R;
import com.remon.sdktest.databinding.ActivityRcvCastBinding;
import com.remotemonster.sdk.Config;
import com.remotemonster.sdk.RemonCast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class CastActivity2 extends AppCompatActivity {
    private RemonApplication remonApplication;
    private String connectChId;
    private RemonCast remonCast = null;
    private RemonCast castViewer = null;
    private boolean isCastView = false;
    private boolean isSpeakerOn = true;
    private boolean isRemoteFullScreen = false;
    private ActivityRcvCastBinding mBinding;

    Handler handler;
    String data,myid,rcvdata;
    SocketChannel socketChannel;
    private static final String HOST = "115.71.237.207";
    private static final int PORT = 5001;
    String msg;

    ListView m_ListView;
    CustomAdapter m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rcv_cast);

        remonApplication = (RemonApplication) getApplicationContext();

        //id값 쉐어드로 받아오기
        SharedPreferences idsdf = getSharedPreferences("id", Activity.MODE_PRIVATE);
        myid = idsdf.getString("id", ""); //id넘어오도록

        isRemoteFullScreen = false;
       // mBinding.perFrameLocal.setPosition(0, 50, 50, 50);
      //  mBinding.perFrameRemote.setPosition(50, 50, 50, 50);


//채팅
        handler = new Handler();


        // 커스텀 어댑터 생성
        m_Adapter = new CustomAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.listView1);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isCreate", false)) {
            /* config set 후 방송생성*/
            ConfigDialog configDialog = new ConfigDialog(CastActivity2.this, true, chid -> {
                remonCast = new RemonCast();

                Config config;
                config = remonApplication.getConfig();
             //   config.setLocalView(mBinding.surfRendererLocal);
                config.setActivity(CastActivity2.this);
                //setCallback(false, remonCast);

                connectChId = chid;
                remonCast.create(connectChId, config);
            });
            configDialog.show();
        } else {
            /* List에 있던 방송 시청 */
            connectChId = intent.getStringExtra("chid");


            if (intent.getBooleanExtra("setConfig", false)) {
                /* Config 수정 후 시청 */
                ConfigDialog configDialog = new ConfigDialog(CastActivity2.this, false, chid -> {
                    castViewer = new RemonCast();
                    Config config;
                    config = remonApplication.getConfig();
                    config.setRemoteView(mBinding.surfRendererRemote);
                    config.setActivity(CastActivity2.this);
                    //setCallback(true, castViewer);

                    castViewer.join(connectChId, config);
                });
                configDialog.show();
            } else {
                /* 즉시 시청 */
                castViewer = RemonCast.builder()
                        .context(CastActivity2.this)
                        .remoteView(mBinding.surfRendererRemote)
                        .serviceId(remonApplication.getConfig().getServiceId())
                        .key(remonApplication.getConfig().getKey())
                        .build();
                //setCallback(true, castViewer);

                castViewer.join(connectChId);
            }
        }


//        mBinding.btnRemonCastClose.setOnClickListener(v -> {
//          //  addLog("Start close");
//            if (remonCast != null) {
//                remonCast.close();
//            }
//            if (castViewer != null) {
//                castViewer.close();
//            }
//        });


//        mBinding.btnStatReport.setOnClickListener(view -> {
//            if (isCastView) {
//                castViewer.enableStatView(true, mBinding.rlRemoteView);
//            } else {
//                remonCast.enableStatView(true, mBinding.rlRemoteView);
//            }
//        });

//        mBinding.btnViewCast.setOnClickListener(v -> {
//            if (!isCastView) {
//                /* 자신이 시청중이 아니라면 방송시청*/
//                castViewer = RemonCast.builder()
//                        .context(CastActivity.this)
//                        .remoteView(mBinding.surfRendererRemote)
//                        .serviceId(remonApplication.getConfig().getServiceId())
//                        .key(remonApplication.getConfig().getKey())
//                        .build();
//                setCallback(true, castViewer);
//
//                castViewer.join(remonCast.getId());
//            }
//        });


//        mBinding.btnSpeakerOnOff.setOnClickListener(v -> {
//            if (castViewer != null) {
//                isSpeakerOn = isSpeakerOn ? (isSpeakerOn = false) : (isSpeakerOn = true);
//                castViewer.setSpeakerphoneOn(isSpeakerOn);
//            }
//        });


        // 영상크기 변환시 사용
        mBinding.imvRemoteScreenChange.setOnClickListener(view -> runOnUiThread(() -> {
            if (isRemoteFullScreen) {
                isRemoteFullScreen = false;
                mBinding.perFrameRemote.setPosition(50, 50, 50, 50);
                mBinding.perFrameRemote.requestLayout();
            } else {
                isRemoteFullScreen = true;
                mBinding.perFrameRemote.setPosition(0, 0, 100, 100);
                mBinding.perFrameRemote.requestLayout();
            }
        }));


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(true);
                    socketChannel.connect(new InetSocketAddress(HOST, PORT));
                } catch (Exception ioe) {
                    Log.d("asd", ioe.getMessage() + "a");
                    ioe.printStackTrace();

                }
                checkUpdate.start();
            }
        }).start();

        //이 부분이 텍스트 입력해서 클릭하는곳
        mBinding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   // final String return_msg = mBinding.sendMsgEditText.getText().toString();
                    String msg1 = mBinding.sendMsgEditText.getText().toString();
                    String msg2 =  myid+"//"+msg1;
                    final String return_msg = msg2;
                    refresh(return_msg);
                    if (!TextUtils.isEmpty(return_msg)) {
                        new CastActivity2.SendmsgTask().execute(return_msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void setCallback(boolean isCastView, RemonCast remonCast) {
//        remonCast.onInit(() -> addLog("onInit"));
//        remonCast.onCreate((String id) -> addLog("onCreate : " + id));
//        remonCast.onComplete(() -> {
//            addLog("onComplete");
//            this.isCastView = isCastView;
//        });
//        remonCast.onJoin(() -> {
//            addLog("onJoin");
//            runOnUiThread(() -> mBinding.surfRendererRemote.setScalingType(
//                    RendererCommon.ScalingType.SCALE_ASPECT_FILL));
//        });
//        remonCast.onClose(() -> finish());
//        remonCast.onError(e -> addLog("error code : " + e.getRemonCode().toString()));
//        remonCast.onStat(report -> {
//            if (isCastView) {
//                addLog("Receive report - fps : " + report.getRemoteFrameRate());
//            } else {
//                addLog("Receive report - fps : " + report.getLocalFrameRate());
//            }
//        });
//    }


    private String mPriorLog = "";
//    private void addLog(String log) {
//        mPriorLog = mPriorLog + log + "\n";
//        runOnUiThread(() -> {
//            mBinding.tvLog.setText(mPriorLog);
//            mBinding.scvLog.scrollTo(0, mBinding.scvLog.getBottom());
//        });
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onDestroy() {
        if (remonCast != null) {
            remonCast.close();
        }
        if (castViewer != null) {
            castViewer.close();
        }
        super.onDestroy();
        try {
            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //메세지 전송하는 백그라운드  테스크
    private class SendmsgTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                socketChannel
                        .socket()
                        .getOutputStream()
                        .write(strings[0].getBytes("EUC-KR")); // 서버로
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBinding.sendMsgEditText.setText("");
                }
            });
        }
    }

    void receive() {
        while (true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                //서버가 비정상적으로 종료했을 경우 IOException 발생
                int readByteCount = socketChannel.read(byteBuffer); //데이터받기
                Log.d("readByteCount", readByteCount + "");
                //서버가 정상적으로 Socket의 close()를 호출했을 경우
                if (readByteCount == -1) {
                    throw new IOException();
                }

                //data가 받는 메세지인듯
                //보낸메세지 id+msg로 수정했다면 split같은걸로 분리해야함
                byteBuffer.flip(); // 문자열로 변환
                Charset charset = Charset.forName("EUC-KR");
                rcvdata = charset.decode(byteBuffer).toString();
                //191025수정 메시지만 정상적으로 나타난다면 id담을 변수 만들고 해당 변수를 어뎁터에 추가

                if(rcvdata.contains("[SERVER]")){
                    rcvdata = "]//새로운사용자 입장";
                }

                String[] rcvmsg = rcvdata.split("]");
                data = rcvmsg[1];
                Log.d("받은메세지", "msg :" + data);
                handler.post(showUpdate);
            } catch (IOException e) {
                Log.d("getMsg", e.getMessage() + "");
                try {
                    socketChannel.close();
                    break;
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    private Thread checkUpdate = new Thread() {

        public void run() {
            try {
                String line;
                receive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable showUpdate = new Runnable() {

        public void run() {
            String receive = "Coming word : " + data;
            Log.d("받은메시지",receive);
            refresh(data);
        }

    };

    private void refresh (String inputValue) {
        m_Adapter.add(inputValue) ;
        m_Adapter.notifyDataSetChanged();
    }
}

