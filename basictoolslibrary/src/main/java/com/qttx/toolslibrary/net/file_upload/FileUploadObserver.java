package com.qttx.toolslibrary.net.file_upload;

/**
 * 上传文件的回调
 */

public abstract class FileUploadObserver<T>{



//    @Override
//    public void onNext(T t) {
//        onUpLoadSuccess(t);
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        onUpLoadFail(e);
//    }
//
    //可以重写，具体可由子类实现
    public void onComplete() {

    }

    public void onProgressChange(long bytesWritten, long contentLength) {
        onProgress((int) (bytesWritten*100 / contentLength));
    }

    //上传成功的回调
    public abstract void onUpLoadSuccess(T t);

    //上传你失败回调
    public abstract void onUpLoadFail(Throwable e);

    //上传进度回调
    public abstract void onProgress(int progress);

}
