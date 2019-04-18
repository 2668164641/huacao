package com.qttx.toolslibrary.base;

/**
 * Created by huang on 2017/7/21.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.widget.MyLinearLayout;


/**
 * Created by AmatorLee on 2017/7/12.
 */

public class StatusViewManager {

    private LayoutInflater mInflater;
    /**
     * 加载view
     */
    private View mLoadView;
    /**
     * 实际view的父view
     */
    private ViewGroup mContentView;
    /**
     * 实际view
     */
    private View realView;
    /**
     * 无网络链接时得view
     */
    private View mNoNetView;

    private ImageView net_iv;
    private TextView net_tv;
    private TextView re_load;

    private RelativeLayout.LayoutParams mParams;
    /**
     * 保存状态view的container
     */
    private RelativeLayout mStatusContainer;

    private Context mContext;
    /**
     * 避免重复添加
     */
    private boolean isAddLoad, isAddNoNet;
    /**
     * 可见状态
     */
    public static final int V = View.VISIBLE;
    /***
     * 不可见状态
     */
    public static final int G = View.GONE;
    /**
     * 重新加载接口
     */
    private onRetryClick mOnRetryClick;
    /**
     * 切换到主线程改变view的状态
     */
    private Handler mMainThreadHandler;

    private int no_net_layout_id = -1;
    private int loading_layout_id = -1;

    /**
     * 设置的加载和空数据界面距离头部的距离
     */
    private int magTop=0;

    public static final String NONET = "nonet";
    public static final String LOAD = "load";


    public void setOnRetryClick(onRetryClick onRetryClick) {
        mOnRetryClick = onRetryClick;
    }

    private StatusViewManager(Context context, View view, int loading_layout_id, int no_net_layout_id,int magtop) {
        super();
        this.loading_layout_id = loading_layout_id;
        this.no_net_layout_id = no_net_layout_id;
        realView = view;
        this.magTop=magtop;
        mContentView = (ViewGroup) view.getParent();
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        initView();
        initContainer();
    }

    public static StatusViewManager createView(Context context, View view,int magtop) {
        return new StatusViewManager(context, view, -1, -1,magtop);
    }

    public static StatusViewManager createView(Context context, View view, int loading_layout_id, int no_net_layout_id,int magtop) {
        return new StatusViewManager(context, view, loading_layout_id, no_net_layout_id,magtop);
    }


    public void initView() {
        if (loading_layout_id == -1) {
            loading_layout_id = R.layout.view_loading_layout;
        }
        if (no_net_layout_id == -1) {
            no_net_layout_id = R.layout.no_network_layout;
        }
        try {
            mLoadView = mInflater.inflate(loading_layout_id, null);
            mLoadView.setTag(LOAD);
            MyLinearLayout loadingView = (MyLinearLayout) mLoadView.findViewById(R.id.loadingView);
            if (magTop>0) {
                LinearLayout.LayoutParams loading = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                loading.setMargins(0, magTop, 0, 0);
                loadingView.setLayoutParams(loading);
            }
            mNoNetView = mInflater.inflate(no_net_layout_id, null);
            MyLinearLayout errViewView = (MyLinearLayout) mNoNetView.findViewById(R.id.errViewView);
            if (magTop>0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, magTop, 0, 0);
                errViewView.setLayoutParams(lp);
            }
            net_iv = (ImageView) mNoNetView.findViewById(R.id.net_iv);
            net_tv = (TextView) mNoNetView.findViewById(R.id.net_tv);
            re_load = (TextView) mNoNetView.findViewById(R.id.re_load);
            re_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRetryClick != null) {
                        mOnRetryClick.onRetryLoad();
                    }
                }
            });
            mNoNetView.setTag(NONET);
        } finally {
            mInflater = null;
        }
    }

    public void initContainer() {
        mStatusContainer = new RelativeLayout(mContext);
        mStatusContainer.setLayoutParams(mParams);
        mContentView.addView(mStatusContainer);
    }

    public void onLoad() {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoadView != null && !isAddLoad) {
                    isAddLoad = true;
                    mStatusContainer.addView(mLoadView, mParams);
                }
                show(STATUS.LOADING);
            }
        });
    }

    private STATUS status;

    private void show(STATUS result) {
        if (status == result) {
            return;
        }
        status = result;
        switch (result) {
            case SUCCESS:
                changeVisiable(V, G, G);
                break;
            case LOADING:
                changeVisiable(G, V, G);
                break;
            case NONET:
                changeVisiable(G, G, V);
                break;
            default:
        }
    }

    private void changeVisiable(final int contentStatus, final int loadStatus, final int nonetStatus) {

        if (mLoadView != null) {
            mLoadView.setVisibility(loadStatus);
        }
        if (mNoNetView != null) {
            mNoNetView.setVisibility(nonetStatus);
        }
        if (realView != null & realView.getVisibility() == View.GONE) {
            realView.setVisibility(View.VISIBLE);
        }
    }

    public void onSuccess() {
        show(STATUS.SUCCESS);
    }


    public void onNoNet(final String text, final int id, onRetryClick click) {
        if (net_tv != null) {
            net_tv.setText(text);
        }
        if (net_iv != null) {
            net_iv.setImageResource(id);
        }
        if (re_load != null) {
            mOnRetryClick = click;
            if (mOnRetryClick != null) {
                re_load.setVisibility(View.VISIBLE);
            } else {
                re_load.setVisibility(View.GONE);
            }
        }
        if (!isAddNoNet && mNoNetView != null) {
            mStatusContainer.addView(mNoNetView, mParams);
            isAddNoNet = true;
        }
        show(STATUS.NONET);
    }


    public enum STATUS {
        LOADING,
        SUCCESS,
        NONET
    }

    public void onDestory() {
        isAddNoNet = false;
        isAddLoad = false;
        mContext = null;
        if (mLoadView != null) {
            mLoadView = null;
        }
        if (mNoNetView != null) {
            mNoNetView = null;
        }
        if (mParams != null) {
            mParams = null;
        }
        for (int i = 0; i < mStatusContainer.getChildCount(); i++) {
            mStatusContainer.removeViewAt(i);
        }
        mStatusContainer = null;
    }

    public interface onRetryClick {
        void onRetryLoad();
    }

}

