package com.kita.skdnews.ui.about;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kita.skdnews.BuildConfig;
import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;

public class AboutFragment extends Fragment {
    private static String TAG = AboutFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        MainActivity.btnSearch.setVisibility(View.GONE);
        TextView txtOS = root.findViewById(R.id.about_os);
        TextView txtver = root.findViewById(R.id.about_app_ver);


        txtOS.setText("Application Version : " + getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
        txtver.setText("Operating System : " + "Android " + Build.VERSION.RELEASE + ", API " + Build.VERSION.SDK_INT + ", " + Build.MODEL);

        return root;
    }
}