package com.project.container.containersapp.business.checkbox;

import android.os.Bundle;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;

import org.greenrobot.eventbus.EventBus;

public class CheckBoxDetailActivity extends JZXBaseActivity {

    public static final String ZYDM = "zydm";
    public static final String ZYJZXDM = "zyjzxdm";
    private String mZydm;          //作业代码
    private String mZyjzxdm;       //作业集装箱代码

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_box_detail;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mZydm = getIntent().getStringExtra(ZYDM);
        mZyjzxdm = getIntent().getStringExtra(ZYJZXDM);

        /*通知列表页返回时要刷新页面*/
        EventBus.getDefault().post(EventsKey.REFRESH_CHECKBOX);

    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
    }


}
