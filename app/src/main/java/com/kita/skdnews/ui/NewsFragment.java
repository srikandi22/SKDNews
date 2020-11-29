package com.kita.skdnews.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.models.Article;

public class NewsFragment extends Fragment {
    private static String TAG = NewsFragment.class.getSimpleName();
    private WebView webView;
    private ActionBar action;

    int pos = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        MainActivity.btnSearch.setVisibility(View.GONE);
        webView = root.findViewById(R.id.webView);

        action = ((AppCompatActivity)getActivity()).getSupportActionBar();

        pos = getArguments().getInt("index",-1);

        Log.e(TAG, String.valueOf(pos));
        String src = null;
        String url = null;
        if (pos > -1) {
            Article article = MainActivity.articles.get(pos);
            src = article.getSource().getName();
            url = article.getUrl();
        }else{
            src = getArguments().getString("src_name",null);
            url = getArguments().getString("url",null);
        }

        if (src != null) {
            action.setTitle(src);
            action.setSubtitle(url);
            initWebView(url);
        }
        return root;
    }

    private void initWebView(String url){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}
