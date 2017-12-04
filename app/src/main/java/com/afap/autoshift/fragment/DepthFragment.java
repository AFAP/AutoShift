package com.afap.autoshift.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afap.autoshift.R;
import com.afap.autoshift.model.PlatformInfo;

public class DepthFragment extends Fragment {
    public final static String KEY_PLATFORM1 = "key_platform1";
    public final static String KEY_PLATFORM2 = "key_platform2";


    private PlatformInfo mPlatform1;
    private PlatformInfo mPlatform2;


    public static DepthFragment newInstance(PlatformInfo platform1, PlatformInfo platform2) {
        DepthFragment fragment = new DepthFragment();
        Bundle b = new Bundle();
        b.putSerializable(KEY_PLATFORM1, platform1);
        b.putSerializable(KEY_PLATFORM2, platform2);

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlatform1 = (PlatformInfo) getArguments().getSerializable(KEY_PLATFORM1);
        mPlatform2 = (PlatformInfo) getArguments().getSerializable(KEY_PLATFORM2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
