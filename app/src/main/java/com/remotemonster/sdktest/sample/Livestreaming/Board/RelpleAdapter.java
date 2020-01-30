package com.remotemonster.sdktest.sample.Livestreaming.Board;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.remon.sdktest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by samsung on 2018-06-18.
 */

public class RelpleAdapter extends BaseAdapter {
    public class ListContents {

        ArrayList<HashMap<String, String>> inputValue;

        int type;

        ListContents(ArrayList<HashMap<String, String>> _inputValue) {
            //생성자
            this.inputValue = _inputValue;
        }
    }

    //아이템 담을 리스트
    private ArrayList<ListContents> m_List;

    public RelpleAdapter() {
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

        //글씨 써지는 부분
        TextView idview = null;
        TextView titleview = null;
        TextView contentview = null;
        TextView timeview = null;
        TextView numview = null;
        ImageView proimgview = null;
        //고정시키는거?
       CustomHolder holder = null;
        RelativeLayout layout = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if (convertView == null) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_repleitem, parent, false);

            // 뷰 새로 생길때
            layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            idview = (TextView) convertView.findViewById(R.id.name);
            contentview = (TextView) convertView.findViewById(R.id.content);
            numview = (TextView) convertView.findViewById(R.id.num);
            timeview = (TextView)convertView.findViewById(R.id.date) ;
            proimgview = (ImageView)convertView.findViewById(R.id.proimg);


            // 홀더 생성 및 Tag로 등록
            // holder. 이것들은 홀더 메소드에서 선언된것들
            holder = new CustomHolder();
            holder.m_idview = idview;
            holder.m_contentview = contentview;
            holder.m_numview = numview;
            holder.m_proimgview = proimgview;
            holder.m_dateview = timeview;
            holder.layout = layout;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
            idview = holder.m_idview;
            contentview = holder.m_contentview;
            numview = holder.m_numview;
            proimgview = holder.m_proimgview;
            timeview = holder.m_dateview;
            layout  = holder.layout;

        }
        for (int i = 0; i < m_List.size(); i++) {

            idview.setText("글쓴이:" + m_List.get(position).inputValue.get(position).get("id"));
            contentview.setText(m_List.get(position).inputValue.get(position).get("content"));
            numview.setText(m_List.get(position).inputValue.get(position).get("num"));
            timeview.setText(m_List.get(position).inputValue.get(position).get("nowdate"));
            Picasso.with(context)
                    .load("http://cho1719.vps.phps.kr/uploads/"+m_List.get(position).inputValue.get(position).get("proimg"))
                    .into(proimgview);

        }

        return convertView;
    }


    private class CustomHolder {

        TextView m_idview;
        TextView m_contentview;
        TextView m_numview;
        ImageView m_proimgview;
        TextView m_dateview;
        RelativeLayout layout;

    }
}
