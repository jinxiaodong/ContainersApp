package com.project.container.containersapp.business.checkbox;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.BaseRecyclerAdapter;
import com.project.container.containersapp.frame.base.adapter.BaseViewHold;
import com.project.container.containersapp.frame.model.JZXAutoCompleteBean;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/25.
 * description：
 */

public class DialogListAdapter extends BaseRecyclerAdapter<JZXAutoCompleteBean> {

    public DialogListAdapter(Context context, List<JZXAutoCompleteBean> list) {
        super(context, list);
    }

    @Override
    public BaseViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHold hold = new DialogListVH(mInflater.inflate(R.layout.dialog_jzxdm_item, parent, false));
        return hold;
    }

    class DialogListVH extends BaseViewHold<JZXAutoCompleteBean> {

        private TextView tv_jzxdm;

        public DialogListVH(View view) {
            super(view);
            tv_jzxdm = view.findViewById(R.id.tv_jzxdm);
        }

        @Override
        public void onBindViewHolder(int position, List<JZXAutoCompleteBean> mData) {
            JZXAutoCompleteBean jzxAutoCompleteBean = mData.get(position);
            if (jzxAutoCompleteBean == null) {
                return;
            }

            tv_jzxdm.setText(jzxAutoCompleteBean.value);
        }
    }
}
