package com.project.container.containersapp.business.checkbox;

import android.content.Context;
import android.view.ViewGroup;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseRecyclerAdapter;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.CheckBoxListBean;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/16.
 * description：验箱列表适配器
 */

public class CheckBoxListAdapter extends BaseRecyclerAdapter<CheckBoxListBean> {




    public CheckBoxListAdapter(Context context, List<CheckBoxListBean> list) {
        super(context, list);
    }


    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHold hold = new CheckBoxListVH(mInflater.inflate(R.layout.check_box_item, parent, false));
        return hold;
    }

}
