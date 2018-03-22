package com.project.container.containersapp.business.hangingbox;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseRecyclerAdapter;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.DXBoxListBean;
import com.project.container.containersapp.frame.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaodong.jin on 2018/3/20.
 * description：
 */

public class HangingBoxListAdapter extends BaseRecyclerAdapter<DXBoxListBean> {


    public HangingBoxListAdapter(Context context, List<DXBoxListBean> list) {
        super(context, list);
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHold hold = new HangingBoxListVH(mInflater.inflate(R.layout.hanging_box_item, parent, false));
        return hold;
    }

    class HangingBoxListVH extends BaseViewHold<DXBoxListBean> {


        @BindView(R.id.top_divider)
        View mTopDivider;       //顶部分割线
        @BindView(R.id.tv_yundan)
        TextView mTvYundan;     //运单号
        @BindView(R.id.tv_zyjzxdm)
        TextView mTvZyjzxdm;    //集装箱代码（箱号）
        @BindView(R.id.ydaqy)
        TextView mYdaqy;        //所属企业
        @BindView(R.id.ydatyr)
        TextView mYdatyr;       //托运人
        @BindView(R.id.btn_hang)
        TextView mBtnHang;      //吊箱按钮
        @BindView(R.id.ll_line)
        LinearLayout mLlLine;

        public HangingBoxListVH(View itemview) {
            super(itemview);
            ButterKnife.bind(this, itemview);
        }

        @Override
        public void onBindViewHolder(int position, List<DXBoxListBean> mData) {
            DXBoxListBean dxBoxListBean = mData.get(position);
            if (dxBoxListBean == null) {
                return;
            }
            //运单号
            mTvYundan.setText(dxBoxListBean.zyydaid);
            //集装箱代码（箱号）
            mTvZyjzxdm.setText(dxBoxListBean.zyjzxdm);
            //所属企业
            mYdaqy.setText(dxBoxListBean.ydaqy);
            //托运人
            mYdatyr.setText(dxBoxListBean.ydatyr);

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

            mBtnHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.makeToast(mContext, "跳转到吊箱列表");
                }
            });
        }
    }
}
