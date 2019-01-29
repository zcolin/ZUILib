/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     19-1-29 上午9:14
 * ********************************************************
 */
package com.zcolin.gui.imagelayout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcolin.gui.ZDialog;
import com.zcolin.gui.ZDialogAsyncProgress;
import com.zcolin.gui.ZDialogPhotoDetail;
import com.zcolin.gui.helper.ZUIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 图片添加布局，调用图片选择，选择完毕后回填显示
 */
public class ZImageLayout extends RelativeLayout {
    private int maxCount = 20;
    private GridLayout gridLayout;
    private TextView   tvHint;
    private List<ZImageLayoutItem> listLocalImage    = new ArrayList<>();//本地图片
    private List<ZImageLayoutItem> listNetImage      = new ArrayList<>();//服务器返回的网络图片
    private int[]                  compassResolution = new int[2];//图片压缩分辨率
    private int                    columnCount       = 5;         //横向有多少个view
    private int columnMargin;                                    //横向view间隔
    private int itemWidth;
    private int itemHeight;
    private boolean isEditable = true;
    private OnImageLayoutRefreshListener                listener;
    private OnClickListener                             addClickListener;
    private ZDialog.ZDialogParamSubmitListener<Integer> itemImageClickListener;
    private ZImageLayoutUpload                          imageUploadProxy;
    private boolean                                     uploading;

    public ZImageLayout(Context context) {
        this(context, null);
    }

    public ZImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //默认图片压缩分辨率
        compassResolution[0] = 720;
        compassResolution[1] = 1280;

