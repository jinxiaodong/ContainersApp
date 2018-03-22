package com.project.container.containersapp.business.checkbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.model.PoSunTypeBean;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/19.
 * description：
 */

public class XSGridViewAdapter extends BaseAdapter {


    private Context mContext;
    private List<PoSunTypeBean> data;

    public XSGridViewAdapter(Context context, List<PoSunTypeBean> data) {
        this.data = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_xs, null);
        }
        CheckBox checkBox = convertView.findViewById(R.id.cb_xs);
        checkBox.setText(data.get(position).type);

        return convertView;
    }
}
