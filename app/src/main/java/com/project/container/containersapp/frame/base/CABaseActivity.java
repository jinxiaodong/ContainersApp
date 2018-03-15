package com.project.container.containersapp.frame.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.utils.DialogUtils;
import com.project.container.containersapp.frame.utils.easypermissions.EasyPermissions;
import com.redstar.library.base.BaseActivity;

/**
 * Created by xiaodong.jin on 2018/3/14.
 * description：集装箱APP基类Activity：所有activity继承该类实现自有业务。
 */

public class CABaseActivity extends BaseActivity {

    /*TAG*/
    public final String TAG = getClass().getSimpleName();
    /**
     * activity
     */
    protected Context mContext;
    /**
     * LayoutInflater.
     */
    protected LayoutInflater mLayoutInflater;

    /**
     * 标题
     */
    private TextView mTvTitle;
    /**
     * 返回按钮布局
     */
    private ImageView imageBack;

    /*右边按钮*/
    private TextView rightTxt;

    /**
     * 页面header布局
     */
    private RelativeLayout mRelHeader;
    /**
     * 遮罩图层布局
     */
    private LinearLayout mLinWrapper;

    /**
     * 用于在异步请求中更新ui
     */
    private Handler mHandler;

    /*是否正在切换Fragment*/
    private boolean isSwitchFragmenting = false;
    /**
     * 处理框
     */
    private Dialog mWaittingDialog = null;

