package com.project.container.containersapp.business;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.boxing.BoxingListActivity;
import com.project.container.containersapp.business.checkbox.CheckBoxListActivilty;
import com.project.container.containersapp.business.hangingbox.DXBoxListActivity;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.block.LoginBlock;
import com.project.container.containersapp.frame.utils.DisplayUtil;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends JZXBaseActivity {


    @BindView(R.id.ll_content)
    LinearLayout mLlContent;

    private List<String> mRoles;

    private boolean isExit = false;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mRoles = LoginBlock.getRoles();
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        //设置沉浸式
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);

        setTitle("选择操作类型");
        getTitle();
        showOrHideBackButton(View.INVISIBLE);

        addViews();

    }


    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pressAgainExit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*
        * 根据角色添加按钮
        * */
    private void addViews() {
        int width = DisplayUtil.dip2px(mContext, 200);
        int height = DisplayUtil.dip2px(mContext, 50);
        int topMargin = DisplayUtil.dip2px(mContext, 25);

        for (int i = 0; i < mRoles.size(); i++) {
            LinearLayout layout = (LinearLayout) mLayoutInflater.inflate(R.layout.button_view, null);
            TextView textView = layout.findViewById(R.id.tv_title);
            textView.setText(mRoles.get(i));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = width;
            layoutParams.height = height;
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(0, topMargin, 0, 0);
            mLlContent.addView(layout, layoutParams);

            //设置点击事件
            final int index = i;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToActivity(index);
                }
            });
        }
    }

    /*跳转到对应的操作界面*/
    private void JumpToActivity(int index) {
//        ToastUtil.makeToast(mContext, mRoles.get(index));
        String role = mRoles.get(index);
        Intent intent = null;
        switch (role) {
            case "验箱":
                intent = new Intent(mContext, CheckBoxListActivilty.class);
                break;
            case "吊箱":
                intent = new Intent(mContext, DXBoxListActivity.class);

                break;
            case "装箱":

                break;
            case "检斤验货":

                break;
            case "配装":

                break;
            default:
                intent = new Intent(mContext, BoxingListActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    private void pressAgainExit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.makeToast(mContext, "再按一次退出程序");
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }
}
