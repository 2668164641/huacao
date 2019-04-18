package com.qttx.toolslibrary.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RecyView 公共适配器
 * Created by huangyuru on 2016/9/12.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {


    protected List<T> mItems;
    protected LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnErrorItemClickListener errorClickListener;
    private OnItemLongClickListener mLongClickListener;

    public static final int HEADER_VIEW = 0x00000111;

    public static final int FOOTER_VIEW = 0x00000222;

    public static final int EMPTY_VIEW = 0x00000333;
    public static final int ERROR_VIEW = 0x00000444;
    public static final int TITLE_VIEW = 0x00000555;

    private int errorViewType;//无数据时的界面显示0:不启用空数据界面.1空数据,2,错误数据
    private int headerViewCount = 0;

    private View emptyView;
    private View errorView;
    private View footerView;
    private View headerView;


    public RecyclerAdapter(List<T> list) {
        mItems = (list != null) ? list : new ArrayList<T>();
    }

    public RecyclerAdapter(ArrayList<T> list) {
        mItems = (list != null) ? list : new ArrayList<T>();
    }

    public boolean isPinnedPosition(int position) {
        return getItemViewType(position) == TITLE_VIEW;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final RecyclerViewHolder holder;
        switch (viewType) {
            case HEADER_VIEW:
                holder = new RecyclerViewHolder(context, headerView);
                break;
            case FOOTER_VIEW:
                holder = new RecyclerViewHolder(context, footerView);
                break;
            case ERROR_VIEW:
                if (errorView == null) {
                    errorView = setDefaultError(parent);
                    errorView.findViewById(R.id.re_load).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (errorClickListener != null) {
                                errorClickListener.onErrorClick();
                            }
                        }
                    });
                    errorView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (errorClickListener != null) {
                                errorClickListener.onErrorClick();
                            }
                        }
                    });
                }
                holder = new RecyclerViewHolder(context, errorView);
                break;
            case EMPTY_VIEW:
                if (emptyView == null) {
                    emptyView = setDefaultEmptyView(parent);
                }
                holder = new RecyclerViewHolder(context, emptyView);
                break;
            default:
                holder = new RecyclerViewHolder(context,
                        LayoutInflater.from(context).inflate(getLayoutIdType(viewType), parent, false));
        }

        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case HEADER_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            case EMPTY_VIEW:
                bindEmptyData(holder);
                break;
            case ERROR_VIEW:
                break;
            default:
                if (mClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mItems != null && !mItems.isEmpty()) {
                                mClickListener.onItemClick(holder.itemView, position - headerViewCount);

                            }
                        }
                    });
                }
                if (mLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (mItems != null && !mItems.isEmpty()) {
                                if (position - headerViewCount < getDataCount()) {
                                    mLongClickListener.onItemLongClick(holder.itemView, position - headerViewCount);
                                }
                            }
                            return true;
                        }
                    });
                }
                if (position - headerViewCount < getDataCount()) {
                    bindData(holder, mItems.get(position - headerViewCount), position - headerViewCount);
                }
        }
    }

    @Override
    public int getItemCount() {
        int size = getDataCount();

        int type = isEmpty();
        if (type == 1) {
            size++;
        } else if (type == 2) {
            size++;
        }
        if (footerView != null) {
            size++;
        }
        if (headerView != null) {
            size++;
        }
        return size;
    }

    public int getDataCount() {
        int size = mItems == null ? 0 : mItems.size();
        return size;
    }

    public void add(int pos, T item) {
        mItems.add(pos, item);
        notifyItemInserted(pos);
    }


    public void setmItems(List<T> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    public void delete(int pos) {
        mItems.remove(pos);
        notifyItemRemoved(pos);
    }

    public void swap(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    final public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    final public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    final public void setOnErrorClickListener(OnErrorItemClickListener listener) {
        errorClickListener = listener;
    }

    /**
     * 重写该方法，根据viewType设置item的layout
     *
     * @param viewType 通过重写getItemViewType（）设置，默认item是0
     * @return
     */
    abstract protected int getLayoutIdType(int viewType);

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return HEADER_VIEW;
        }

        if (footerView != null) {
//            if (position >= (getItemCount() + headerViewCount)) {
//                return FOOTER_VIEW;
//            }
            if (position >= (getItemCount() - 1)) {
                return FOOTER_VIEW;
            }
        }
        int type = isEmpty();
        if (type == 1) {
            return EMPTY_VIEW;
        } else if (type == 2) {
            return ERROR_VIEW;
        } else {
            return getDefItemViewType(position - headerViewCount);
        }
    }

    /**
     * 是否是空数据
     *
     * @return
     */

    public int isEmpty() {
        int itemcount = 0;
        if (mItems != null && !mItems.isEmpty()) {
            itemcount = getDataCount();
        }
        boolean dataEmpty = itemcount == 0 ? true : false;
        if (errorViewType == 1 && dataEmpty) {
            return 1;
        } else if (errorViewType == 2 && dataEmpty) {
            return 2;
        }
        return 0;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }

    public void setHeaderView(View headerView) {
        headerViewCount = 1;
        this.headerView = headerView;
    }

    public View gettErrorView() {
        return errorView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    /**
     * 方便改变空数据界面的大小
     *
     * @return
     */
    public View getEmptyView() {
        return emptyView;
    }

    /**
     * 错误界面不允许重写
     *
     * @param parent
     * @return
     */
    private final View setDefaultError(ViewGroup parent) {

        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        return mInflater.inflate(R.layout.no_network_layout, parent, false);
    }

    /**
     * 重写 可改变空数据界面
     *
     * @param parent
     * @return
     */
    public View setDefaultEmptyView(ViewGroup parent) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        return mInflater.inflate(R.layout.no_data_layout, parent, false);
    }

    /***
     * 重写可以控制空数据界面的值
     *
     * @param holder
     */
    protected void bindEmptyData(RecyclerViewHolder holder) {

    }

    public void setLoadEmpty() {
        errorViewType = 1;
    }

    public void setLoadError() {
        errorViewType = 2;
    }

    public void setLoadError(View view) {
        errorView = view;
        errorViewType = 2;
        notifyDataSetChanged();
    }

    public void setLoadRest() {
        errorViewType = 0;
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 重写该方法进行item数据项视图的数据绑定
     *
     * @param holder   通过holder获得item中的子View，进行数据绑定
     * @param position 该item的position
     * @param item     映射到该item的数据
     */
    abstract protected void bindData(RecyclerViewHolder holder, T item, int position);


    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int pos);

    }

    public interface OnErrorItemClickListener {
        void onErrorClick();

    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return SizeUtils.dp2px(dpValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    protected int px2dip(float pxValue) {
        return SizeUtils.px2dp(pxValue);
    }

    public List<T> getmItems() {
        return mItems;
    }

}