        init(context);
    }

    private void init(Context context) {
        columnMargin = ZUIHelper.dip2px(context, 5);
        itemWidth = (ZUIHelper.getScreenWidth(context) - (columnMargin * 2 * (columnCount)) - getPaddingLeft() - getPaddingRight()) / columnCount;
        itemHeight = itemWidth;

        gridLayout = new GridLayout(context);
        addView(gridLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //提示字段
        tvHint = new TextView(context);
        tvHint.setHint("格式jpg、png");
        LayoutParams rlParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlParams.setMargins(getPaddingLeft() + columnMargin * 3 + itemWidth, itemHeight * 3 / 4, 0, 0);
        addView(tvHint, rlParams);

        refreshLayout();
    }

    /**
     * 设置布局变动监听
     */
    public void setImageLayoutRefreshListener(OnImageLayoutRefreshListener listener) {
        this.listener = listener;
    }

    /**
     * 是否上传完成
     */
    public boolean isUploadComplete() {
        return !uploading;
    }

    /**
     * 设置图片上上传代理，不设置则添加图片时则不会启用上传状态监听
     */
    public void setImageLayoutUploadProxy(ZImageLayoutUpload uploadImageProxy) {
        this.imageUploadProxy = uploadImageProxy;
    }

    /**
     * 设置item宽高
     * 默认会使用itemWidth=itemHeight=screenWidth/column-padding-margin，所以尽量不单独设置宽高
     */
    public void setItemSize(int itemWidth, int itemHeight) {
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
    }

    /**
     * 列数量
     */
    public void setColumnCount(int count) {
        this.columnCount = count;
        removeAllViews();
        init(getContext());
    }

    /**
     * 设置提示字段
     */
    public void setHint(String strHint) {
        tvHint.setHint(strHint);
    }

    /**
     * 设置最大上传张数
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 设置是否可以编辑，默认true
     */
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        refreshLayout();
    }

    /**
     * 设置压缩分辨率
     */
    public void setCompassResolution(int x, int y) {
        compassResolution[0] = x;
        compassResolution[1] = y;
    }

    /**
     * 获取本地图片
     */
    public List<String> getLocalImages() {
        List<String> list = new ArrayList<>();
        for (ZImageLayoutItem imageLayoutItem : listLocalImage) {
            list.add(imageLayoutItem.getImagePath());
        }
        return list;
    }

    /**
     * 获取网络图片
     */
    public List<String> getNetImages() {
        List<String> list = new ArrayList<>();
        for (ZImageLayoutItem imageLayoutItem : listNetImage) {
            list.add(imageLayoutItem.getImagePath());
        }
        return list;
    }


    /**
     * 设置图片数据源
     */
    public void setNetImage(List<String> list) {
        this.listNetImage.clear();
        this.addToNetImage(list);
    }

    /**
     * 增加网络图片数据源
     */
    public void addToNetImage(List<String> list) {
        if (list != null) {
            for (String image : list) {
                this.addToNetList(image);
            }
        }
        refreshLayout();
    }

    /**
     * 增加网络图片数据源
     */
    public void addToNetImage(String image) {
        addToNetList(image);
        refreshLayout();
    }

    /**
     * 设置本地图片数据源
     */
    public void setLocalImage(List<String> list) {
        this.listLocalImage.clear();
        this.addLocalImage(list);
    }

    /**
     * 增加本地图片数据源
     */
    public void addLocalImage(List<String> list) {
        if (list != null) {
            for (String image : list) {
                this.addToLocalImageList(image);
            }
        }
        refreshLayout();
    }

    /**
     * 增加本地图片数据源
     */
    public void addLocalImage(String image) {
        addToLocalImageList(image);
        refreshLayout();
    }

    private void addToLocalImageList(String image) {
        if (image != null) {
            if (this.listLocalImage.size() + this.listNetImage.size() < maxCount) {
                ZImageLayoutItem item = new ZImageLayoutItem(getContext());
                item.initAsLocalImage(image, isEditable, (v) -> {
                    listLocalImage.remove(item);
                    refreshLayout();
                });
                listLocalImage.add(item);

                //如果有上传代理，则立即开始遍历上传
                if (imageUploadProxy != null) {
                    uploadImage();
                }
            }
        }
    }

    private void addToNetList(String imageUrl) {
        if (imageUrl != null) {
            if (this.listNetImage.size() + this.listLocalImage.size() < maxCount) {
                ZImageLayoutItem item = new ZImageLayoutItem(getContext());
                item.initAsNetImage(imageUrl, isEditable, (v) -> {
                    listNetImage.remove(item);
                    refreshLayout();
                });
                this.listNetImage.add(item);
            }
        }
    }

    /**
     * 刷新布局
     */
    public void refreshLayout() {
        tvHint.setVisibility((listLocalImage.size() + listNetImage.size() == 0 && isEditable) ? VISIBLE : GONE);
        gridLayout.removeAllViews();
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);
            GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.width = itemWidth;
            params.height = itemHeight;
            if (i / columnCount == 0) {
                params.setMargins(columnMargin, 0, columnMargin, columnMargin);
            } else {
                params.setMargins(columnMargin, columnMargin, columnMargin, columnMargin);
            }
            gridLayout.addView(getItem(i), params);
        }

        if (listener != null) {
            listener.onRefresh(count, maxCount);
        }
    }

    /**
     * 获取上传成功的图片url,需要自己判断{@link #isUploadComplete()}之后再调用此方法
     */
    public List<String> getUploadSuccessImageUrlList() {
        List<String> list = new ArrayList<>();
        for (ZImageLayoutItem imageLayoutItem : listLocalImage) {
            if (imageLayoutItem.getUploadStatus() == ZImageLayoutItem.UPLOADIMAGE_STATE_SUCCESSED) {
                list.add(imageLayoutItem.getUploadNetImagePath());
            }
        }
        return list;
    }

    private int getItemCount() {
        if (listLocalImage.size() + listNetImage.size() >= maxCount || !isEditable) {
            return listLocalImage.size() + listNetImage.size();
        } else {
            return listLocalImage.size() + listNetImage.size() + 1;
        }
    }

    private ZImageLayoutItem getItem(int position) {
        ZImageLayoutItem item;
        if (position < listLocalImage.size() + listNetImage.size()) {
            if (position < listNetImage.size()) {
                item = listNetImage.get(position);
            } else {
                item = listLocalImage.get(position - listNetImage.size());
            }

            item.setOnClickListener(v -> {
                if (itemImageClickListener != null) {
                    itemImageClickListener.submit(position);
                } else {
                    try {
                        List<String> list = new ArrayList<>();
                        for (ZImageLayoutItem imageItem : listNetImage) {
                            list.add(imageItem.getImagePath());
                        }
                        for (ZImageLayoutItem imageItem : listLocalImage) {
                            list.add(imageItem.getImagePath());
                        }

                        ZDialogPhotoDetail.PhotoBean bean = new ZDialogPhotoDetail.PhotoBean();
                        bean.images = list.toArray(new String[list.size()]);
                        bean.index = position;
                        new ZDialogPhotoDetail(((Activity) getContext()), bean).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            item = new ZImageLayoutItem(getContext());
            item.initAsAdd();
            item.setOnClickListener(addClickListener);
        }
        return item;
    }

    /**
     * 添加按钮的点击回调 参照
     * PhotoSelectedUtil.selectPhoto(getContext(), getCurMaxCount(), (resultCode, data) -> {
     * if (resultCode == Activity.RESULT_OK && data != null) {
     * final List<String> listPath = Matisse.obtainPathResult(data);
     * compassImagesToLocalList(listPath);
     * }
     */
    public void setOnAddClickListener(OnClickListener clickListener) {
        this.addClickListener = clickListener;
    }

    /**
     * 图片点击回调，默认会显示详图弹框，设置之后进行覆盖
     */
    public void setOnImageItemClick(ZDialog.ZDialogParamSubmitListener<Integer> clickListener) {
        this.itemImageClickListener = clickListener;
    }

    /**
     * 获取当前可选的最大数量
     */
    public int getCurMaxCount() {
        return maxCount - listNetImage.size() - listLocalImage.size();
    }

    /**
     * 图片压缩完成之后，加入本地列表
     */
    public void compassImagesToLocalList(final List<String> listPath) {
        ZDialogAsyncProgress dlg = new ZDialogAsyncProgress(getContext());
        dlg.setDoInterface(new ZDialogAsyncProgress.DoInterface() {
            @Override
            public ZDialogAsyncProgress.ProcessInfo onDoInback() {
                ZDialogAsyncProgress.ProcessInfo info = new ZDialogAsyncProgress.ProcessInfo();
                final List<String> listTempPath = new ArrayList<>();
                if (listPath != null && listPath.size() > 0) {
                    for (String s : listPath) {
                        String tempPath = getContext().getApplicationContext().getExternalCacheDir() + "/img_cache/" + UUID.randomUUID().toString() + ".png ";
                        ZUIHelper.copyPic(s, tempPath, compassResolution[0], compassResolution[1], 300);//压缩图片到缓存路径
                        listTempPath.add(tempPath);
                    }
                    info.info = listTempPath;
                }
                return info;
            }

            @Override
            public void onPostExecute(ZDialogAsyncProgress.ProcessInfo info) {
                List<String> list = ((List<String>) info.info);
                addLocalImage(list);
            }
        }).show();
    }

    /**
     * 上传图片，会遍历没有上传的本地图片，进行状态监测，然后一一上传
     */
    private void uploadImage() {
        for (ZImageLayoutItem imageLayoutItem : listLocalImage) {
            if (imageLayoutItem.getUploadStatus() == ZImageLayoutItem.UPLOADIMAGE_STATE_NONE) {
                imageLayoutItem.setUploadStatus(ZImageLayoutItem.UPLOADIMAGE_STATE_WAIT_UPLOAD);
            }
        }

        if (uploading) {
            return;
        }

        upload();
    }

    /**
     * 遍历上传
     */
    private void upload() {
        List<ZImageLayoutItem> list = new ArrayList<>();
        for (ZImageLayoutItem imageLayoutItem : listLocalImage) {
            if (imageLayoutItem.getUploadStatus() == ZImageLayoutItem.UPLOADIMAGE_STATE_WAIT_UPLOAD) {
                list.add(imageLayoutItem);
            }
        }

        if (list.size() > 0 && imageUploadProxy != null) {
            uploading = true;
            ZImageLayoutItem item = list.get(0);
            item.setUploadStatus(ZImageLayoutItem.UPLOADIMAGE_STATE_UPLOADING);
            imageUploadProxy.upload(item.getImagePath(), new ZImageLayoutUploadStatusListener() {
                @Override
                public void onSuccess(String path) {
                    item.setUploadStatus(ZImageLayoutItem.UPLOADIMAGE_STATE_SUCCESSED);
                    item.setUploadNetImagePath(path);
                    upload();
                }

                @Override
                public void onFailed(int code, String message) {
                    item.setUploadStatus(ZImageLayoutItem.UPLOADIMAGE_STATE_FAILED);
                    upload();
                }

                @Override
                public void onProgress(int progress) {
                    item.setUploadProgress(progress);
                }
            });
        } else {
            uploading = false;
        }
    }


    /**
     * 图片数据源更新回调
     */
    public interface OnImageLayoutRefreshListener {
        void onRefresh(int selectedCount, int maxCount);
    }
}
