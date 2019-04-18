package com.qttx.toolslibrary.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qttx.toolslibrary.net.ErrorMsgBean;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * @author huangyuru
 * @date 2017/10/21
 */

public interface Iview {

    /**
     * 通过dialog显示正在加载
     */
    void onLoadByDialog();

    /**
     * 通过界面View显示正在加载
     */
    void onLoadByView();

    /**
     * 隐藏加载界面，隐藏错误界面
     */
    void onSuccess();

    /**
     * 显示重新加载的错误界面，覆盖掉原先内容
     *
     * @return
     */
    void onReLoadErrorShow(ErrorMsgBean errorMsgBean);

    void showToast(String message);

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    @NonNull
    <T> LifecycleTransformer<T> bindLifecycle();

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    @NonNull
    <T> LifecycleTransformer<T> bindLifecycle(@NonNull ActivityEvent event);

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    @NonNull
    <T> LifecycleTransformer<T> bindLifecycle(@NonNull FragmentEvent event);

    @NonNull
    <T> LifecycleTransformer<T> bindLifecycle(@NonNull ActivityEvent activityEvent, @NonNull FragmentEvent event);

    Context getIviewContext();

}
