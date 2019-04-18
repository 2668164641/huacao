package com.qttx.toolslibrary.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qttx.toolslibrary.library.refresh.PtrFrameLayout;


/**
 * Created by huang on 2017/7/18.
 */

public interface IListview<T extends RecyclerAdapter> extends Iview {


    @NonNull
    PtrFrameLayout getPtr();


    @NonNull
    RecyclerView getRecyclerView();

    @NonNull
    LinearLayoutManager getLayoutManager();

    @NonNull
    T getAdapter();
}
