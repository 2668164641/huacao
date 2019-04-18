package com.qttx.toolslibrary.widget;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.utils.GlideUtils;

/**
 * 自定义底部Bar
 */

public class NativeTabButton extends FrameLayout {
    private int mIndex;

    private ImageView mImage;
    private TextView mTitle;
    private TextView mNotify;
    private ImageView mImage_sel;

    private int mSelectedImg;
    private int mUnselectedImg;

    private String mSelectedImgUrl;
    private String mUnselectedImgUrl;

    private int selectColor;
    private int unselectColor;

    private OnTabClick click;
    private Context mContext;

    public NativeTabButton(Context context) {
        this(context, null);
    }

    public NativeTabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NativeTabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.mContext = context;
        OnClickListener clickListner = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null) {
                    click.showIndex(mIndex);
                }
            }
        };

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.native_tab_button, this, true);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.tab_btn_container);

        mImage = (ImageView) findViewById(R.id.tab_btn_default);
        mTitle = (TextView) findViewById(R.id.tab_btn_title);
        mNotify = (TextView) findViewById(R.id.tab_unread_notify);
        mImage_sel= (ImageView) findViewById(R.id.tab_btn_default_sel);

        container.setOnClickListener(clickListner);
    }

    public void setOnTabClick(OnTabClick click) {
        this.click = click;

    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public void setUnselectedImage(int img) {
        this.mUnselectedImg = img;
        mImage.setImageResource(mUnselectedImg);
    }

    public void setSelectedImage(int img) {
        this.mSelectedImg = img;
        mImage_sel.setImageResource(mSelectedImg);

    }
    public void setUnselectedImageUrl(String img) {
        if (!TextUtils.isEmpty(img)) {
            this.mUnselectedImgUrl = img;
            GlideUtils.loadImage(mImage, mUnselectedImgUrl);
        }
    }

    public void setSelectedImageUrl(String img) {
        if (!TextUtils.isEmpty(img))
        {
            this.mSelectedImgUrl = img;
            GlideUtils.loadImage(mImage_sel,mSelectedImgUrl);
        }

    }
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public void setUnselectColor(int unselectColor) {
        this.unselectColor = unselectColor;
    }

    /**
     * 设置字体颜色
     *
     * @param selected
     */
    private void setSelectedColor(Boolean selected) {
        if (selected) {
            mTitle.setTextColor(getResources().getColor(
                    selectColor));
        } else {
            mTitle.setTextColor(getResources().getColor(unselectColor));
        }
    }

    public void setSelectedButton(Boolean selected) {
        setSelectedColor(selected);
        if (selected) {
            mImage.setVisibility(GONE);
            mImage_sel.setVisibility(VISIBLE);

        } else {
            mImage.setVisibility(VISIBLE);
            mImage_sel.setVisibility(GONE);
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title))
        mTitle.setText(title);
    }

    public void setUnreadNotify(int unreadNum) {
        if (0 == unreadNum) {
            mNotify.setVisibility(View.INVISIBLE);
            return;
        }
        String notify;
        if (unreadNum > 99) {
            notify = "99+";
        } else {
            notify = Integer.toString(unreadNum);
        }

        mNotify.setText(notify);
        mNotify.setVisibility(View.VISIBLE);
    }

    public interface OnTabClick {
        void showIndex(int dex);
    }
}
