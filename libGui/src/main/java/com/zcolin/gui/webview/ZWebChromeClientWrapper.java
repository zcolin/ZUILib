/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-12-26 上午11:07
 * ********************************************************
 */
package com.zcolin.gui.webview;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zcolin.gui.ZAlert;
import com.zcolin.gui.ZConfirm;
import com.zcolin.gui.ZDialog;


/**
 * WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比如
 */
class ZWebChromeClientWrapper extends WebChromeClient {

    private WebChromeClient webChromeClient;
    private ProgressBar     proBar;
    private boolean         isSupportH5Location;//是否支持H5定位

    ZWebChromeClientWrapper(WebChromeClient webChromeClient) {
        this.webChromeClient = webChromeClient;
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClient;
    }

    public WebChromeClient setWebChromeClient(WebChromeClient webChromeClient) {
        this.webChromeClient = webChromeClient;
        return this;
    }

    public WebChromeClient setProgressBar(ProgressBar bar) {
        this.proBar = bar;
        return this;
    }

    public WebChromeClient setSupportH5Location() {
        this.isSupportH5Location = true;
        return this;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (!webChromeClient.onJsAlert(view, url, message, result)) {
            ZAlert alert = new ZAlert(view.getContext()).setMessage(message);
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.confirm();
                }
            });
            alert.show();
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        if (!webChromeClient.onJsConfirm(view, url, message, result)) {
            ZConfirm dlg = new ZConfirm(view.getContext());
            dlg.setMessage(message)
               .addSubmitListener(new ZDialog.ZDialogSubmitInterface() {
                   @Override
                   public boolean submit() {
                       result.confirm();
                       return true;
                   }
               })
               .addCancelListener(new ZDialog.ZDialogCancelInterface() {
                   @Override
                   public boolean cancel() {
                       result.cancel();
                       return true;
                   }
               })
               .show();
        }
        return true;
    }

    /**
     * H5定位相关
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (isSupportH5Location) {
            callback.invoke(origin, true, false);
        }
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (proBar != null) {
            proBar.setProgress(newProgress);
        }
        webChromeClient.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        webChromeClient.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        webChromeClient.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        webChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        webChromeClient.onShowCustomView(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        webChromeClient.onShowCustomView(view, requestedOrientation, callback);
    }

    @Override
    public void onHideCustomView() {
        webChromeClient.onHideCustomView();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return webChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view) {
        webChromeClient.onRequestFocus(view);
    }

    @Override
    public void onCloseWindow(WebView window) {
        webChromeClient.onCloseWindow(window);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return webChromeClient.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return webChromeClient.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        webChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        webChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }


    @Override
    public void onGeolocationPermissionsHidePrompt() {
        webChromeClient.onGeolocationPermissionsHidePrompt();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        webChromeClient.onPermissionRequest(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        webChromeClient.onPermissionRequestCanceled(request);
    }

    @Override
    public boolean onJsTimeout() {
        return webChromeClient.onJsTimeout();
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        webChromeClient.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return webChromeClient.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return webChromeClient.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        return webChromeClient.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        webChromeClient.getVisitedHistory(callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return webChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}