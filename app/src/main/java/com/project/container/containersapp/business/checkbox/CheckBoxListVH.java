package com.project.container.containersapp.business.checkbox;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.CheckBoxListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaodong.jin on 2018/3/18.
 * description：
 */

public class CheckBoxListVH extends BaseViewHold<CheckBoxListBean> {
    @BindView(R.id.top_divider)
    View mTopDivider;
    @BindView(R.id.tv_yundan)
    TextView mTvYundan;
    @BindView(R.id.tv_zyjzxdm)
    TextView mTvZyjzxdm;
    @BindView(R.id.xqmc)
    TextView mXqmc;
    @BindView(R.id.xwdm)
    TextView mXwdm;
    @BindView(R.id.btn_check)
    TextView mBtnCheck;
    @BindView(R.id.ll_line)
    LinearLayout mLlLine;

    public CheckBoxListVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    @Override
    public void onBindViewHolder(int position, List<CheckBoxListBean> mData) {

        final CheckBoxListBean checkBoxListBean = mData.get(position);

        if (checkBoxListBean == null) {
            return;
        }

        mTvYundan.setText(checkBoxListBean.zyydaid);
        mTvZyjzxdm.setText(checkBoxListBean.zyjzxdm);
        mXqmc.setText(checkBoxListBean.xqmc);
        mXwdm.setText(checkBoxListBean.xwdm);

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

        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CheckBoxDetailActivity.class);
                intent.putExtra(CheckBoxDetailActivity.ZYDM, checkBoxListBean.zydm);
                intent.putExtra(CheckBoxDetailActivity.ZYJZXDM, checkBoxListBean.zyjzxdm);
                mContext.startActivity(intent);
            }
        });

    }
}