    /**
     * 无数据view
     */
    private View mNoDataView;

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.title_bar_native;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.base_common_activity_layout;
    }

    @Override
    protected void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            DialogUtils.dismissDiaog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
        }
        return false;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mContext = this;
        mLayoutInflater = LayoutInflater.from(mContext);
        try {
            if (getHeaderView() != null) {

                imageBack = (ImageView) findViewById(R.id.title_bar_back);
                rightTxt = (TextView) findViewById(R.id.title_bar_ok);
            }
            mLinWrapper = (LinearLayout) findViewById(R.id.lin_wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getHeaderView() != null) {
            mRelHeader = (RelativeLayout) findViewById(R.id.rel_header);
        }
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        try {
            if (getHeaderView() != null && imageBack != null) {
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        close();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);
    }


    /**
     * @param resLayId       resID
     * @param fragment       fragment
     * @param isAddBackStack 是否加入返回栈
     * @return
     * @description 替换Fragment (默认有动画效果)
     */
    protected void replaceFragment(int resLayId, Fragment fragment, boolean isAddBackStack) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.replace(resLayId, fragment);
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        isSwitchFragmenting = false;
    }

    /**
     * @param resLayId       resid
     * @param fragment       fragment
     * @param isAddBackStack 是否加入返回栈
     * @param isAnimation    切换动画
     * @return
     * @description 替换Fragment
     */
    protected void replaceFragment(int resLayId, Fragment fragment,
                                   boolean isAddBackStack, boolean isAnimation) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isAnimation) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
        fragmentTransaction.replace(resLayId, fragment);
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        isSwitchFragmenting = false;
    }

    /**
     * @param resLayId
     * @param showFragment
     * @param isAnimation
     * @param isAddBackStack
     * @param hideFragments  要隐藏的Fragment数组
     * @return
     * @description 添加Fragment
     */
    protected void addFragment(int resLayId, Fragment showFragment,
                               boolean isAnimation, boolean isAddBackStack,
                               Fragment... hideFragments) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (isAnimation) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
        if (hideFragments != null) {
            for (Fragment hideFragment : hideFragments) {
                if (hideFragment != null)
                    fragmentTransaction.hide(hideFragment);
            }
        }
        fragmentTransaction.add(resLayId, showFragment);
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        isSwitchFragmenting = false;
    }

    /**
     * @param showFragment   显示的fragment
     * @param hideFragments  要隐藏的Fragment数组
     * @param isAddBackStack 是否加入返回栈
     * @description 显示隐藏Fragment
     */
    protected void showHideFragment(Fragment showFragment, boolean isAnimation,
                                    boolean isAddBackStack, Fragment... hideFragments) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (isAnimation) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
        if (hideFragments != null) {
            for (Fragment hideFragment : hideFragments) {
                if (hideFragment != null && hideFragment.isAdded())
                    fragmentTransaction.hide(hideFragment);
            }
        }
        if (showFragment != null) {
            fragmentTransaction.show(showFragment);
        }
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        //#5266 nSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存完状态后
        //再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }


    /**
     * @param
     * @return
     * @description getHandler
     * @author SongZheng
     * @date 2016/3/7
     */
    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(mContext.getMainLooper());
        }
        return mHandler;
    }

    /**
     * @param
     * @return
     * @description 获得遮罩层布局
     */
    public LinearLayout getLinWrapper() {
        try {
            if (mLinWrapper == null) {
                mLinWrapper = new LinearLayout(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                mLinWrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                mLinWrapper.setOrientation(LinearLayout.VERTICAL);
                addContentView(mLinWrapper, lp);
            }
        } catch (Exception e) {
        }
        return mLinWrapper;
    }

    /**
     * @param
     * @return
     * @description 设置标题
     */
    public void setTitle(String title) {
        if (getHeaderView() == null) {
            return;
        }
        try {
            mTvTitle = (TextView) findViewById(R.id.title_bar_title_text);

            mTvTitle.setText(title);
            mRelHeader.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @return
     * @description 设置标题
     */
    public void setTitle(int strResId) {
        if (getHeaderView() == null) {
            return;
        }
        try {
            mTvTitle = (TextView) findViewById(R.id.title_bar_title_text);

            mTvTitle.setText(getResString(strResId));
            mTvTitle.setVisibility(View.VISIBLE);
            mRelHeader.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @return
     * @description 隐藏标题
     */
    public void hideTitle() {
        if (getHeaderView() == null) {
            return;
        }
        try {
            mTvTitle.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @return
     * @description 设置标题栏显示状态
     */
    public void shwoOrHideHeader(int viewState) {
        if (getHeaderView() == null) {
            return;
        }
        try {
            mRelHeader.setVisibility(viewState);
        } catch (Exception e) {
        }
    }

    /**
     * @param
     * @return
     * @description 设置返回按钮显示状态
     */
    public void showOrHideBackButton(int viewState) {
        if (getHeaderView() == null) {
            return;
        }
        try {
            if (imageBack == null) {
                imageBack = (ImageView) findViewById(R.id.title_bar_back);
            }
            imageBack.setVisibility(viewState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param
     * @return
     * @description getInflaterView
     */
    public final View getInflaterView(int resId) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(this);
        }
        return mLayoutInflater.inflate(resId, null, false);
    }


    /**
     * @param
     * @return
     * @description getResString
     */
    public String getResString(int res) {
        return getResources().getString(res) + "";
    }


    /**
     * 获取头布局
     */
    public View getRelHeader() {
        mRelHeader = findViewById(R.id.rel_header);
        return mRelHeader;
    }

    /**
     * 设置标题栏颜色
     */
    public void setHeaderColor(int colorId) {
        mRelHeader = (RelativeLayout) findViewById(R.id.rel_header);

        if (getHeaderView() == null) {
            return;
        }
        if (mRelHeader == null) {
            return;
        }
        mRelHeader.setBackgroundColor(getResources().getColor(colorId));
    }

    /**
     * 设置标题颜色
     */
    public void setTitleColor(int colorId) {
        if (getHeaderView() == null) {
            return;
        }
        try {
            mRelHeader = (RelativeLayout) findViewById(R.id.rel_header);
            mTvTitle = (TextView) findViewById(R.id.title_bar_title_text);

            mTvTitle.setTextColor(getResources().getColor(colorId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示默认等待框
     */
    public void showDialog() {
        showDialog("");
    }

    public void showDialog(final String msg) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!CABaseActivity.this.isFinishing()) {
                    DialogUtils.showDialog(mContext, true, msg);
                }
            }
        });
    }

    /**
     * 取消等待框
     */
    public void dismissDialog() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                DialogUtils.dismissDiaog();
            }
        });

    }


    public void showNoDataNoti(ViewGroup viewGroup, View view) {
        try {
            if (mNoDataView == null) {
                mNoDataView = view;
                mNoDataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //donothing
                    }
                });
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                viewGroup.addView(mNoDataView, lp);
            } else {
                if (mNoDataView == view) {
                    mNoDataView.setVisibility(View.VISIBLE);
                } else {
                    viewGroup.removeView(mNoDataView);
                    mNoDataView = view;
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    viewGroup.addView(mNoDataView, lp);
                    mNoDataView.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 显示无数据提示
     */
    public void showNoDataNoti(ViewGroup viewGroup, int layoutResId) {
        try {
            if (mNoDataView == null) {
                mNoDataView = mLayoutInflater.inflate(layoutResId, null);
                mNoDataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //donothing
                    }
                });
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                viewGroup.addView(mNoDataView, lp);
            } else {
                mNoDataView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏无数据提示
     */
    public void hideNoDataNoti() {
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.GONE);
        }
    }


    /**
     * 无网络刷新后，重新获得数据，供子类实现
     */
    public void reRequestData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
