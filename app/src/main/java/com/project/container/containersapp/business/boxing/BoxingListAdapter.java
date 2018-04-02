package com.project.container.containersapp.business.boxing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseRecyclerAdapter;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.BoxingListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaodong.jin on 2018/3/25.
 * description：
 */

class BoxingListAdapter extends BaseRecyclerAdapter<BoxingListBean> {


    private onFinishItemClickListener mOnFinishItemClickListener;

    public BoxingListAdapter(Context context, List<BoxingListBean> list) {
        super(context, list);
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHold hold = new BoxingListVH(mInflater.inflate(R.layout.boxing_item, parent, false));
        return hold;
    }

    class BoxingListVH extends BaseViewHold<BoxingListBean> {
        @BindView(R.id.top_divider)
        View mTopDivider;
        @BindView(R.id.tv_yundan)
        TextView mTvYundan;
        @BindView(R.id.tv_zyjzxdm)
        TextView mTvZyjzxdm;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.btn_finish)
        TextView mBtnFinish;
        @BindView(R.id.ll_line)
        LinearLayout mLlLine;

        public BoxingListVH(View itemview) {
            super(itemview);
            ButterKnife.bind(this, itemview);
        }

        @Override
        public void onBindViewHolder(final int position, List<BoxingListBean> mData) {
            BoxingListBean boxingListBean = mData.get(position);

            if (boxingListBean == null) {
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
            mTvGoodsName.setText(boxingListBean.zyhwmc);
            //集装箱代码
            mTvZyjzxdm.setText(boxingListBean.zyjzxdm);
            //运单号
            mTvYundan.setText(boxingListBean.zyydaid);

            mBtnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFinishItemClickListener.onItemClick(position);
                }
            });
        }
    }


    public void setOnFinishItemClickListener(onFinishItemClickListener listener) {
        mOnFinishItemClickListener = listener;
    }

    public interface onFinishItemClickListener {
        void onItemClick(int position);
    }
}
