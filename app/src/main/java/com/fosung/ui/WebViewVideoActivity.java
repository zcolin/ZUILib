/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-12-26 上午11:33
 * ********************************************************
 */
package com.fosung.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.fosung.gui.webview.ZWebView;

/**
 * 带JsBridge的webview的Demo
 */
public class WebViewVideoActivity extends FragmentActivity {
    private ZWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_video);

        webView = (ZWebView) findViewById(R.id.webView);
        webView.setSupportVideoFullScreen(this).setSupportProgressBar();
        
        webView.loadUrl("http://dt.85ido.com:8080/r/cms/qilu/qilu/jty/gbxxpt/video-new.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.hideCustomView()) {
                return true;
            } else if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
