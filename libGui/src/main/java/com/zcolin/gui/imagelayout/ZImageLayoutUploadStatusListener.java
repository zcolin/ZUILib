/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     19-1-29 上午9:14
 * ********************************************************
 */

/*
 * *********************************************************
 * author   zcolin
 * github   github.com/zcolin
 * email    wanglin2046@126.com
 * date     18-10-17 下午10:47
 * *********************************************************
 */

package com.zcolin.gui.imagelayout;

/**
 * 上传状态监听
 */
public interface ZImageLayoutUploadStatusListener {
    void onSuccess(String path);

    void onFailed(int code, String message);

    void onProgress(int progress);
}
