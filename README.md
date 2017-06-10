# ZUILib
##公用控件Lib。
####此项目现在为Alpha版本，Demo和一些需要的控件还需要优化
1. 一系列的Dialog，包括Alert,Confirm, 单选, 多选, Menu, 日期选择，进度条等。 
2. key-value形式的控件，ZKeyValueView,ZKeyValueEditView,ZKeySwitchView。
3. ZBanner导航条轮播图。
4. 小红点ZBadgeView。
5. 带清除按钮，带密码开关的EditText。
6. wheelView。
7. WebView, 可以设置支持进度条、视频全屏播放、js通讯、文件选择等。


## Gradle
app的build.gradle中添加
```
dependencies {
    compile 'com.github.zcolin:ZUILib:latest.release'
}
```
工程的build.gradle中添加
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

##USAGE
ZDialog系列：
```
new ZAlert(mActivity)
        .setTitle("ZAlert")
        .setMessage("这是一个Alert")
        .show();
        
new ZConfirm(mActivity)
        .setTitle("ZConfirm")
        .setMessage("这是一个通用对话框")
        .addSubmitListener(new ZDialog.ZDialogSubmitInterface() {
            @Override
            public boolean submit() {
                ToastUtil.toastShort("点击了确定");
                return true;
            }
        })
        .show();
        
final String[] arrt = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
new ZDialogRadioGroup(mActivity)
        .setTitle("ZDialogRadioGroup")
        .setDatas(arrt, "menu1")
        .addSubmitListener(new ZDialog.ZDialogParamSubmitInterface<Integer>() {
            @Override
            public boolean submit(Integer integer) {
                ToastUtil.toastShort("选择了" + arrt[integer]);
                return true;
            }
        })
        .show();
        
//gui库中的系列控件都可以使用initLayout函数来重新设置Layout，但是自己的Layout的控件Id必须和库中的控件Id相同
ZDialogBottomView.initLayout(R.layout.dlg_bottomitem);
ZConfirm.initLayout(R.layout.dlg_confirm);
ZAlert.initLayout(layoutId);
ZDialogMenu.initLayout(layoutId);
ZDialogRadioGroup.initLayout(layoutId);
ZDialogCheckBox.initLayout(layoutId);
ZDialogWheelDate.initLayout(layoutId);
ZDialogEdit.initLayout(layoutId);
ZDialogProgress.initLayout(layoutId);
ZKeyValueEditView.initLayout(layoutId);
ZKeyValueView.initLayout(layoutId);
ZKeySwitchView.initLayout(layoutId);

//以下方式可以重写构造继承重定义控件
ZAlert(Context context, int layout);
ZConfirm(Context context, int layout);
ZDialogCheckBox(Context context, int layout);
ZDialogEdit(Context context, int layout);
ZDialogMenu(Context context, int layout);
ZDialogProgress(Context context, int layout);
ZDialogRadioGroup(Context context, int layout);
ZDialogWheelDate(Context context, int layout);
ZDialogWheelTime(Context context, int layout);

//以下方式可以实现函数继承重定义控件
ZKeySwitchView#getSelfLayoutId()
ZKeySwitchEditView#getSelfLayoutId()
ZKeyValueView#getSelfLayoutId()
```



ZWebView：
```
webView = (ZWebView) findViewById(R.id.webView);
webView.setSupportVideoFullScreen(this)
        .setSupportProgressBar()
        .setSupportChooeFile(activity)
        .setSupportVideoFullScreen（activity）
        .setSupportAutoZoom()
        .setSupportJsBridge();
```
Other
```
//ZTagLayout  标签控件流式布局
ZTagLayout.Tag tag = tagLayout.createTag(String.format("第%d个标签", 0))
                          .setData(data)
                          .setBackground(null)
                          .setTextColor(getResources().getColor(R.color.black_light))
                          .setPressTextColor(getResources().getColor(R.color.black_light))
                          .setSelectTextColor(getResources().getColor(R.color.colorPrimary))
                          .setIsSelected(i == 5);
tagLayout.addTag(tag);

//ZoomImageView 手势放大缩小图片控件
zoomImageView.setMinScale(0.5f);
zoomImageView.setMaxScale(2f);
zoomImageView.setOnPhotoTapListener(new ZoomImageView.OnPhotoTapListener() {
    @Override
    public void onPhotoTap(View view, float x, float y) {
        toggleCoverView();
    }
});

//ZBanner 导航轮播图
banner.setBannerStyle(ZBanner.NUM_INDICATOR_TITLE)
  .setIndicatorGravity(ZBanner.RIGHT)
  .setBannerTitle(listTitle)
  .setDelayTime(4000)
  .setOnBannerClickListener(new ZBanner.OnBannerClickListener() {
      @Override
      public void OnBannerClick(View view, int position) {

      }
  })
  .setImages(listUrl)
  .startAutoPlay();
```
