package com.remotemonster.sdktest.sample.Livestreaming.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.remon.sdktest.R;


/**
 * Created by samsung on 2019-04-16.
 */

public class Frag2 extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, container, false);

        return view;
    }
}