package com.maesta.maesta.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maesta.maesta.R;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class BannerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.layout_banner_fragment, container, false);

        return rootView;
    }
}
