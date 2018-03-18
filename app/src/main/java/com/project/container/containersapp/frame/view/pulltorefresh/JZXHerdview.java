package com.project.container.containersapp.frame.view.pulltorefresh;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class JZXHerdview extends FrameLayout implements PtrUIHandler {

    private TextView mMsgTv;//状态
    private ImageView mImgView;//图标

    private int mTotalDragDistance;//rootView高度
    private int mBallbackDistance;//回弹距离
    private AnimatorSet animatorSet;
    private JZXTensionIndicator mIndicator;
    private PtrFrameLayout mPtrFrameLayout;

    /**旋转时长*/
    private int mRotateAniTime = 1000;
    /**回弹时长*/
    private int mBallbackTime = 400;
    private Context mContext;
    /**下拉是否执行刷新*/
    private boolean isPerformRefresh;

    /**图片高度*/
    private int imageHeight;
    /**root view*/
    private LinearLayout rootView;
//    private int pt;

    public JZXHerdview(Context context) {
        super(context);
        initView(null);
    }

    public JZXHerdview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public JZXHerdview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /***
     * 初始化view
     *
     * @param attrs
     */
    private void initView(AttributeSet attrs) {
        mContext = getContext();

        TypedArray arr = getContext().obtainStyledAttributes(attrs, in.srain.cube.views.ptr.R.styleable.PtrClassicHeader, 0, 0);
        if (arr != null) {
            mRotateAniTime = arr.getInt(in.srain.cube.views.ptr.R.styleable.PtrClassicHeader_ptr_rotate_ani_time, mRotateAniTime);
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pull_to_refresh_header_view, this);
        rootView = (LinearLayout) view.findViewById(R.id.root_view);
        mMsgTv = (TextView) view.findViewById(R.id.msg);
        mImgView = (ImageView) view.findViewById(R.id.img);

        PtrLocalDisplay.init(mContext);
        mTotalDragDistance = getResources().getDimensionPixelOffset(R.dimen.padding_80dp);
        mBallbackDistance = PtrLocalDisplay.dp2px(8);

        buildAnimation();
        resetView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mTotalDragDistance + getResources().getDimensionPixelOffset(R.dimen.padding_23dp);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingBottom() + getPaddingTop(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mIndicator = new JZXTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mIndicator);
    }

    /***
     * 重置view状态
     */
    private void resetView() {
        if (animatorSet.isStarted()) {
            animatorSet.end();
        }
    }

    /***
     * 创建动画
     */
    private void buildAnimation() {
        animatorSet = new AnimatorSet();
        //旋转动画
        ObjectAnimator mRotateAnimation = ObjectAnimator.ofFloat(mImgView, "rotationY", 0, 180, 360);
        mRotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mRotateAnimation.setDuration(mRotateAniTime);
        mRotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnimation.setRepeatMode(ValueAnimator.RESTART);

        //位移动画
        ObjectAnimator mTranslation = ObjectAnimator.ofFloat(mImgView, "translationY", 0 , -mBallbackDistance, 0);
        mTranslation.setInterpolator(new AccelerateDecelerateInterpolator());
        mTranslation.setDuration(mBallbackTime);

        animatorSet.play(mTranslation).before(mRotateAnimation);
    }

    /***
     * 设置旋转动画时长 ms
     *
     * @param time
     */
    public void setRotateAniTime(int time) {
        if (time == mRotateAniTime || time <= 0)
            return;
        this.mRotateAniTime = time;
    }

    /***
     * 设置回弹时长 ms
     * @param time
     */
    public void setBallbackTime(int time){
        if (time == mBallbackTime || time <= 0)
            return;
        this.mBallbackTime = time;
    }

    /***
     * 重置UI
     *
     * @param frame
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
    }

    /***
     * 刷新前准备
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mMsgTv.setText(mContext.getString(R.string.pull_refresh));
        isPerformRefresh = true;
    }

    /***
     * 开始刷新
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mMsgTv.setText(mContext.getString(R.string.refreshing));
        animatorSet.start();
    }

    /***
     * 完成刷新
     *
     * @param frame
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mMsgTv.setText(mContext.getString(R.string.refresh_complete));
        isPerformRefresh = false;
        resetView();
    }

    /***
     * 位置改变
     *
     * @param frame
     * @param isUnderTouch
     * @param status
     * @param ptrIndicator
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        //开始刷新的偏移量
        int offsetToRefresh = frame.getOffsetToRefresh();
        //Y轴的偏移量
        int currentPosY = ptrIndicator.getCurrentPosY();
        //Y轴上个拉伸位置
        int lastPosY = ptrIndicator.getLastPosY();

        int i = mTotalDragDistance - currentPosY;
        if (i >= 0){
            rootView.scrollTo(0, -i);
        } else {
            //位置复原，否则在快速滑动的时候会有bug
            rootView.scrollTo(0, 0);
        }

        //当前Y轴位置小于刷新点，并且刷新点高度小于等于最大可拉伸长度
        if (currentPosY < offsetToRefresh && lastPosY >= offsetToRefresh) {
            //下拉刷新
            //如果用户手指在拉伸并且处于准备刷新状态
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                mMsgTv.setText(mContext.getString(R.string.pull_refresh));
            }
        } else if (currentPosY > offsetToRefresh && lastPosY <= offsetToRefresh) {
            //释放刷新
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                mMsgTv.setText(mContext.getString(R.string.release_to_refresh));
            }
        }


        //当拉伸距离大于rootView高度，继续拉伸--->图片拉伸
        if (currentPosY >= offsetToRefresh){
            //改变imageview的纵向高度
            int offset = currentPosY - offsetToRefresh;
            ViewGroup.LayoutParams layoutParams = mImgView.getLayoutParams();
            int height = layoutParams.height;
            if (imageHeight == 0){
                imageHeight = layoutParams.height;
            }

            if ((!mPtrFrameLayout.isRefreshing() && isPerformRefresh) || imageHeight < height){
                layoutParams.height = imageHeight + offset;
                mImgView.requestLayout();
            }
        }
    }
}
