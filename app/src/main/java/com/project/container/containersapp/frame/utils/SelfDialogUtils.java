package com.project.container.containersapp.frame.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.project.container.containersapp.frame.view.SelfDialog;

/**
 * Created by xiaodong.jin on 2018/3/30.
 * description：
 */

public class SelfDialogUtils {

    public static SelfDialog showSelfDialog(SelfDialog selfDialog, Context mContext, String message, SelfDialog.onClickListener onClickListener) {

        if (selfDialog == null) {
            selfDialog = new SelfDialog(mContext, true);
        }
        selfDialog.setMessage(message);

        selfDialog.setOnclickListener("确定", "取消", onClickListener);

        selfDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        selfDialog.show();
        return selfDialog;
    }
}
