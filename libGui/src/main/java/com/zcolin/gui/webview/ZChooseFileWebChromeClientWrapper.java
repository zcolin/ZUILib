/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/
package com.zcolin.gui.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 支持文件选择的WebChormeClient
 * <p>
 * webview默认的chromeClient是{@link ZWebChromeClientWrapper}
 */
public class ZChooseFileWebChromeClientWrapper extends ZWebChromeClientWrapper {
    private static final int REQUEST_CODE = 5200;

    private ValueCallback<Uri[]> mUploadMessages;
    private ValueCallback<Uri>   mUploadMessage;
    private Fragment             fragment;
    private Activity             activity;

    /**
     * Context 必须为Fragment或者Activity的子类
     */
    public ZChooseFileWebChromeClientWrapper(WebChromeClient webChromeClient, Fragment fragment) {
        super(webChromeClient);
        this.fragment = fragment;
    }

    /**
     * Context 必须为Fragment或者Activity的子类
     */
    public ZChooseFileWebChromeClientWrapper(WebChromeClient webChromeClient, Activity activity) {
        super(webChromeClient);
        this.activity = activity;
    }

    // For Android > 4.1.1 
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
        String acceptType = TextUtils.isEmpty(AcceptType) ? "*/*" : AcceptType;
        pickFile(null, uploadMsg, acceptType);
    }

    // For Android 3.0+ 文件选择  
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
        String acceptType = TextUtils.isEmpty(AcceptType) ? "*/*" : AcceptType;
        pickFile(null, uploadMsg, acceptType);
    }

    // For Android < 3.0  
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        pickFile(null, uploadMsg, "*/*");
    }

    // For Android > 4.4
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (!super.onShowFileChooser(webView, filePathCallback, fileChooserParams)) {
            String acceptType = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && fileChooserParams != null && fileChooserParams.getAcceptTypes() != null && fileChooserParams.getAcceptTypes().length > 0) {
                acceptType = fileChooserParams.getAcceptTypes()[0];
            }
            acceptType = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            pickFile(filePathCallback, null, acceptType);
        }
        return true;
    }

    /**
     * 实现选择文件方法
     */
    private void pickFile(ValueCallback<Uri[]> filePathCallbacks, ValueCallback<Uri> filePathCallback, String acceptType) {
        mUploadMessage = filePathCallback;
        mUploadMessages = filePathCallbacks;
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType(acceptType);

        if (fragment != null) {
            fragment.startActivityForResult(chooserIntent, REQUEST_CODE);
        } else if (activity != null) {
            activity.startActivityForResult(chooserIntent, REQUEST_CODE);
        }
    }

    /**
     * 在Activity或者fragment的onActivityResult中调用此函数
     */
    public boolean processResult(int requestCode, int resultCode, Intent intent) {
        Uri result = null;
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && intent != null) {
            result = intent.getData();
        }

        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (mUploadMessages != null) {
            mUploadMessages.onReceiveValue(result == null ? null : new Uri[]{result});
            mUploadMessages = null;
        }

        return result != null;
    }
}