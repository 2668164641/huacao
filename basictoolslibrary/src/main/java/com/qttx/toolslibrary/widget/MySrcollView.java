package com.qttx.toolslibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 监听ScrollView滑动
 */
public class MySrcollView extends ScrollView {


    public MySrcollView(Context context) {
        super(context);
    }

    public MySrcollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySrcollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null)
            listener.onChanged(l, t, oldl, oldt);
    }

    public onChangeLister listener;

    public void setListener(onChangeLister listener) {
        this.listener = listener;
    }

    public interface onChangeLister {
        void onChanged(int l, int t, int oldl, int oldt);
    }


}
