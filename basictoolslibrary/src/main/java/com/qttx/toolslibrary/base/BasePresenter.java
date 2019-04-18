package com.qttx.toolslibrary.base;


import com.qttx.toolslibrary.utils.ToastUtils;

/**
 * Created by Administrator on 2017/3/3 0003.
 * T  对应着Activity 的UI抽象接口  视图
 */

public class BasePresenter<T extends Iview> {

    public T mView;

    public void attachView(T view) {
        this.mView = view;
    }

    public void detachView() {
        mView = null;
    }

    public void showToast(String text) {
        ToastUtils.showShort(text);
    }
}
