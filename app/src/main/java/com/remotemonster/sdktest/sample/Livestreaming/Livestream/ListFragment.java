package com.remotemonster.sdktest.sample.Livestreaming.Livestream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.remon.sdktest.R;
import com.remon.sdktest.databinding.ActivityListBinding;
import com.remon.sdktest.databinding.FragmentListBinding;
import com.remon.sdktest.databinding.ListItemChannelBinding;
import com.remotemonster.sdk.RemonCall;
import com.remotemonster.sdk.RemonCast;
import com.remotemonster.sdk.data.Room;

import java.util.ArrayList;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * Created by lucas on 2018. 5. 16..
 */

public class ListFragment  extends Fragment {
    private RoomAdapter mAdapter;
    private RemonCall remonCall;
    private RemonCast remonCast;
    private int remonType = 1;
    private RemonApplication remonApplication;
    String id;
    private FragmentListBinding mBinding;

    public ListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);


        remonApplication = (RemonApplication) getActivity().getApplicationContext();

        final SharedPreferences idsdf = getActivity().getSharedPreferences("id", Activity.MODE_PRIVATE);

        id =  idsdf.getString("id","");

        Intent intent = getActivity().getIntent();
       // remonType = intent.getIntExtra("remonType", 0);

        mAdapter = new RoomAdapter();
        mRoomList = new ArrayList<>();
        mBinding.lvChannel.setAdapter(mAdapter);

        mBinding.btnCreate.setOnClickListener(v -> {
            if (remonType == 1) {
                remonCast.close();
                Intent intentCreate = new Intent(getActivity(), CastActivity.class);
                intentCreate.putExtra("isCreate", true);
                startActivity(intentCreate);
            }
        });


        return mBinding.getRoot();
    }



    private void getChannelList() {

        Log.d("getChannel","check");
        mRoomList.clear();
        if (remonType == 0 || remonType == 3) {
            /* type = 0 통신일 경우 */
            Log.d("Remoncall","check");
            remonCall = RemonCall.builder()
                    .context(getActivity())
                    //.serviceId(remonApplication.getConfig().getServiceId())
                    .serviceId(remonApplication.getConfig().getServiceId())
                    .key(remonApplication.getConfig().getKey())
                    .restUrl(remonApplication.getConfig().restHost)
                    .wssUrl(remonApplication.getConfig().socketUrl)
                    .build();
            remonCall.onInit(() -> remonCall.fetchCalls());
            remonCall.onFetch(rooms -> {
                mRoomList.clear();
                for (Room room : rooms) {
                    mRoomList.add(room);
                }
                getActivity().runOnUiThread(() -> mAdapter.notifyDataSetChanged());
            });
        } else {
            Log.d("Remoncast","check");
            /* type = 1 방송일 경우 */
            remonCast = RemonCast.builder()
                    .context(getActivity())
                    .serviceId(remonApplication.getConfig().getServiceId())
                    .key(remonApplication.getConfig().getKey())
                    .restUrl(remonApplication.getConfig().restHost)
                    .wssUrl(remonApplication.getConfig().socketUrl)
                     .build();
            remonCast.onInit(() -> remonCast.fetchCasts());
            remonCast.onFetch(rooms -> {
                mRoomList.clear();
                for (Room room : rooms) {
                    mRoomList.add(room);
                }
                getActivity().runOnUiThread(() -> mAdapter.notifyDataSetChanged());
            });
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setServiceId) {
            ServiceIdDialog serviceIdDialog = new ServiceIdDialog(getActivity());
            serviceIdDialog.show();
            return true;
        }
        if (id == R.id.itemRefresh) {
            getChannelList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Room> mRoomList;

    private class RoomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mRoomList.size();
        }

        @Override
        public Object getItem(int position) {
            return mRoomList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemChannelBinding binding;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item_channel, null);
                binding = DataBindingUtil.bind(convertView);
                convertView.setTag(binding);
            } else {
                binding = (ListItemChannelBinding) convertView.getTag();
            }

            //방제목이랑 아이디로 변경하기
            Log.d("roominfotest",mRoomList.get(position).getName());
            Log.d("test",mRoomList.get(position).getServiceId());
            Log.d("test",mRoomList.get(position).getId());
            Log.d("test",mRoomList.get(position).getStatus());

            binding.tvRoomInfo.setText(mRoomList.get(position).getName());
            binding.tvStatus.setText(id);
            binding.tvRoomInfo.setOnClickListener(v -> {
                if (remonType == 1) {
                    remonCast.close();
                    Intent intent = new Intent(getActivity(), CastActivity2.class);
                    intent.putExtra("isCreate", false);
                    intent.putExtra("chid", mRoomList.get(position).getId());
                    startActivity(intent);
                }
            });
            binding.imvSetConfig.setOnClickListener(v -> {
                if (!mRoomList.get(position).getStatus().equals("COMPLETE")) {
                   if (remonType == 1) {
                        remonCast.close();
                        Intent intent = new Intent(getActivity(), CastActivity2.class);
                        intent.putExtra("isCreate", false);
                        intent.putExtra("setConfig", true);
                        intent.putExtra("chid", mRoomList.get(position).getId());
                        startActivity(intent);
                    }
                }
            });
            return binding.getRoot();
        }

    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

    @Override
    public void onResume() {
        super.onResume();
        getChannelList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

