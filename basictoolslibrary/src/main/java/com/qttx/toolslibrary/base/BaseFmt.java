package com.qttx.toolslibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.library.picture.PictureHelper;
import com.qttx.toolslibrary.net.ErrorMsgBean;
import com.qttx.toolslibrary.utils.PermissionsNameHelp;
import com.qttx.toolslibrary.utils.SizeUtils;
import com.qttx.toolslibrary.utils.ToastUtils;
import com.qttx.toolslibrary.widget.loading.SpotsDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public abstract class BaseFmt<T extends BasePresenter> extends RxFragment implements Iview, StatusViewManager.onRetryClick, EasyPermissions.PermissionCallbacks {
    protected Context context;
    protected StatusViewManager statusViewManager;
    protected T mPresenter;
    protected SpotsDialog dialog;
    /**
     * 是否初始化
     */
    private boolean hasCreateView;

    /**
     * 是否处于可见状态
     */
    protected boolean isFragmentVisible;
    protected FrameLayout rootView;
    protected TextView top_title;
    protected TextView top_news_title;
    protected ImageView top_left;
    protected TextView top_right;
    protected ImageView top_right_iv;
    protected FrameLayout top_view;
    private PictureHelper pictureHelper = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new FrameLayout(context);
        View contentview = inflater.inflate(getLayoutId(), null);
        rootView.addView(contentview, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        statusViewManager = StatusViewManager.createView(context, contentview, getLoadingShow(), getErrorShow(), getStatusViewMarginTop());
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        if (!isLazy()) {
            processLogic();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            onShow();
            isFragmentVisible = true;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            PictureHelper helper = savedInstanceState.getParcelable("pictureHelper");
            if (helper != null) {
                if (pictureHelper != null) {
                    pictureHelper.destroy();
                    pictureHelper = null;
                }
                pictureHelper = helper;
                pictureHelper.setFragment(this);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (pictureHelper != null) {
            pictureHelper.destroy();
            pictureHelper = null;
        }
        dimissLoadingDialog();
        statusViewManager.onDestory();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 设置状态view中的距离顶部的高度
     *
     * @return
     */
    protected int getStatusViewMarginTop() {
        return SizeUtils.dp2px(45);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onShow();
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onHide();
            isFragmentVisible = false;
        }
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    public void onShow() {
        if (isLazy()) {
            processLogic();
        }
    }

    public void onHide() {

    }

    public void reshData() {

    }

    protected abstract int getLayoutId();

    protected T getPresenter() {
        return null;
    }

    /**
     * @描述 处理业务
     */
    protected abstract void processLogic();

    @Override
    public void onLoadByDialog() {
        dimissLoadingDialog();
        dialog = new SpotsDialog(context, "正在加载...");
        dialog.show();
    }

    @Override
    public void onLoadByView() {
        statusViewManager.onLoad();
    }

    @Override
    public void onSuccess() {
        dimissLoadingDialog();
        statusViewManager.onSuccess();
    }


    /**
     * @return
     */
    @Override
    public void onReLoadErrorShow(ErrorMsgBean errorMsgBean) {
        dimissLoadingDialog();
        if (errorMsgBean.isCanRetry()) {
            statusViewManager.onNoNet(errorMsgBean.getErrorMsg(), errorMsgBean.getImageRes(), this);
        } else {
            statusViewManager.onNoNet(errorMsgBean.getErrorMsg(), errorMsgBean.getImageRes(), null);
        }
    }


    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle() {
        return bindToLifecycle();
    }

    /**
     * 绑定生命周期
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull FragmentEvent event) {
        return bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull ActivityEvent event) {
        throw new IllegalStateException("Could not user ActivityEvent in fragment");
    }

    /**
     * 绑定生命周期
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull ActivityEvent activityEvent, @NonNull FragmentEvent fragmentEvent) {
        return bindLifecycle(fragmentEvent);
    }

    /**
     * 采用默认loadingView
     *
     * @return
     */
    public int getLoadingShow() {
        return -1;
    }

    /**
     * 采用默认ErrorView
     * 通过ErrorBean控制显示
     *
     * @return
     */
    public int getErrorShow() {
        return -1;
    }

    /**
     * 当控制显示了重新加载按钮后，点击时被回调
     */
    @Override
    public void onRetryLoad() {

    }

    protected final <T> T $(int id) {
        return (T) rootView.findViewById(id);
    }

    protected final <T> T $(View view, int id) {
        return (T) view.findViewById(id);
    }

    protected void setTopTitle(String title) {
        top_view = (FrameLayout) rootView.findViewById(R.id.top_view);
        top_view.setVisibility(View.VISIBLE);
        top_left = (ImageView) rootView.findViewById(R.id.top_left);
        top_title = (TextView) rootView.findViewById(R.id.top_title);
        top_title.setText(title);
        top_left.setVisibility(View.VISIBLE);
        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    protected void setTopTitle(String title, boolean centerTitle) {
        top_view = (FrameLayout) rootView.findViewById(R.id.top_view);
        top_view.setVisibility(View.VISIBLE);
        if (centerTitle)
            top_news_title = (TextView) rootView.findViewById(R.id.top_news_title);
            top_news_title.setVisibility(View.VISIBLE);
            top_news_title.setText(title);
    }

    protected void setTopTitle(String title, String rightText, View.OnClickListener listener) {
        setTopTitle(title);
        top_right = (TextView) rootView.findViewById(R.id.top_right);
        top_right.setVisibility(View.VISIBLE);
        top_right.setText(rightText);
        top_right.setOnClickListener(listener);
    }

    protected void setTopTitle(String title, int rightres, View.OnClickListener listener) {
        setTopTitle(title);
        top_right_iv = (ImageView) rootView.findViewById(R.id.top_iv_right);
        top_right_iv.setVisibility(View.VISIBLE);
        top_right_iv.setImageResource(rightres);
        top_right_iv.setOnClickListener(listener);
    }

    /**
     * 销毁用于加载的对话框
     */
    private void dimissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 绑定view跟layoutmanager 快速回到顶部
     *
     * @param id
     * @param manager
     */
    public void setViewClickToTop(int id, final LinearLayoutManager manager) {
        if (rootView != null && manager != null) {
            View view = rootView.findViewById(id);
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.scrollToPositionWithOffset(0, 0);
                        manager.setStackFromEnd(true);
                    }
                });
            }
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showShort(message);
    }

    protected void showToast(int resId) {
        ToastUtils.showShort(resId);
    }


    /**
     * 是否启用懒加载
     *
     * @return
     */
    protected boolean isLazy() {
        return false;
    }

    public PictureHelper getPictureHelper() {
        if (null == pictureHelper) {
            pictureHelper = new PictureHelper(this);
        }
        return pictureHelper;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (pictureHelper != null) {
            outState.putParcelable("pictureHelper", pictureHelper);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            String message = PermissionsNameHelp.getPermissionsMulti(list);
            new AppSettingsDialog.Builder(this).setTitle("权限申请").
                    setRationale(message)
                    .setNegativeButton("暂不")
                    .setPositiveButton("设置")
                    .setThemeResId(R.style.AlertDialogTheme)
                    .build()
                    .show();
        }
    }

    public void requsetPerMission(int requestCode, String... strings) {
        String[] perms = hasPermissions(context, strings);
        if (perms == null) {
            PerMissionSuccess(requestCode);
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, requestCode, perms)
                            .setRationale(PermissionsNameHelp.getPermissionsMulti(strings))
                            .setPositiveButtonText("设置")
                            .setNegativeButtonText("暂不")
                            .setTheme(R.style.AlertDialogTheme)
                            .build());
        }
    }

    private void PerMissionSuccess(int requestCode) {
        String[] permissions = new String[]{"SUCCESS"};
        int[] grantResults = new int[1];
        grantResults[0] = PackageManager.PERMISSION_GRANTED;
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public static String[] hasPermissions(Context context, @NonNull String... perms) {
        List<String> strings = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null;
        }
        if (context == null) {
            throw new IllegalArgumentException("Can't check permissions for null context");
        }
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                if (strings == null) {
                    strings = new ArrayList<>();
                }
                strings.add(perm);
            }
        }
        if (strings != null && !strings.isEmpty()) {
            String[] toBeStored = strings.toArray(new String[strings.size()]);
            return toBeStored;
        }
        return null;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void jumpToContainerActivity(Class c, Bundle bundle) {
        ContainerActivity.startContainerActivity(context, c.getCanonicalName(), bundle);
    }

    public void jumpToContainerActivity(Class c) {
        ContainerActivity.startContainerActivity(context, c.getCanonicalName());
    }

    @Override
    public Context getIviewContext() {
        return context;
    }
}
