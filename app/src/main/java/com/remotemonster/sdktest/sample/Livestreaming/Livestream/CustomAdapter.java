package com.remotemonster.sdktest.sample.Livestreaming.Livestream;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.remon.sdktest.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends BaseAdapter {
    public class ListContents{

        String id;
        String myid;
        String yourid;


        String msg;

        int type;

        ListContents(String _msg)
        {
            //??????
            this.msg = _msg;
            //this.id = _id;

        }
    }


    private ArrayList<ListContents> m_List;

    public CustomAdapter() {
        m_List = new ArrayList<ListContents>();
    }


    public void add(String _msg) {

        m_List.add(new ListContents(_msg));
    }


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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //?????
        final int pos = position;
        final Context context = parent.getContext();

        //넘어온 메시지에서 아이디 떼오는거
        //스플릿으로 해야겠네

        int ididx = m_List.get(position).msg.indexOf("//");
        String idcheck = m_List.get(position).msg.substring(0,ididx);
        Log.d("아이디확인",idcheck);
        Log.d("아이디확인",m_List.get(position).msg);

        int msgidx = m_List.get(position).msg.indexOf("//");
        String msgcheck = m_List.get(position).msg.substring(msgidx);
        String remsg = m_List.get(position).msg;


        TextView myidtext = null;
        TextView youridtext = null;

        TextView text = null;

        CustomHolder holder  = null;
        LinearLayout layout  = null;

        View viewRight = null;
        View viewLeft = null;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("a K:mm");
        String nowDate = sdf.format(date);



        if ( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_chatitem, parent, false);


            layout    = (LinearLayout) convertView.findViewById(R.id.layout);

            youridtext = (TextView)convertView.findViewById(R.id.yourid);
            text    = (TextView) convertView.findViewById(R.id.text);
            viewLeft    = (View) convertView.findViewById(R.id.imageViewleft);



            holder = new CustomHolder();
            //holder.m_myidText = myidtext;
            holder.m_youridText = youridtext;
            holder.m_TextView   = text;
            holder.layout = layout;
            //holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        else {
            holder  = (CustomHolder) convertView.getTag();
         //   myidtext = holder.m_myidText;
            youridtext = holder.m_youridText;
            text    = holder.m_TextView;
            layout  = holder.layout;
        //    viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }
//        Log.d("id??",m_List.get(position).myid);
        Log.d("msg??",m_List.get(position).msg);

        youridtext.setText(idcheck);
    //    myidtext.setText(m_List.get(position).id);
        text.setText(msgcheck);
     //   text.setText(remsg);
       // text.setText(m_List.get(position).msg);


         //   youridtext.setText(nowDate);
         //   text.setBackgroundResource(R.drawable.outbox2);
            // text.setBackgroundResource(R.drawable.inbox2);
        //    myidtext.setVisibility(View.GONE);
        //    youridtext.setVisibility(View.VISIBLE);
       //     layout.setGravity(Gravity.RIGHT);
           // layout.setGravity(Gravity.LEFT);
        //    viewRight.setVisibility(View.GONE);
        //    viewLeft.setVisibility(View.GONE);

//        else{
//            myidtext.setText(nowDate);
//          //  text.setBackgroundResource(R.drawable.inbox2);
//         //   text.setBackgroundResource(R.drawable.outbox2);
//            layout.setGravity(Gravity.LEFT);
//          //  layout.setGravity(Gravity.RIGHT);
//            myidtext.setVisibility(View.VISIBLE);
//            youridtext.setVisibility(View.VISIBLE);
//            viewRight.setVisibility(View.GONE);
//            viewLeft.setVisibility(View.GONE);
//        }

        return convertView;
    }

    private class CustomHolder {
        TextView m_myidText;
        TextView m_youridText;
        TextView m_TextView;
        LinearLayout layout;
        View viewRight;
        View viewLeft;
    }
}
