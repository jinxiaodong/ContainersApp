package com.project.container.containersapp.frame.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.constants.DeviceInfo;
import com.project.container.containersapp.frame.view.LoadingDialog;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description：
 */

public class DialogUtils {
    private static Dialog mDialog;
    private static TextView loading_text;
    private static RelativeLayout dialog_background;

    private static Dialog createDialog(Context context, boolean cancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.dialog_dialog, null);// 得到加载view
        dialog_background = (RelativeLayout) v.findViewById(R.id.dialog_background);
        dialog_background.getBackground().setAlpha(204);//设置透明底 80%
        loading_text = (TextView) v.findViewById(R.id.loading_text);//文字
        RelativeLayout viewById = (RelativeLayout) v.findViewById(R.id.loading_dialog);//图片容器
        LoadingDialog dialogView = new LoadingDialog(context);//转圈动画
        int width = (int) ((DeviceInfo.WIDTHPIXELS) * 120.00 / 750.00);
        int height = width;
       /* int height = (int) (DeviceUtils.getWidth((Activity) context) * 200.00 / 720.00);
        int width = height * 18 / 11;*/
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new FrameLayout.LayoutParams(width, height));
        dialogView.setLayoutParams(layoutParams);
        viewById.addView(dialogView);
        Dialog loadingDialog = new Dialog(context, R.style.Dialog_NoTitle);// 创建自定义样式dialog
        loadingDialog.setCancelable(cancelable);// 可以用“返回键”取消
        loadingDialog.setContentView(v);// 设置布局

        //重置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.x = 0;
        lp.y = -120;
        window.setAttributes(lp);

        return loadingDialog;
    }


    public static void showDialog(Context context, boolean cancelable, String text) {
        if (context == null)
            return;
        try {
            dismissDiaog();
            mDialog = createDialog(context, cancelable);
            if (null != loading_text && !TextUtils.isEmpty(text)) {
                loading_text.setText(text);
            }
            mDialog.show();
        } catch (Exception e) {
        }
    }

    public static void showDialog(Context context, boolean cancelable) {
        showDialog(context, cancelable, null);
    }

    public static void showDialog(Context context) {
        showDialog(context, true);
    }

    public static void showDialog(Context context, String text) {
        showDialog(context, true, text);
    }


    public static void dismissDiaog() {
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {
            }
            mDialog = null;
        }

    }

    public static void setProgress(String text) {
        if (null != loading_text && TextUtils.isEmpty(text)) {
            loading_text.setSingleLine(false);
            loading_text.setText("正在下载" + '\n' + text);
        }
    }
}
