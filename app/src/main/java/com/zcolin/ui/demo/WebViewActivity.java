/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-23 上午10:36
 * ********************************************************
 */
package com.zcolin.ui.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fosung.ui.R;
import com.zcolin.frame.util.GsonUtil;
import com.zcolin.gui.ZAlert;
import com.zcolin.gui.ZDialog;
import com.zcolin.gui.webview.ZWebView;
import com.zcolin.gui.webview.jsbridge.BridgeHandler;
import com.zcolin.gui.webview.jsbridge.CallBackFunction;
import com.zcolin.gui.webview.jsbridge.DefaultHandler;

/**
 * 带JsBridge的webview的Demo
 */
public class WebViewActivity extends FragmentActivity implements OnClickListener {
    private ZWebView                   webView;
    private Button                     button;
    private Activity                   mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mActivity = this;

        webView = (ZWebView) findViewById(R.id.webView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        initWebView();
        loadUrl();

        getUserDataFrom_xx();
    }

    public void initWebView() {
        webView.setDefaultHandler(new DefaultHandler());//如果JS调用send方法，会走到DefaultHandler里
        webView.setSupportChooseFile(mActivity);
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                new ZAlert(mActivity).setMessage("监听到网页传入数据：" + data)
                                     .addSubmitListener(new ZDialog.ZDialogSubmitInterface() {
                                         @Override
                                         public boolean submit() {
                                             function.onCallBack("java 返回数据！！！");
                                             return true;
                                         }
                                     })
                                     .show();
            }
        });
        webView.registerStartActivity(mActivity);
        webView.registerFinishActivity(mActivity);
    }

    public void loadUrl() {
        webView.loadUrl("file:///android_asset/bridgewebview_html_demo.html");
    }

    public void callJsFunc(String funcName, String strParam) {
        webView.callHandler(funcName, strParam, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                new ZAlert(mActivity).setMessage("网页返回数据：" + data)
                                     .show();
            }
        });
        //webView.send("hello");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.processResult(requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View v) {
        if (button.equals(v)) {
            callJsFunc("functionInJs", "java 调用传入数据");
        }
    }

    public void getUserDataFrom_xx() {
        User user = new User();
        Location location = new Location();
        location.address = "SDU";
        user.location = location;
        user.name = "大头鬼";

        callJsFunc("functionInJs", GsonUtil.beanToString(user));
    }


    static class Location {
        String address;
    }

    static class User {
        String   name;
        Location location;
        String   testStr;
    }
}
