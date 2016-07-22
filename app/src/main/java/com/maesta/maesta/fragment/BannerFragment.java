package com.maesta.maesta.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.maesta.maesta.R;
import com.maesta.maesta.vo.Banner;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class BannerFragment extends Fragment {

        private static final String ID = "ID";
        private static final String URL = "URL";
        private Banner banner;
        private OnFragmentInteractionListener mListener;

        public BannerFragment() {
        }
        public static BannerFragment newInstance(Banner banner) {
            BannerFragment fragment = new BannerFragment();
            Bundle args = new Bundle();
            args.putInt(ID,banner.id);
            args.putString(URL,banner.url);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                banner = new Banner();
                banner.id = getArguments().getInt(ID);
                banner.url = getArguments().getString(URL);
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view   =   inflater.inflate(R.layout.layout_banner_fragment, container, false);
            Glide.with(getActivity()).load(banner.url).asBitmap()
                    .placeholder(R.drawable.banner_1).centerCrop().into((ImageView) view.findViewById(R.id.img_banner));

            return view;
        }


        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            /*if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }*/
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        public interface OnFragmentInteractionListener {
            void onFragmentInteraction(String command);
        }
    }