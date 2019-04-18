package com.qttx.toolslibrary.net;


import com.qttx.toolslibrary.base.Iview;
import com.qttx.toolslibrary.net.basbean.BaseResultBean;
import com.qttx.toolslibrary.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by huang on 2017/7/18.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    public static ErrorMsgConverter errorMsgConverter;

    public static void initErrorMsgConverter(ErrorMsgConverter converter) {
        errorMsgConverter = null;
        errorMsgConverter = converter;
    }
    /**
     * 请求接口时的界面加载状态,，默认用dialog方式加载
     * <p>
     * LOADING_TYPE.DIALOG==》dialog样式弹窗加载
     * LOADING_TYPE.VIEW==》View内置界面加载
     */
    private LOADING_TYPE Loading_Type = LOADING_TYPE.DIALOG_LOADING;

    /**
     * 默认Toast提示错误信息
     * HAVE_ERRORVIEW==》错误界面显示错误信息
     */
    private ERROR_TYPE Error_Type = ERROR_TYPE.TOAST_ERROR;

    public enum LOADING_TYPE {
        VIEW_LOADING,
        DIALOG_LOADING
    }

    public enum ERROR_TYPE {
        TOAST_ERROR,
        VIEW_ERROR
    }

    private Iview iview;

    public BaseObserver() {

    }

    public BaseObserver(Iview iview) {
        this(iview, LOADING_TYPE.DIALOG_LOADING, ERROR_TYPE.TOAST_ERROR);
    }

    public BaseObserver(Iview iview, LOADING_TYPE loading_type) {
        this(iview, loading_type, ERROR_TYPE.TOAST_ERROR);
    }

    public BaseObserver(Iview iview, ERROR_TYPE error_type) {
        this(iview, LOADING_TYPE.DIALOG_LOADING, error_type);
    }

    public BaseObserver(Iview iview, LOADING_TYPE loading_type, ERROR_TYPE error_type) {
        this.iview = iview;
        this.Loading_Type = loading_type;
        this.Error_Type = error_type;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (iview != null) {
            //分两种，dialog加载,界面加载
            if (Loading_Type == LOADING_TYPE.DIALOG_LOADING) {
                iview.onLoadByDialog();
            } else {
                iview.onLoadByView();
            }
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        onResult(t);
    }
    public abstract void onResult(@NonNull T result);
    @Override
    public void onError(@NonNull Throwable e) {
        ExceptionHandle.ResponeThrowable exception = ExceptionHandle.handleException(e);
        ErrorMsgBean errorMsgBean = null;
        if (errorMsgConverter != null) {
            errorMsgBean = errorMsgConverter.converterError(exception.code, exception.message, e instanceof ExceptionHandle.ServerException);
        } else {
            errorMsgBean = new ErrorMsgBean();
            errorMsgBean.setErrorCode(exception.code);
            errorMsgBean.setErrorMsg(exception.message);
        }

        /**
         * 如果标注了该请求要显示错误界面，并且错误值没有被特殊处理，则操作错误界面
         */
        if (Error_Type == ERROR_TYPE.VIEW_ERROR && !errorMsgBean.isSpecial()) {
            iview.onReLoadErrorShow(errorMsgBean);
        } else {
            ToastUtils.showShort(exception.message);
            if (iview != null) {
                iview.onSuccess();
            }
        }
    }

    @Override
    public void onComplete() {
        if (iview != null) {
            iview.onSuccess();
        }
    }
}
