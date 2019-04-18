package com.qttx.toolslibrary.widget.loading;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.qttx.toolslibrary.R;


/**
 */
public class SpotsDialog extends AlertDialog {

    private static final int DELAY = 150;
    private static final int DURATION = 1500;

    private int size;
    private AnimatedView[] spots;
    private AnimatorPlayer animator;
    private CharSequence message;
    private TextView tv;

    public SpotsDialog(Context context) {
        this(context, R.style.SpotsDialogDefault);
    }

    public SpotsDialog(Context context, CharSequence message) {
        this(context);
        this.message = message;
    }

    public SpotsDialog(Context context, CharSequence message, int theme) {
        this(context, theme);
        this.message = message;
    }

    public SpotsDialog(Context context, int theme) {
        super(context, theme);
    }

    public SpotsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_loading_layout);
        setCanceledOnTouchOutside(false);

        initMessage();
        initProgress();
    }

    @Override
    protected void onStart() {
        super.onStart();

        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }

    @Override
    protected void onStop() {
        super.onStop();

        animator.stop();
    }

    @Override
    public void setMessage(CharSequence message) {
        ((TextView) findViewById(R.id.dmax_spots_title)).setText(message);
    }

    //~

    private void initMessage() {
        tv = ((TextView) findViewById(R.id.dmax_spots_title));
        if (message != null && message.length() > 0) {
            tv.setText(message);
        }
    }

    public void initMessage(String text) {
        if (text != null && text.length() > 0) {
            if (tv == null) {
                tv = ((TextView) findViewById(R.id.dmax_spots_title));
            }
            tv.setText(text);

        }
    }

    private void initProgress() {
        ProgressLayout progress = (ProgressLayout) findViewById(R.id.dmax_spots_progress);
        size = progress.getSpotsCount();

        spots = new AnimatedView[size];
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.spot_size);
        int progressWidth = getContext().getResources().getDimensionPixelSize(R.dimen.progress_width);
        for (int i = 0; i < spots.length; i++) {
            AnimatedView v = new AnimatedView(getContext());
            v.setBackgroundResource(R.drawable.toolslib_dialog_spots_bk);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            progress.addView(v, size, size);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }
}
