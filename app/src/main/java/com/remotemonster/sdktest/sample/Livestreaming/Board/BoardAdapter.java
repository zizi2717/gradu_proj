package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.remon.sdktest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import static android.provider.Settings.System.AIRPLANE_MODE_ON;


public class BoardAdapter extends BaseAdapter {
    public class ListContents {

        ArrayList<HashMap<String, String>> inputValue;

        //메세지 선언인가봄
        //  String roomname;
        //  String id;
        // 내껀지 다른사람껀지 날짜인지 타입 1;내꺼 2: 다른사람 3: 날짜
        int type;

        ListContents(ArrayList<HashMap<String, String>> _inputValue) {
            //생성자
            this.inputValue = _inputValue;
        }
    }

    //아이템 담을 리스트
    private ArrayList<ListContents> m_List;

    public BoardAdapter() {
        m_List = new ArrayList<ListContents>();
    }

    // 외부에서 아이템 추가 요청 시 사용
    // 즉 채팅 추가될때 사용되는 부분
    public void add(ArrayList<HashMap<String, String>> _inputValue) {

        m_List.add(new ListContents(_inputValue));
    }

    //이건 필요없겠다.
    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 보여지는 부분 처리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //위치값
        final int pos = position;
        final Context context = parent.getContext();

        isAirplaneModeOn(context);

        //글씨 써지는 부분
        TextView idview = null;
        TextView titleview = null;
        TextView contentview = null;
        TextView timeview = null;
        TextView numview = null;
        ImageView profileview = null;

        //고정시키는거?
        CustomHolder holder = null;
       RelativeLayout layout = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if (convertView == null) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_recycler_view, parent, false);

            // 뷰 새로 생길때
            layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            idview = (TextView) convertView.findViewById(R.id.name);
            titleview = (TextView) convertView.findViewById(R.id.title);
            contentview = (TextView) convertView.findViewById(R.id.content);
            timeview = (TextView) convertView.findViewById(R.id.date);
            numview = (TextView) convertView.findViewById(R.id.num);
            profileview = (ImageView)convertView.findViewById(R.id.image);


            // 홀더 생성 및 Tag로 등록
            // holder. 이것들은 홀더 메소드에서 선언된것들
            holder = new CustomHolder();
            holder.m_idview = idview;
            holder.m_titleview = titleview;
            holder.m_contentview = contentview;
            holder.m_timeview = timeview;
            holder.m_numview = numview;
            holder.m_profileview = profileview;
            holder.layout = layout;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
            idview = holder.m_idview;
            titleview = holder.m_titleview;
            contentview = holder.m_contentview;
            timeview = holder.m_timeview;
            numview = holder.m_numview;
            profileview = holder.m_profileview;
            layout  = holder.layout;

        }

  //      for (int i = 0; i < m_List.size(); i++) {
            idview.setText("글쓴이:" + m_List.get(position).inputValue.get(position).get("id"));
            titleview.setText(m_List.get(position).inputValue.get(position).get("title"));
            contentview.setText(m_List.get(position).inputValue.get(position).get("content"));
            timeview.setText(m_List.get(position).inputValue.get(position).get("nowdate"));
            numview.setText(m_List.get(position).inputValue.get(position).get("num"));

            String imgname = m_List.get(position).inputValue.get(position).get("proimg");
              Log.d("프로필아이디",idview.getText().toString());
             Log.d("프로필이미지",imgname);

             profileview.setImageResource(R.drawable.kakao);

    //    Glide.with(context).load("http://cho1719.vps.phps.kr/uploads/"+imgname).apply(new RequestOptions().fitCenter()).into(profileview);
//            Picasso.with(context)
//                    .load("http://cho1719.vps.phps.kr/uploads/"+imgname)
//                    .into(profileview);

           // Log.d("프로필이미지",m_List.get(position).inputValue.get(position).get("proimg"));
   //     }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numintent = new Intent(context, Board.class);
                String itemnum = m_List.get(pos).inputValue.get(pos).get("num");
                numintent.putExtra("itemnum", itemnum);
                context.startActivity(numintent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(context); // 다이얼로그 사용
                dlg.setTitle("방 나가기")
                        .setMessage("나가시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               m_List.remove(pos);
                                String rmnum = m_List.get(pos).inputValue.get(pos).get("num");
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                };
                                DeleteBoardRequest deleteBoardRequest = new DeleteBoardRequest(rmnum, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(context);
                                queue.add(deleteBoardRequest);
                            }
                        }).show();
                return true;
            }
        });
        return convertView;
    }


    private class CustomHolder {

        TextView m_idview;
        TextView m_titleview;
        TextView m_contentview;
        TextView m_timeview;
        TextView m_numview;
        ImageView m_profileview;
        RelativeLayout layout;

    }

    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
    }
}
