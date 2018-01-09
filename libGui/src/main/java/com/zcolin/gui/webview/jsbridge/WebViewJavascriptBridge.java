/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.gui.webview.jsbridge;

public interface WebViewJavascriptBridge {

    void send(String data);

    void send(String data, CallBackFunction responseCallback);
}
