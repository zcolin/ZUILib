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
```

ZWebView：
```
webView = (ZWebView) findViewById(R.id.webView);
webView.setSupportVideoFullScreen(this).setSupportProgressBar();
```


