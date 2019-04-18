package com.qttx.toolslibrary.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by huangyuru on 2016/9/12.
 */
public class ModuleViewHolder {
    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private Context mContext;//上下文对象
    private View itemView;

    public ModuleViewHolder(Context ctx, View itemView) {
        mContext = ctx;
        mViews = new SparseArray<View>();
        this.itemView = itemView;
    }

    public View getItemView() {
        return itemView;
    }

    public <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ModuleViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public ModuleViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public ModuleViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }




}
