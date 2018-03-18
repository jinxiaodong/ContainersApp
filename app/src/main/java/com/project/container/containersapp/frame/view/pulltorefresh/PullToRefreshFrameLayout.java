
package com.project.container.containersapp.frame.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @description 扩展第三方下拉刷新库
 */
public class PullToRefreshFrameLayout extends PtrFrameLayout {

    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/
    private JZXHerdview hxHerdview;
    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/
    public PullToRefreshFrameLayout(Context context) {
        super(context);
        init();
    }

    public PullToRefreshFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/
    private void init(){
        // the following are default settings
        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(500);
        // default is false
        setPullToRefresh(false);
        // default is true
        setKeepHeaderWhenRefresh(true);
        setLoadingMinTime(1500);

        //etHeaderView
        hxHerdview = new JZXHerdview(getContext());
        hxHerdview.setLayoutParams(new PullToRefreshFrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        hxHerdview.setUp(this);
        setHeaderView(hxHerdview);
        addPtrUIHandler(hxHerdview);

    }
    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
