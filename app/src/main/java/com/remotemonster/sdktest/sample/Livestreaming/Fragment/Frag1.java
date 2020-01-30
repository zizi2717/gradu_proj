//package com.remotemonster.sdktest.sample.Livestreaming.Fragment;
//
//
//import android.app.Fragment;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import com.remon.sdktest.R;
//import com.remon.sdktest.databinding.ActivityListBinding;
//import com.remon.sdktest.databinding.FragmentListBinding;
//import com.remotemonster.sdk.RemonCall;
//import com.remotemonster.sdk.RemonCast;
//import com.remotemonster.sdktest.sample.Livestreaming.Livestream.RemonApplication;
//
//
///**
// * Created by samsung on 2019-04-16.
// */
//
//public class  Frag1 extends Fragment /*implements View.OnClickListener*/ {
//    private RoomAdapter mAdapter;
//    private RemonCall remonCall;
//    private RemonCast remonCast;
//    private int remonType = 0;
//    private RemonApplication remonApplication;
//    private FragmentListBinding mBinding;
//    public Frag1() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_frag1, container, false);
//        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
//
//
//        return mBinding.getRoot();
//    }
//
//}