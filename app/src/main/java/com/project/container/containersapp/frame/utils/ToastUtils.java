package com.project.container.containersapp.frame.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.container.containersapp.R;


public class ToastUtils {
    private static LayoutInflater mLayoutInflater;
    private static Toast mToast;

    private static LayoutInflater getInflater(Context context) {
        if (null == mLayoutInflater) {
            mLayoutInflater = LayoutInflater.from(context.getApplicationContext());
        }
        return mLayoutInflater;
    }

    private static Toast makeToast(Context context) {
        if (null != mToast) {
            mToast.cancel();
        }
        return mToast = new Toast(context.getApplicationContext());
    }

    private static View getView(Context context, String msg) {
        View view = getInflater(context).inflate(R.layout.toast, null);
        TextView tv_toast_content = (TextView) view.findViewById(R.id.tv_toast_content);
        tv_toast_content.setText(msg);
        return view;
    }


    private static View getSuccessView(Context context, String msg) {
        View view = getInflater(context).inflate(R.layout.toast_success, null);
        TextView tv_toast_content = (TextView) view.findViewById(R.id.tv_toast_content);
        tv_toast_content.setText(msg);
        return view;
    }

    private static View getFailView(Context context, String msg) {
        View view = getInflater(context).inflate(R.layout.toast_fail, null);
        TextView tv_toast_content = (TextView) view.findViewById(R.id.tv_toast_content);
        tv_toast_content.setText(msg);
        return view;
    }

    public static void showLongToast(Context context, String msg) {
        Toast toast = makeToast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(getView(context, msg));
        toast.show();
    }

    public static void showShortToast(Context context, String msg) {
        Toast toast = makeToast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(getView(context, msg));
        toast.show();
    }

    public static void showFailToast(Context context, String msg) {
        Toast toast = makeToast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(getFailView(context, msg));
        toast.show();
    }

    public static void showSuccessToast(Context context, String msg) {
        Toast toast = makeToast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(getSuccessView(context, msg));
        toast.show();
    }
}
