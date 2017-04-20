/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/
package com.zcolin.gui.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zcolin.gui.webview.jsbridge.BridgeWebViewClient;


/**
 * WebViewClient主要帮助WebView处理各种通知、请求事件的.
 */
class ZWebViewClientWrapper extends BridgeWebViewClient {
    private WebViewClient webViewClient;
    private ProgressBar   proBar;

    ZWebViewClientWrapper(WebViewClient webViewClient) {
        this.webViewClient = webViewClient;
    }

    public WebViewClient getWebViewClient() {
        return webViewClient;
    }

    public ZWebViewClientWrapper setWebViewClient(WebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        return this;
    }

    public ZWebViewClientWrapper setProgressBar(ProgressBar bar) {
        this.proBar = bar;
        return this;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (super.shouldOverrideUrlLoading(view, url)) {
            return true;
        } else {
            return webViewClient.shouldOverrideUrlLoading(view, url);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (super.shouldOverrideUrlLoading(view, request)) {
            return true;
        } else {
            return webViewClient.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (proBar != null) {
            proBar.setVisibility(View.VISIBLE);
        }
        webViewClient.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (proBar != null) {
            proBar.setVisibility(View.GONE);
        }
        webViewClient.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        webViewClient.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        webViewClient.onLoadResource(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPageCommitVisible(WebView view, String url) {
        webViewClient.onPageCommitVisible(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return webViewClient.shouldInterceptRequest(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return webViewClient.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        webViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        webViewClient.onReceivedError(view, request, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        webViewClient.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        webViewClient.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        webViewClient.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        webViewClient.onReceivedSslError(view, handler, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        webViewClient.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        webViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return webViewClient.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        webViewClient.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        webViewClient.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        webViewClient.onReceivedLoginRequest(view, realm, account, args);
    }
}