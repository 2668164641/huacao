package com.qttx.toolslibrary.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
/**
 * Created by huangyr
 * on 2017/11/22.
 * Glide图片加载工具类
 */

public class GlideUtils {
    /**
     * @param view
     * @param url
     * 正常加载
     */
    public static void loadImage(ImageView view, String url) {
        loadImage(view, url, com.qttx.basicres.R.drawable.default_image_360_360);
    }

    /**
     * @param view
     * @param url
     * @param defalutimage
     */
    public static void loadImage(ImageView view, String url, int defalutimage) {
        RequestOptions options = RequestOptions.placeholderOf(defalutimage);
        loadImage(view, url, options.error(defalutimage).centerCrop());
    }
    /**
     * @param view
     * @param url
     * 加载圆形图片
     */
    public static void loadCircleImage(ImageView view, String url) {
        loadCircleImage(view, url, com.qttx.basicres.R.drawable.default_image_360_360);
    }

    public static void loadCircleImage(ImageView view, String url, int defalutimage) {
        RequestOptions options = RequestOptions.placeholderOf(defalutimage);
        loadImage(view, url, options.error(defalutimage).circleCrop());
    }

    /**
     * 强制加载centerCrop圆角图片
     * @param view
     * @param url
     */
    public static void loadRountCenterImage(ImageView view, String url) {
        loadRountCenterImage(view, url, com.qttx.basicres.R.drawable.default_image_360_360, 5);
    }

    public static void loadRountCenterImage(ImageView view, int mRadius, String url) {
        loadRountCenterImage(view, url, com.qttx.basicres.R.drawable.default_image_360_360, mRadius);
    }

    public static void loadRountCenterImage(ImageView view, String url, int defalutimage) {
        loadRountCenterImage(view, url, defalutimage, 5);
    }

    public static void loadRountCenterImage(ImageView view, String url, int defalutimage, int mRadius) {
        RequestOptions options = RequestOptions.placeholderOf(defalutimage);
        loadImage(view, url, options.error(defalutimage).transforms(new CenterCrop(), new RoundedCorners(SizeUtils.dp2px(mRadius))));
    }

    /**
     * 正常加载centerCrop圆角图片
     * 会根据ImageView的scaleType变化
     * 不设置||centerInside==》原图比例的圆角图片
     * fitXY==>满view圆角图片
     * centerCrop==>失去圆角属性
     * @param view
     * @param url
     */
    public static void loadRounImage(ImageView view, String url) {
        loadRounImage(view, url, com.qttx.basicres.R.drawable.default_image_360_360, 5);
    }

    public static void loadRounImage(ImageView view, int mRadius, String url) {
        loadRounImage(view, url, com.qttx.basicres.R.drawable.default_image_360_360, mRadius);
    }

    public static void loadRounImage(ImageView view, String url, int defalutimage) {
        loadRounImage(view, url, defalutimage, 5);
    }

    public static void loadRounImage(ImageView view, String url, int defalutimage, int mRadius) {
        RequestOptions options = RequestOptions.placeholderOf(defalutimage);
        loadImage(view, url, options.error(defalutimage).transform(new RoundedCorners(SizeUtils.dp2px(mRadius))));
    }

    /**
     * @param view
     * @param url
     * @param options
     */
    public static void loadImage(ImageView view, String url, RequestOptions options) {
        if (view == null) {
            throw new IllegalArgumentException("GlideUtils Exception:You cannot start a load on a null  ImageView");
        }
        Context context = view.getContext();
        Glide.with(context).load(url).apply(options).into(view);
    }
    public static void loadImage(ImageView view, @Nullable Integer resourceId) {
        if (view == null) {
            throw new IllegalArgumentException("GlideUtils Exception:You cannot start a load on a null  ImageView");
        }
        Context context = view.getContext();
        Glide.with(context).load(resourceId).into(view);
    }


}
