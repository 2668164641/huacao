package com.qttx.toolslibrary.widget;

import android.widget.LinearLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;


import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.base.ModuleAdpaer;
import com.qttx.toolslibrary.base.ModuleViewHolder;
import com.qttx.toolslibrary.library.picture.ImageBrowserActivity;
import com.qttx.toolslibrary.utils.EmptyUtils;
import com.qttx.toolslibrary.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyuru on 2016/12/16.
 */

public class ImageGrideShow extends LinearLayout {

    private GrideForScrollView grideForScrollView;
    private ImageGrideShow.ImageItemAdapter adapter;
    private ArrayList<String> imageList = new ArrayList<>();
    private Context contxt;

    public void setId(String id, int position) {
        this.position = position;
        this.id = id;
    }

    private String id;
    private int position;

    public ImageGrideShow(Context context) {
        this(context, null, 0);
    }

    public ImageGrideShow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGrideShow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.contxt = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.common_weight_imagegride, this, true);
        grideForScrollView = (GrideForScrollView) findViewById(R.id.gride_listview);

    }


    /**
     * @param list 数据集合
     */
    public void setImageList(List<String> list) {
        if (EmptyUtils.isEmpty(list)) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        imageList.clear();
        if (list != null) {
            imageList.addAll(list);
        }
        grideForScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageBrowserActivity.showActivity(contxt, imageList, position);
            }
        });
        adapter = new ImageGrideShow.ImageItemAdapter(contxt, imageList);
        grideForScrollView.setAdapter(adapter);
        grideForScrollView.setOnTouchBlankPositionListener(new GrideForScrollView.OnTouchBlankPositionListener() {
            @Override
            public void onTouchBlank(MotionEvent event) {
                if (listener != null) {
                    listener.onTouchBlank(event);
                }
            }
        });
    }

    public void setListener(GrideForScrollView.OnTouchBlankPositionListener listener) {
        this.listener = listener;
    }

    private GrideForScrollView.OnTouchBlankPositionListener listener;

    public class ImageItemAdapter extends ModuleAdpaer<String> {

        public ImageItemAdapter(Context context, List<String> result) {
            super(context, result);
        }

        @Override
        public int getLayoutIdType(int type) {
            return R.layout.common_list_item_weight_imagegride;
        }

        @Override
        public void bindData(ModuleViewHolder holder, String bean, int position) {
            RoundAngleImageView view = holder.findViewById(R.id.iv_photo);

            GlideUtils.loadImage(view, bean);
        }
    }
}
