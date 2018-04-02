package com.project.container.containersapp.frame.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.checkbox.DialogListAdapter;
import com.project.container.containersapp.frame.base.BaseDialog;
import com.project.container.containersapp.frame.base.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.project.container.containersapp.frame.constants.DeviceInfo;
import com.project.container.containersapp.frame.model.JZXAutoCompleteBean;
import com.project.container.containersapp.frame.presenter.IListView;
import com.project.container.containersapp.frame.presenter.checkbox.AutoCompletePresenter;
import com.project.container.containersapp.frame.utils.DisplayUtil;
import com.project.container.containersapp.frame.view.recycleview.LoadMoreRecyclerView;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/25.
 * description：
 */
public class NewJzxdmListDialog extends BaseDialog {

    private LoadMoreRecyclerView loadMoreRecyclerView;
    private LinearLayout ll_dialog;


    private OnJzxdmItemClickListener mJzxdmItemClickListener;
    private AutoCompletePresenter mPresenter;
    private String mJzxdmKeys = "";
    private DialogListAdapter mAdapter;

    public NewJzxdmListDialog(Context context, int gravity, boolean horizontalFullScreen, String keys, OnJzxdmItemClickListener mJzxdmItemClickListener) {
        super(context, gravity, horizontalFullScreen);
        this.mJzxdmItemClickListener = mJzxdmItemClickListener;
        mJzxdmKeys = keys;

        initValue();
        initWidget();
        initListener();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_jzxdm_filter;
    }

    @Override
    protected void initValue() {
        super.initValue();

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        int h = (int) (DeviceInfo.HEIGHTPIXELS - DisplayUtil.dip2px(context, 24)) / 2;
        ll_dialog = (LinearLayout) findViewById(R.id.ll_dialog);
        if (null != ll_dialog.getLayoutParams()) {
            ll_dialog.getLayoutParams().height = h;
        }

        loadMoreRecyclerView = findViewById(R.id.loadMoreRecyclerView);
        loadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new DialogListAdapter(context, null);
        loadMoreRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mPresenter = new AutoCompletePresenter(context, new IListView<JZXAutoCompleteBean>() {
            @Override
            public void onSuccess(List<JZXAutoCompleteBean> data) {
                mAdapter.getData().clear();
                mAdapter.getData().addAll(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onError(String code, String msg) {

            }
        });

        loadMoreRecyclerView.setOnItemClickListener(new HeaderAndFooterRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                JZXAutoCompleteBean jzxAutoCompleteBean = mAdapter.getData().get(position);
                if (jzxAutoCompleteBean != null) {
                    mJzxdmItemClickListener.onItemClick(jzxAutoCompleteBean);
                }
                dismiss();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getAutoList(mJzxdmKeys);
    }

    public interface OnJzxdmItemClickListener {
        void onItemClick(JZXAutoCompleteBean jzxAutoCompleteBean);
    }
}
