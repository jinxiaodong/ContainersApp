package com.project.container.containersapp.business.checkgoods;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseRecyclerAdapter;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.CheckBoxListBean;
import com.project.container.containersapp.frame.model.CheckGoodsListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaodong.jin on 2018/3/26.
 * description：
 */

class CheckGoodsListAdapter extends BaseRecyclerAdapter<CheckBoxListBean> {


    public CheckGoodsListAdapter(Context context, List<CheckBoxListBean> list) {
        super(context, list);
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHold hold = new CheckGoodsListVH(mInflater.inflate(R.layout.check_goods_item, parent, false));
        return hold;
    }

    class CheckGoodsListVH extends BaseViewHold<CheckGoodsListBean> {
        @BindView(R.id.top_divider)
        View mTopDivider;
        @BindView(R.id.tv_yundan)
        TextView mTvYundan;
        @BindView(R.id.tv_zyjzxdm)
        TextView mTvZyjzxdm;
        @BindView(R.id.tv_xiangqu)
        TextView mTvXiangqu;
        @BindView(R.id.tv_xiangwei)
        TextView mTvXiangwei;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.btn_finish)
        TextView mBtnFinish;
        @BindView(R.id.ll_line)
        LinearLayout mLlLine;

        public CheckGoodsListVH(View itemview) {
            super(itemview);
            ButterKnife.bind(this, itemview);
        }

        @Override
        public void onBindViewHolder(int position, List<CheckGoodsListBean> mData) {

            CheckGoodsListBean checkGoodsListBean = mData.get(position);
            if (checkGoodsListBean == null) {
                return;
            }

            /*设置背景*/
            if (position % 2 == 0) {
                mLlLine.setBackgroundResource(R.color.blue_list_bg);
            } else {
                mLlLine.setBackgroundResource(R.color.white);
            }
            if (position == 0) {
                mTopDivider.setVisibility(View.VISIBLE);
            } else {
                mTopDivider.setVisibility(View.GONE);
            }

            //货物名称
            mTvGoodsName.setText("石灰粉");
            //集装箱代码
            mTvZyjzxdm.setText("TBJU10010022");
            //运单号
            mTvYundan.setText("yda0002");
            mTvXiangqu.setText("股6西内");
            mTvXiangwei.setText("070252");
        }
    }
}
