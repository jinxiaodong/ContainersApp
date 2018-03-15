package com.project.container.containersapp.business.checkbox;

import android.os.Bundle;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.JZXBaseActivity;

/*
* 验箱列表页面
* */
public class CheckBoxListActivilty extends JZXBaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_box_list_activilty;
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("验箱");
    }
}
