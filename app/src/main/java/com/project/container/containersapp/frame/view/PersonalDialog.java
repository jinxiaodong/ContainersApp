package com.project.container.containersapp.frame.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.BaseDialog;

/**
 * Created by xiaodong.jin on 2018/3/30.
 * description：
 */

public class PersonalDialog extends BaseDialog {


    private LinearLayout ll_exit;
    private LinearLayout ll_login_out;

    private OnItemClickListener mOnItemClickListener;
    private TextView txt_cancel;

    public PersonalDialog(Context context, int gravity, boolean horizontalFullScreen, OnItemClickListener mOnItemClickListener) {
        super(context, gravity, horizontalFullScreen);
        this.mOnItemClickListener = mOnItemClickListener;

        initValue();
        initWidget();
        initListener();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_personal;
    }

    @Override
    protected void initValue() {
        super.initValue();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        ll_exit = findViewById(R.id.ll_exit);
        ll_login_out = findViewById(R.id.ll_login_out);
        txt_cancel = findViewById(R.id.txt_cancel);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ll_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onExitClick();
            }
        });

        ll_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onLoginOutClick();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public interface OnItemClickListener {
        void onExitClick();

        void onLoginOutClick();
    }
}
