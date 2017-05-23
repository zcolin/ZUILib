/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/
package com.zcolin.gui.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zcolin.gui.R;
import com.zcolin.gui.helper.ZUIHelper;
import com.zcolin.gui.webview.jsbridge.BridgeHandler;
import com.zcolin.gui.webview.jsbridge.BridgeWebView;
import com.zcolin.gui.webview.jsbridge.CallBackFunction;


/**
 * 封装的Webview的控件
 */
public class ZWebView extends BridgeWebView {

    private ZWebViewClientWrapper   webViewClientWrapper;
    private ZWebChromeClientWrapper webChromeClientWrapper;
    private ProgressBar             proBar;            //加载進度条
    private boolean                 isSupportJsBridge;
    private boolean                 isSupportH5Location;

    public ZWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    //网页属性设置
    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setHorizontalScrollBarEnabled(false);
        //webView.addJavascriptInterface(new JsInterUtil(), "javautil");
        setHorizontalScrollbarOverlay(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    public void setWebViewClient(@NonNull WebViewClient webViewClient) {
        this.webViewClientWrapper = new ZWebViewClientWrapper(webViewClient);
        webViewClientWrapper.setProgressBar(proBar);
        if (isSupportJsBridge) {
            webViewClientWrapper.setSupportJsBridge();
        }
        super.setWebViewClient(webViewClientWrapper);
    }

    @Override
    public void setWebChromeClient(@NonNull WebChromeClient webChromeClient) {
        if (webChromeClientWrapper == null) {
            this.webChromeClientWrapper = new ZWebChromeClientWrapper(webChromeClient);
        } else if (webChromeClientWrapper instanceof ZChooseFileWebChromeClientWrapper || webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            this.webChromeClientWrapper.setWebChromeClient(webChromeClient);
        } else {
            this.webChromeClientWrapper = new ZWebChromeClientWrapper(webChromeClient);
        }

        webChromeClientWrapper.setProgressBar(proBar);
        if (isSupportH5Location) {
            webChromeClientWrapper.setSupportH5Location();
        }
        super.setWebChromeClient(webChromeClientWrapper);
    }

    /**
     * 支持文件选择
     * <p>
     * <p>
     * 需要在Activity的onActivityResult中调用:
     * <pre>
     *  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
     *       super.onActivityResult(requestCode, resultCode, intent);
     *      webView.processResult(requestCode, resultCode, intent);
     *  }
     * </pre>
     */
    public ZWebView setSupportChooseFile(Activity activity) {
        webChromeClientWrapper = new ZChooseFileWebChromeClientWrapper(webChromeClientWrapper.getWebChromeClient(), activity);
        setWebChromeClient(webChromeClientWrapper.getWebChromeClient());
        return this;
    }

    /**
     * 支持文件选择
     * <p>
     * <p>
     * 需要在Fragment的onActivityResult中调用:
     * <pre>
     *  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
     *      webView.processResult(requestCode, resultCode, intent);
     *  }
     * </pre>
     */
    public ZWebView setSupportChooseFile(Fragment fragment) {
        webChromeClientWrapper = new ZChooseFileWebChromeClientWrapper(webChromeClientWrapper.getWebChromeClient(), fragment);
        setWebChromeClient(webChromeClientWrapper.getWebChromeClient());
        return this;
    }

    /**
     * 支持视频全屏
     * <p>
     * <strong>必须在Activity的manifest文件中指定 android:configChanges="keyboardHidden|orientation|screenSize"</strong>
     * <p>
     * <pre>
     * <strong>在Activity的OnKeyDown中如下：</strong>
     *  public boolean onKeyDown(int keyCode, KeyEvent event) {
     *      if (keyCode == KeyEvent.KEYCODE_BACK) {
     *          if (webView.hideCustomView()) {
     *              return true;
     *          } else if (webView.canGoBack()) {
     *               webView.goBack();
     *              return true;
     *           }
     *       }
     *      return super.onKeyDown(keyCode, event);
     *  }
     * </pre>
     */
    public ZWebView setSupportVideoFullScreen(Activity activity) {
        ViewGroup group = (ViewGroup) this.getParent();
        FrameLayout container = new FrameLayout(getContext());
        int index = group.indexOfChild(this);

        //将原来的布局之间添加一层，用来盛放webView和视频全屏控件
        group.removeView(this);
        group.addView(container, index, this.getLayoutParams());
        container.addView(this, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //添加视频ViewContainer
        FrameLayout flCustomContainer = new FrameLayout(getContext());
        flCustomContainer.setVisibility(View.INVISIBLE);
        container.addView(flCustomContainer, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View videoProgressView = LayoutInflater.from(activity)
                                               .inflate(R.layout.gui_view_webview_video_progress, null);
        webChromeClientWrapper = new ZVideoFullScreenWebChromeClient(webChromeClientWrapper.getWebChromeClient(), activity, this, flCustomContainer, videoProgressView);
        setWebChromeClient(webChromeClientWrapper.getWebChromeClient());
        return this;
    }

    public void setCustomViewShowStateListener(ZVideoFullScreenWebChromeClient.CustomViewShowStateListener customViewShowStateListener) {
        if (webChromeClientWrapper != null && webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            ((ZVideoFullScreenWebChromeClient) webChromeClientWrapper).setCustomViewShowStateListener(customViewShowStateListener);
        }
    }

    /**
     * 支持显示进度条
     */
    public ZWebView setSupportProgressBar() {
        ViewGroup group = (ViewGroup) this.getParent();
        FrameLayout container = new FrameLayout(getContext());
        int index = group.indexOfChild(this);
        group.removeView(this);
        group.addView(container, index, this.getLayoutParams());
        container.addView(this, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        proBar = (ProgressBar) LayoutInflater.from(getContext())
                                             .inflate(R.layout.gui_view_webview_progressbar, null);
        container.addView(proBar, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, ZUIHelper.dip2px(getContext(), 2)));
        webChromeClientWrapper.setProgressBar(proBar);
        webViewClientWrapper.setProgressBar(proBar);
        return this;
    }

    /**
     * 设置是否支持JsBridge
     */
    public void setSupportJsBridge() {
        isSupportJsBridge = true;
        webViewClientWrapper.setSupportJsBridge();
    }

    /**
     * 设置是否支持自动缩放
     */
    public void setSupportAutoZoom() {
        WebSettings webSettings = getSettings();
        webSettings.setUseWideViewPort(true);//关键点  
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
    }

    /**
     * 设置是否支持H5定位
     * <p>
     * 需要声明权限
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     */
    public void setSupportH5Location() {
        isSupportH5Location = true;
        webChromeClientWrapper.setSupportH5Location();
        WebSettings webSettings = getSettings();
        webSettings.setDomStorageEnabled(true);
    }

    /**
     * 支持网页下载
     */
    public void setSupportDownLoad() {
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });
    }

    public WebViewClient getWebViewClient() {
        return webViewClientWrapper.getWebViewClient();
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClientWrapper.getWebChromeClient();
    }

    /**
     * 支持文件选择的时候需要在onActivity中调用此函数
     */
    public boolean processResult(int requestCode, int resultCode, Intent intent) {
        if (webChromeClientWrapper instanceof ZChooseFileWebChromeClientWrapper) {
            return ((ZChooseFileWebChromeClientWrapper) webChromeClientWrapper).processResult(requestCode, resultCode, intent);
        }
        return false;
    }

    /**
     * 如果在视频全屏播放状态，取消全屏播放
     */
    public boolean hideCustomView() {
        if (webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            ZVideoFullScreenWebChromeClient client = ((ZVideoFullScreenWebChromeClient) webChromeClientWrapper);
            if (client.isCustomViewShow()) {
                client.onHideCustomView();
                return true;
            }
        }
        return false;
    }


    /**
     * 是否在全屏播放页面
     */
    public boolean isVideoFullScreen() {
        if (webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            ZVideoFullScreenWebChromeClient client = ((ZVideoFullScreenWebChromeClient) webChromeClientWrapper);
            return client.isCustomViewShow();
        }
        return false;
    }


    /**
     * 注册启动Activity的web交互
     */
    public ZWebView registerStartActivity(final Activity activity) {
        registerHandler("startActivity", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                try {
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName(activity.getPackageName(), activity.getPackageName() + "build/intermediates/exploded-aar/com.android.support/support-v4/23.2.1/res" + data);
                    intent.setComponent(componentName);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    /**
     * 注册启动Activity的web交互
     */
    public ZWebView registerFinishActivity(final Activity activity) {
        registerHandler("finishActivity", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                activity.finish();
            }
        });
        return this;
    }
}
