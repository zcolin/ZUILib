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
 * 图片布局组件的上传代理
 */
public interface ZImageLayoutUpload {
    void upload(String filePath, ZImageLayoutUploadStatusListener listener);
}
