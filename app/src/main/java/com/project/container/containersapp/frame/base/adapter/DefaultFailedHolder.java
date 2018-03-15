package com.project.container.containersapp.frame.base.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.JZXBaseActivity;

import java.util.List;

/**
 * Created by shijia on 2016/9/23.
 */
public class DefaultFailedHolder extends BaseViewHold {

    public DefaultFailedHolder(final Context mContext, View itemView) {
        super(itemView);
        Button bt = (Button) itemView.findViewById(R.id.bt_refresh);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((JZXBaseActivity) mContext).reRequestData();
            }
        });

    }

    @Override
    public void onBindViewHolder(int position, List mData) {

    }
}
