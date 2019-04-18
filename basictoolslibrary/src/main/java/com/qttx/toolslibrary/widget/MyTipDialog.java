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


public class MyTipDialog extends Dialog implements OnClickListener {
    private LayoutInflater factory;

    private Context context;
    private TextView sure;
    private TextView title;
    private String text;
    private String rigthString;
    public MyTipDialog(Context context, String text) {
        this(context, R.style.mydialogstyle, text);
    }

    public MyTipDialog(Context context, int theme, String text) {
        super(context, theme);
        this.context = context;
        this.text = text;
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(factory.inflate(R.layout.my_tip_dialog, null));
        title = (TextView) findViewById(R.id.dialog_message);
        sure = (TextView) findViewById(R.id.yes);
//        sure.setTextColor(SkinManager.getInstance().getColor(R.color.primary_color));
        if (!TextUtils.isEmpty(rigthString))
        {
            sure.setText(rigthString);
        }
        title.setText(text);
        sure.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.yes)
        {
            onRight();
            this.dismiss();
        }
    }

    public void onLeft() {

    }

    public void onRight() {

    }


    public void setRightText(String text)
    {
        this.rigthString=text;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
