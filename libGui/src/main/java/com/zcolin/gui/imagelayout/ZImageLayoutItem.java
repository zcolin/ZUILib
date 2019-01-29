/*
 * *********************************************************
 * author   zcolin
 * github   github.com/zcolin
 * email    wanglin2046@126.com
 * date     18-10-17 下午10:47
 * *********************************************************
 */

package com.zcolin.gui.imagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zcolin.gui.R;

import java.io.File;

/**
 * ImageLayout单个图片组件
 */
public class ZImageLayoutItem extends RelativeLayout {
    public static final int UPLOADIMAGE_STATE_NONE        = 0;
    public static final int UPLOADIMAGE_STATE_WAIT_UPLOAD = 1;
    public static final int UPLOADIMAGE_STATE_UPLOADING   = 2;
    public static final int UPLOADIMAGE_STATE_FAILED      = 3;
    public static final int UPLOADIMAGE_STATE_SUCCESSED   = 4;

    private ImageView      imageView;
    private RelativeLayout rlUploading;
    private TextView       tvUploading;
    private TextView       tvWaitUpload;
    private ImageView      ivUploadError;
    private Button         btnDel;
    private String         path;

    private int    uploadStatus;
    private int    uploadProgress;
    private String uploadNetImagePath;

    public ZImageLayoutItem(Context context) {
        this(context, null);
    }

    public ZImageLayoutItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.gui_view_imagelayout_add_item, this);
        imageView = findViewById(R.id.iv_image);
        btnDel = findViewById(R.id.btn_delete);
        tvWaitUpload = findViewById(R.id.tv_wait_upload);
        rlUploading = findViewById(R.id.rl_uploading);
        tvUploading = findViewById(R.id.tv_uploadding);
        ivUploadError = findViewById(R.id.tv_upload_error);
    }

    public void initAsNetImage(String url, boolean isDeleteAble, OnClickListener deleteClickListener) {
        this.path = url;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.gui_imagelayout_load_placeholder).error(R.drawable.gui_imagelayout_load_error)).into(imageView);
        btnDel.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onClick(v);
            }
        });
        btnDel.setVisibility(isDeleteAble ? View.VISIBLE : GONE);
    }

    public void initAsLocalImage(String path, boolean isDeleteAble, OnClickListener deleteClickListener) {
        this.path = path;
        final File file = new File(path);
        Glide.with(getContext()).load(file).apply(new RequestOptions().placeholder(R.drawable.gui_imagelayout_load_placeholder).error(R.drawable.gui_imagelayout_load_error)).into(imageView);
        btnDel.setOnClickListener(v -> {
            if (file.exists()) {
                file.delete();
            }
            if (deleteClickListener != null) {
                deleteClickListener.onClick(v);
            }
        });
        btnDel.setVisibility(isDeleteAble ? View.VISIBLE : GONE);
    }

    public void initAsAdd() {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.gui_imagelayout_add);
        btnDel.setVisibility(GONE);
    }

    public String getImagePath() {
        return path;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int status) {
        this.uploadStatus = status;
        switch (status) {
            case UPLOADIMAGE_STATE_NONE:
            case UPLOADIMAGE_STATE_SUCCESSED:
                rlUploading.setVisibility(INVISIBLE);
                tvWaitUpload.setVisibility(INVISIBLE);
                ivUploadError.setVisibility(INVISIBLE);
                break;
            case UPLOADIMAGE_STATE_WAIT_UPLOAD:
                rlUploading.setVisibility(INVISIBLE);
                tvWaitUpload.setVisibility(VISIBLE);
                ivUploadError.setVisibility(INVISIBLE);
                break;
            case UPLOADIMAGE_STATE_UPLOADING:
                rlUploading.setVisibility(VISIBLE);
                tvWaitUpload.setVisibility(INVISIBLE);
                ivUploadError.setVisibility(INVISIBLE);
                break;
            case UPLOADIMAGE_STATE_FAILED:
                rlUploading.setVisibility(INVISIBLE);
                tvWaitUpload.setVisibility(INVISIBLE);
                ivUploadError.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setUploadProgress(int progress) {
        this.uploadProgress = progress;
        if (progress > 0 && progress < 100) {
            rlUploading.setVisibility(VISIBLE);
            tvUploading.setText(progress + "%");
        } else {
            rlUploading.setVisibility(INVISIBLE);
        }
    }

    public int getUploadProgress() {
        return this.uploadProgress;
    }

    /**
     * 设置上传成功的图片地址
     */
    public void setUploadNetImagePath(String uploadNetImagePath) {
        this.uploadNetImagePath = uploadNetImagePath;
    }

    /**
     * 获取上传成功的图片地址
     */
    public String getUploadNetImagePath() {
        return this.uploadNetImagePath;
    }
}
