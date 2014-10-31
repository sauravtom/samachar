package com.sauravtom.samachar;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebViewFragment extends Fragment {

    public static WebView webView;
    public static WebViewClient webViewClient;
    private ProgressBar spinner;
    private String[] menus;
    private int position;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        position = getArguments().getInt("position");
        String url = getArguments().getString("url");
        menus = getResources().getStringArray(R.array.menus);

        v = inflater.inflate(R.layout.fragment_layout, container, false);

        spinner = (ProgressBar)v.findViewById(R.id.progressbar);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.GONE);

        webViewClient = new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                spinner.setVisibility(View.INVISIBLE);

            }

            //Intercept and change urls to comply with reddit mobile view
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };

        webView = (WebView)v.findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(webViewClient);
        webView.setVerticalScrollBarEnabled(false);
        webView.loadUrl(url);

        return v;
    }




}