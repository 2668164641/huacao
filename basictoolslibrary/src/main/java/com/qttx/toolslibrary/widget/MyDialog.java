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
import android.widget.TextView;

import com.qttx.toolslibrary.R;


public class MyDialog extends Dialog implements OnClickListener {
    private LayoutInflater factory;

    private Context context;
    private TextView sure;
    private TextView cancel;
    private TextView title;
    private String text;
    private String leftString;
    private String rigthString;
    private MyDialogClickListener listener;

    public MyDialog(Context context, String text) {
        this(context, R.style.mydialogstyle, text);
    }

    public MyDialog(Context context, int theme, String text) {
        super(context, theme);
        this.context = context;
        this.text = text;
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(factory.inflate(R.layout.my_dialog, null));
        title = (TextView) findViewById(R.id.dialog_message);
        sure = (TextView) findViewById(R.id.yes);
//        sure.setTextColor(SkinManager.getInstance().getColor(R.color.primary_color));
        cancel = (TextView) findViewById(R.id.no);
        if (!TextUtils.isEmpty(leftString)) {
            cancel.setText(leftString);
        }
        if (!TextUtils.isEmpty(rigthString)) {
            sure.setText(rigthString);
        }
        title.setText(text);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();
        Window window = this.getWindow();
//		LayoutParams dialogParams = window.getAttributes();
//		dialogParams.width = (int) (d.getWidth() * 0.90);
//		dialogParams.height = (int) (d.getHeight() * 0.35);
        window.setGravity(Gravity.CENTER);
        this.setCanceledOnTouchOutside(false);
        window.setWindowAnimations(R.style.mydialogstyle);
//		window.setAttributes(dialogParams);
    }

    public void setListener(MyDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yes) {
            onRight();
            this.dismiss();
        } else if (v.getId() == R.id.no) {
            onLeft();
            this.dismiss();
        }
    }

    public void onLeft() {
        if (listener!=null)
            listener.onLeft();

    }

    public void onRight() {
        if (listener!=null)
            listener.onRight();

    }


    public void setLeftText(String text) {
        this.leftString = text;
    }

    public void setRightText(String text) {
        this.rigthString = text;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface MyDialogClickListener {
        void onLeft();

        void onRight();
    }
}
