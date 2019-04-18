package com.qttx.toolslibrary.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qttx.toolslibrary.utils.GlideUtils;


/**
 * Created by tlh on 2016/9/12.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private Context mContext;//上下文对象

    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
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

    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, int color) {
        TextView view = findViewById(viewId);
        view.setTextColor(color);
        return this;
    }

    public RecyclerViewHolder setImageRes(int viewId, String url, int defalutimage) {
        ImageView view = findViewById(viewId);
        GlideUtils.loadImage(view, url, defalutimage);
        return this;
    }

    public RecyclerViewHolder setImageRes(int viewId, String url) {
        ImageView view = findViewById(viewId);
        GlideUtils.loadImage(view, url);
        return this;
    }

    public RecyclerViewHolder setImageRes(int viewId, @Nullable Integer resourceId) {
        ImageView view = findViewById(viewId);
        GlideUtils.loadImage(view, resourceId);
        return this;
    }

    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
