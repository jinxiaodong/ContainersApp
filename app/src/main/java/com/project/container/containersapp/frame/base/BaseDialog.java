package com.project.container.containersapp.frame.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.constants.DeviceInfo;

/**
 * Created by xiaodong.jin on 2018/3/25.
 * description：
 */

public abstract class BaseDialog extends Dialog {

    protected Context context;

    public BaseDialog(Context context, int gravity, boolean horizontalFullScreen) {
        super(context, R.style.dialog_style);
        this.context = context;
        setContentView(getContentViewId());
        Window window = getWindow();
        if (gravity == Gravity.BOTTOM) {
            window.setWindowAnimations(R.style.dialog_anim_from_bottom);
        }
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = window.getAttributes();
        if (!horizontalFullScreen) {
            params.width = (int) (DeviceInfo.WIDTHPIXELS / 1.1);
        } else {
            params.width = (int) DeviceInfo.WIDTHPIXELS ;
        }
        params.gravity = gravity;
        window.setAttributes(params);
        initValue();
        initWidget();
        initListener();
        initData();
    }

    protected void initValue() {
    }

    protected void initWidget() {
    }

    protected void initListener() {
    }

    protected void initData() {
    }

    protected abstract int getContentViewId();
}
