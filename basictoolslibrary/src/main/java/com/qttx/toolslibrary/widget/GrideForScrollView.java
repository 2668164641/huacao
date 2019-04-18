package com.qttx.toolslibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * ScrollView 嵌套GrideView，可设置空白区域点击
 */
public class GrideForScrollView extends GridView {

    public boolean isOnMeasure;

    public GrideForScrollView(Context context) {
        super(context);
    }

    public GrideForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrideForScrollView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private float mTouchX;
    private float mTouchY;
    private OnTouchBlankPositionListener mTouchBlankPosListener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchBlankPosListener != null) {
            if (!isEnabled()) {
                return isClickable() || isLongClickable();
            }
            int action = event.getActionMasked();
            float x = event.getX();
            float y = event.getY();
            final int motionPosition = pointToPosition((int) x, (int) y);
            if (motionPosition == INVALID_POSITION) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchX = x;
                        mTouchY = y;
//                        mTouchBlankPosListener.onTouchBlank(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(mTouchX - x) > 10
                                || Math.abs(mTouchY - y) > 10) {
//                            mTouchBlankPosListener.onTouchBlank(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mTouchX = 0;
                        mTouchY = 0;
                        mTouchBlankPosListener.onTouchBlank(event);
                        break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置GridView的空白区域的触摸事件
     *
     * @param listener
     */
    public void setOnTouchBlankPositionListener(
            OnTouchBlankPositionListener listener) {
        mTouchBlankPosListener = listener;
    }

    public interface OnTouchBlankPositionListener {
        void onTouchBlank(MotionEvent event);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

}
