package com.qttx.toolslibrary.net;


import com.qttx.toolslibrary.R;

/**
 * Created by huang on 2017/10/26.
 */

public class ErrorMsgBean {


    private String errorMsg;
    private int errorCode;
    /**
     * 显示的错误图片
     */
    private int imageRes = R.drawable.toolslib_no_network;
    /**
     * 是否显示重新加载按钮
     */
    private boolean canRetry = true;
    /**
     * 是否是服务器错误
     */
    private boolean isServiceError;
    /**
     * 是否需要特殊处理
     */
    private boolean isSpecial;

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public boolean isCanRetry() {
        return canRetry;
    }

    public void setCanRetry(boolean canRetry) {
        this.canRetry = canRetry;
    }

    public boolean isServiceError() {
        return isServiceError;
    }

    public void setServiceError(boolean serviceError) {
        isServiceError = serviceError;
    }


}
