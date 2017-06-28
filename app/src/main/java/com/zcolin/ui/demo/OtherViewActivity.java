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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.fosung.ui.R;
import com.zcolin.frame.imageloader.ImageLoaderUtils;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.ZBanner;
import com.zcolin.gui.ZDialogAsyncProgress;
import com.zcolin.gui.ZDialogProgress;
import com.zcolin.gui.ZEditTextWithClear;
import com.zcolin.gui.ZEditTextWithPassword;
import com.zcolin.gui.ZPopupMenu;
import com.zcolin.gui.ZTagLayout;
import com.zcolin.gui.ZTextSwitcher;
import com.zcolin.gui.ZoomImageView;

import java.util.ArrayList;

import static android.R.attr.data;


/**
 * 其他的一些View的示例
 */
public class OtherViewActivity extends FragmentActivity {
    private Activity      mActivity;
    private ZTextSwitcher textSwitcher;
    private ZBanner       banner;
    private ZTagLayout    tagLayout;
    private ZoomImageView zoomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherview);
        mActivity = this;

        ZEditTextWithClear etClear = (ZEditTextWithClear) findViewById(R.id.et_clear);
        etClear.setHint("这是Hint");
        ZEditTextWithPassword etPassword = (ZEditTextWithPassword) findViewById(R.id.et_password);
        etPassword.setHint("这是ZEditTextWithPassword");
        textSwitcher = (ZTextSwitcher) findViewById(R.id.view_textswitcher);
        tagLayout = (ZTagLayout) findViewById(R.id.tagview);
        banner = (ZBanner) findViewById(R.id.view_banner);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoomImageView);
        Button btn1 = (Button) findViewById(R.id.btn_1);
        Button btn2 = (Button) findViewById(R.id.btn_2);
        final Button btn3 = (Button) findViewById(R.id.btn_3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlgAsyncProgress();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlgProgress();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(btn3);
            }
        });
        ImageLoaderUtils.displayImage(this, "http://img1.imgtn.bdimg.com/it/u=1480985633,1206349730&fm=214&gp=0.jpg", zoomImageView);
        setUpTagView();
        startBanner();
        startTextSwitcher();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!textSwitcher.isStart() && textSwitcher.isInit()) {
            textSwitcher.startSwitcher();
        }

        if (!banner.isStart() && banner.isInit()) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        textSwitcher.stopSwitcher();
        banner.stopAutoPlay();
    }

    public void startTextSwitcher() {
        String text = "只要用过mvp这个问题可能很多人都知道。写mvp的时候，presenter会持有view，如果presenter有后台异步的长时间的动作，" +
                "比如网络请求，这时如果返回退出了Activity，后台异步的动作不会立即停止，这里就会有内存泄漏的隐患，所以会在presenter中加入" +
                "一个销毁view的方法。现在就在之前的项目中做一下修改";
        textSwitcher.setTextColor(Color.BLACK)
                    .setTextSize(20)
                    .setSwitchInterval(4000)
                    .setText(text)
                    //                    .setOutAnima(mActivity, R.anim.textswitcher_slide_out)
                    //                    .setInAnima(mActivity, R.anim.textswitcher_slide_in)
                    .startSwitcher();

    }

    public void startBanner() {
        ArrayList<String> listUrl = getListUrl();
        banner.setBannerStyle(ZBanner.CIRCLE_INDICATOR_TITLE)
              .setIndicatorGravity(ZBanner.CENTER)
              .setBannerTitle(listUrl)
              .setDelayTime(4000)
              .setOnBannerClickListener(new ZBanner.OnBannerClickListener() {
                  @Override
                  public void OnBannerClick(View view, int position) {
                      ToastUtil.toastShort("点击了第" + (position + 1) + "张图片");
                  }
              })
              .setImages(listUrl)
              .startAutoPlay();
    }

    private void setUpTagView() {
        ArrayList<ZTagLayout.Tag> listTag = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ZTagLayout.Tag tag = tagLayout.createTag(String.format("第%d个标签", i))
                                          .setData(data)
                                          .setBackground(null)
                                          .setTextColor(getResources().getColor(R.color.black_light))
                                          .setPressTextColor(getResources().getColor(R.color.black_light))
                                          .setSelectTextColor(getResources().getColor(R.color.colorPrimary))
                                          .setIsSelected(i == 5);
            listTag.add(tag);
        }
        tagLayout.setOnTagClickListener(new ZTagLayout.OnTagClickListener() {
            @Override
            public void onTagClick(int position, ZTagLayout.Tag tag) {
                if (tag.getData() != null) {
                    ToastUtil.toastShort(String.format("第%d个标签", position));
                }
            }
        });
        tagLayout.addTags(listTag);
    }

    private ArrayList<String> getListUrl() {
        String url_1 = "http://img.ycwb.com/news/attachement/jpg/site2/20110226/90fba60155890ed3082500.jpg";
        String url_2 = "http://cdn.duitang.com/uploads/item/201112/04/20111204012148_wkT88.jpg";
        String url_3 = "http://img6.faloo.com/Picture/680x580/0/449/449476.jpg";
        ArrayList<String> listUrl = new ArrayList<>();
        listUrl.add(url_1);
        listUrl.add(url_2);
        listUrl.add(url_3);
        return listUrl;
    }

    /**
     * 异步执行任务，显示ProgressDialog
     */
    private void showDlgAsyncProgress() {
        final ZDialogAsyncProgress dlg = new ZDialogAsyncProgress(mActivity);
        dlg.setDoInterface(new ZDialogAsyncProgress.DoInterface() {
            @Override
            public ZDialogAsyncProgress.ProcessInfo onDoInback() {
                SystemClock.sleep(1000);
                dlg.setMessage("执行第二步");
                SystemClock.sleep(2000);

                ZDialogAsyncProgress.ProcessInfo info = new ZDialogAsyncProgress.ProcessInfo();
                info.msg = "执行结果：success";
                return info;
            }

            @Override
            public void onPostExecute(ZDialogAsyncProgress.ProcessInfo info) {
                ToastUtil.toastShort(info.msg);
            }
        });
        dlg.execute(0);
    }

    /**
     * 异步执行任务，显示ProgressDialog
     */
    private void showDlgProgress() {
        final ZDialogProgress progress = new ZDialogProgress(mActivity);
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                SystemClock.sleep(2000);
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.show();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                progress.dismiss();
            }
        }.execute(0);
    }

    private void showPopupMenu(Button btn3) {
        ZPopupMenu.Item item = new ZPopupMenu.Item();
        item.setBackground(mActivity, R.drawable.gui_listitem_selector);
        item.setText("富强福");
        new ZPopupMenu(mActivity).addAction("和谐福")
                                 .addAction("有善福")
                                 .addAction("敬业福")
                                 .addAction(item)
                                 .setOnItemClickListener(new ZPopupMenu.OnItemOnClickListener() {
                                     @Override
                                     public boolean onItemClick(ZPopupMenu.Item item, int position) {
                                         ToastUtil.toastShort(String.valueOf(item.text));
                                         return true;
                                     }
                                 })
                                 .show(btn3, -250, 0, Gravity.RIGHT);
    }
}
