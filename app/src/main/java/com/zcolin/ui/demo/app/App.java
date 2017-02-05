/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.zcolin.ui.demo.app;

import com.zcolin.frame.app.BaseApp;
import com.zcolin.gui.ZConfirm;
import com.zcolin.gui.ZDialogBottomView;
import com.fosung.ui.R;

/**
 * 程序入口
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        initDialogLayout();
    }

    public void initDialogLayout() {
        //gui库中的系列控件都可以使用initLayout函数来重新设置Layout，但是自己的Layout的控件Id必须和库中的控件Id相同
        ZDialogBottomView.initLayout(R.layout.dlg_bottomitem);
        ZConfirm.initLayout(R.layout.dlg_confirm);
        //        ZAlert.initLayout(layoutId);
        //        ZDialogMenu.initLayout(layoutId);
        //        ZDialogRadioGroup.initLayout(layoutId);
        //        ZDialogCheckBox.initLayout(layoutId);
        //        ZDialogWheelDate.initLayout(layoutId);
        //        ZDialogEdit.initLayout(layoutId);
        //        ZDialogProgress.initLayout(layoutId);
        //        ZKeyValueEditView.initLayout(layoutId);
        //        ZKeyValueView.initLayout(layoutId);
        //        ZKeySwitchView.initLayout(layoutId);
    }
}
