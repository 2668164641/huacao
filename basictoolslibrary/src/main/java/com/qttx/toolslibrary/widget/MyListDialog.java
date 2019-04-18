package com.qttx.toolslibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.base.ModuleAdpaer;
import com.qttx.toolslibrary.base.ModuleViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MyListDialog extends Dialog implements OnClickListener {

    private LayoutInflater factory;


    private ListView listview;


    private Button mCancel;

    /**
     * 数据集合
     */
    private List<String> list;
    /**
     * 选中的文本
     */
    private String selectText;
    /**
     * 返回按钮的文本
     */
    private String bottomText;
    private Context context;

    public MyListDialog(Context context, String selectText) {
        this(context, null, selectText);
    }

    public MyListDialog(Context context, List<String> list, String selectText) {
        this(context, R.style.mydialogstyle, list, selectText);
    }

    public MyListDialog(Context context, int theme, List<String> list, String selectText) {
        super(context, R.style.mydialogstyle);
        this.context = context;
        this.selectText = selectText;
        factory = LayoutInflater.from(context);
        this.list = list;
        this.selectText = selectText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(factory.inflate(
                R.layout.list_dialog_layout, null));
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();
        Window window = this.getWindow();
        LayoutParams dialogParams = window.getAttributes();
        dialogParams.width = (int) (d.getWidth() * 0.90);
//		dialogParams.height = (int) (d.getHeight() * 0.35);
        if (list == null) {
            list = new ArrayList<String>();
            list.add("拍照");
            list.add("相册");
        }
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomdialog);
        window.setAttributes(dialogParams);

        listview = (ListView) this.findViewById(R.id.listview);
        listview.setAdapter(new DialogAdapter(context, list, selectText));
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                onListItemClick(i, list.get(i));
//                dismiss();
//            }
//        });
        mCancel = (Button) this.findViewById(R.id.gl_choose_cancel);

        if (!TextUtils.isEmpty(bottomText)) {
            mCancel.setText(bottomText);
        }
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gl_choose_cancel) {
            dismiss();
        }
    }

    public void onListItemClick(int position, String selectText) {

    }

    public class DialogAdapter extends ModuleAdpaer<String> {
        private String selectTexttext;

        public DialogAdapter(Context context, List<String> result, String selectText) {
            super(context, result);
            selectTexttext = selectText;
        }


        @Override
        public int getLayoutIdType(int type) {
            return R.layout.dialog_list_item;
        }

        @Override
        public void bindData(ModuleViewHolder viewHolder, final String bean, final int position) {
            Button text = viewHolder.findViewById(R.id.text);
            text.setText(bean);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onListItemClick(position, bean);
                    dismiss();
                }
            });
            if (bean.equals(selectTexttext)) {
                text.setTextColor(context.getResources().getColor(R.color.primaryColor));
            } else {
                text.setTextColor(context.getResources().getColor(R.color.deepColor));
            }
        }
    }

    public void setTitle(String text) {
        bottomText = text;
    }
}
