package com.qttx.toolslibrary.net;

/**
 * Created by huang on 2017/10/26.
 *
 * 错误转换器
 */

public interface ErrorMsgConverter {

    ErrorMsgBean converterError(int errorCode, String errorMessage, boolean isServiceEror);

}
