package com.project.container.containersapp.business.hangingbox;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseRecyclerAdapter;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.DXBoxDetailsBean;
import com.project.container.containersapp.frame.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaodong.jin on 2018/3/22.
 * description：
 */

public class DxBoxDetailAdapter extends BaseRecyclerAdapter<DXBoxDetailsBean> {


    public DxBoxDetailAdapter(Context context, List<DXBoxDetailsBean> list) {
        super(context, list);
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHold hold = new DXBoxLDetailVH(mInflater.inflate(R.layout.dx_box_detail_item, parent, false));
        return hold;
    }

     class DXBoxLDetailVH extends BaseViewHold<DXBoxDetailsBean> {
        @BindView(R.id.top_divider)
        View mTopDivider;
        @BindView(R.id.tv_position)
        TextView mTvPosition;
        @BindView(R.id.tv_jzxdm)
        TextView mTvJzxdm;
        @BindView(R.id.tv_zygclxdm)
        TextView mTvZygclxdm;
        @BindView(R.id.tv_ywzlx)
        TextView mTvYwzlx;
        @BindView(R.id.tv_ywz)
        TextView mTvYwz;
        @BindView(R.id.tv_mbwzlx)
        TextView mTvMbwzlx;
        @BindView(R.id.tv_mbwz)
        TextView mTvMbwz;
        @BindView(R.id.btn_hang)
        TextView mBtnHang;
        @BindView(R.id.ll_line)
        LinearLayout mLlLine;

        public DXBoxLDetailVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(final int position, List<DXBoxDetailsBean> mData) {
            DXBoxDetailsBean dxBoxDetailsBean = mData.get(position);

            if (dxBoxDetailsBean == null) {
                return;
            }
            /*设置背景*/
            if (position % 2 == 0) {
                mLlLine.setBackgroundResource(R.color.white);
            } else {
                mLlLine.setBackgroundResource(R.color.blue_list_bg);
            }
            if (position == 0) {
                mTopDivider.setVisibility(View.VISIBLE);
            } else {
                mTopDivider.setVisibility(View.GONE);
            }

            mTvPosition.setText(position + 1+"");
            mTvJzxdm.setText(dxBoxDetailsBean.dxdjzxdm);
            if ("03".equals(dxBoxDetailsBean.dxdzygclxdm)) {
                mTvZygclxdm.setText("空箱");
            }
            if ("07".equals(dxBoxDetailsBean.dxdzygclxdm)) {
                mTvZygclxdm.setText("重箱");
            }
            mTvZygclxdm.setText("01空箱");

            //原位置
            mTvYwzlx.setText(dxBoxDetailsBean.dxdywzlx);
            mTvYwz.setText(dxBoxDetailsBean.dxdywz);
            //目标位置
            mTvMbwzlx.setText(dxBoxDetailsBean.dxdmbwzlx);
            mTvMbwz.setText(dxBoxDetailsBean.dxdmbwz);

            mBtnHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.makeToast(mContext,"完成第"+(position+1)+"步");
                }
            });
        }
    }
}
