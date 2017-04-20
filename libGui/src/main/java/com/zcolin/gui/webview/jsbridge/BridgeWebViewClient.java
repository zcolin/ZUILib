/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/
package com.zcolin.gui.webview.jsbridge;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 和网页js通讯的webViewClient
 */
public class BridgeWebViewClient extends WebViewClient {
    private boolean isSupportJsBridge;

    /**
     * 支持JsBridge
     */
    public void setSupportJsBridge() {
        isSupportJsBridge = true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (isSupportJsBridge) {
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (view instanceof BridgeWebView) {
                BridgeWebView webView = (BridgeWebView) view;
                if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
                    webView.handlerReturnData(url);
                    return true;
                } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
                    webView.flushMessageQueue();
                    return true;
                } else if (url.startsWith(BridgeUtil.IOS_SCHEME)){
                    return true;
                }
            }
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return shouldOverrideUrlLoading(view, request.getUrl().toString());
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (isSupportJsBridge) {
            BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);

            if (view instanceof BridgeWebView) {
                BridgeWebView webView = (BridgeWebView) view;
                if (webView.getStartupMessage() != null) {
                    for (Message m : webView.getStartupMessage()) {
                        webView.dispatchMessage(m);
                    }
                    webView.setStartupMessage(null);
                }
            }
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}