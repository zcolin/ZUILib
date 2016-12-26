/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.gui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 进度条封装类，执行的内容会自动在子线程中调用
 * 调用{@link ZDialogAsyncProgress#setDoInterface(DoInterface)}执行后台任务
 */
public class ZDialogAsyncProgress extends AsyncTask<Integer, Integer, ZDialogAsyncProgress.ProcessInfo> {

    private ZDialogProgress processBar;
    private DoInterface     doInter;
    private MyHandler       handler;

    public ZDialogAsyncProgress(Context context) {
        processBar = new ZDialogProgress(context);
        handler = new MyHandler(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        processBar.show();
    }

    @Override
    protected ProcessInfo doInBackground(Integer... params) {
        ProcessInfo info = null;
        if (doInter != null) {
            info = doInter.onDoInback();
        }

        return info;
    }

    @Override
    protected void onPostExecute(ProcessInfo info) {
        super.onPostExecute(info);
        processBar.dismiss();
        if (doInter != null)
            doInter.onPostExecute(info);
    }

    /**
     * 添加任务回调接口
     */
    public void setDoInterface(DoInterface doInter) {
        this.doInter = doInter;
    }

    /**
     * 设置信息显示，此方法需要在回调接口中调用
     *
     * @param message 进度条显示信息
     */
    public void setMessageInBack(String message) {
        Message msg = handler.obtainMessage();
        msg.arg1 = 0;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    /**
     * 进度条显示
     */
    public void show() {
        this.execute(0);
    }

    /**
     * 任务回调接口
     */
    public interface DoInterface {

        ProcessInfo onDoInback();

        void onPostExecute(ProcessInfo info);
    }

    /**
     * 处理子线程任务和主线程之间的交互
     */
    static class MyHandler extends Handler {

        ZDialogAsyncProgress proBar;

        public MyHandler(ZDialogAsyncProgress proBar) {
            WeakReference<ZDialogAsyncProgress> weakProBar = new WeakReference<ZDialogAsyncProgress>(proBar);
            this.proBar = weakProBar.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.arg1 == 0 && msg.obj != null) {
                this.proBar.processBar.setMessage((String) msg.obj);
            }
        }
    }


    /**
     * 处理信息携带类
     */
    public static class ProcessInfo {

        private int       id;        //id
        private String    msg;      //描述
        private Exception e;        //异常
        private boolean   flag;    //扩展boolean
        private Object    info;    //扩展对象

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Exception getE() {
            return e;
        }

        public void setE(Exception e) {
            this.e = e;
        }

        public Object getInfo() {
            return info;
        }

        public void setInfo(Object info) {
            this.info = info;
        }
    }
}
