package com.qttx.toolslibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/29
 *     desc  : 吐司相关工具类
 * </pre>
 */
public final class ToastUtils {
    private static Toast toast;

    public static void showShort( String content) {
        if (null == toast) {
            toast = Toast.makeText(AppUtils.getApp(), "", Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public static void showShort( int content) {
        if (null == toast) {
            toast = Toast.makeText(AppUtils.getApp(), "", Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public static void makeTextLong( String content) {
        if (null == toast) {
            toast = Toast.makeText(AppUtils.getApp(), "", Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public static void makeTextLong( int content) {
        if (null == toast) {
            toast = Toast.makeText(AppUtils.getApp(), "", Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }
}
