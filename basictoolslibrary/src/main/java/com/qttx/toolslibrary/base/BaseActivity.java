package com.qttx.toolslibrary.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.umeng.analytics.MobclickAgent;
import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.library.picture.PictureHelper;
import com.qttx.toolslibrary.library.swaipLayout.SwipeBackActivity;
import com.qttx.toolslibrary.net.ErrorMsgBean;
import com.qttx.toolslibrary.utils.ActivityManagerUtils;
import com.qttx.toolslibrary.utils.PermissionsNameHelp;
import com.qttx.toolslibrary.utils.SizeUtils;
import com.qttx.toolslibrary.utils.ToastUtils;
import com.qttx.toolslibrary.widget.loading.SpotsDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * Created by huangyuru on 2017/7/27.
 * 关于界面状态的构思
 * 1.加载界面,强制动态写入父控件,请求时,如果传入iveiw,则调用onload,成功时隐藏,失败时看==>3
 * 2.空数据界面,严格逻辑来讲,空数据界面只有列表展示界面界面有,空数据界面由adapter来控制,方便又下拉时可以下拉刷新
 * 3.错误界面(包含网络错误,请求错误,服务器错误)功能暂定为,不同的文字,不同的图片,有无重新请求事件,三种处理方式
 * 1.下拉分页列表,完全有adapter控制,封装类已经完全封装
 * 2.普通列表界面.通过重写showErrorView()为true,显示默认界面,或者重写onNoNet(),自定义显示错误界面
 * 3.详情界面同上(备注:如果不通过statusViewManager控制显示错误界面显示,要先调用statusViewManager.hideloaing)
 */
public abstract class BaseActivity<T extends BasePresenter> extends SwipeBackActivity implements Iview, StatusViewManager.onRetryClick, EasyPermissions.PermissionCallbacks {

    protected TextView top_title;
    protected ImageView top_left;
    protected TextView top_right;
    protected ImageView top_right_iv;
    protected Context appcontext;
    protected FrameLayout top_view;
    protected T mPresenter;
    protected StatusViewManager statusViewManager;

    protected SpotsDialog spotsDialog;

    private PictureHelper pictureHelper = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        addActivity();
        appcontext = getApplication();
        View view = getLayoutInflater().inflate(getLayoutId(), null);
        setContentView(view);

        statusViewManager = StatusViewManager.createView(this, view, getLoadingShow(), getErrorShow(), getStatusViewMarginTop());
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        beforeProcessLogic(savedInstanceState);
        processLogic();
    }

    public void beforeProcessLogic(@Nullable Bundle savedInstanceState) {

    }

    protected void addActivity() {
        ActivityManagerUtils.getActivityManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 在此处detachView
     * 如果bindLifecycle 用的ActivityEvent是onDestroy
     * 注意 onComplete 之前会先走
     * mPersenter.detachView();
     * mView为null;
     */
    @Override
    protected void onDestroy() {
        dimissLoadingDialog();
        statusViewManager.onDestory();
        ActivityManagerUtils.getActivityManager().finishActivity(this);
        super.onDestroy();
        if (pictureHelper != null) {
            pictureHelper.destroy();
            pictureHelper = null;
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (pictureHelper != null) {
            outState.putParcelable("pictureHelper", pictureHelper);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        PictureHelper helper = savedInstanceState.getParcelable("pictureHelper");
        if (helper != null) {
            if (pictureHelper != null) {
                pictureHelper.destroy();
                pictureHelper = null;
            }
            pictureHelper = helper;
            pictureHelper.setActivity(this);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onLoadByDialog() {
        dimissLoadingDialog();
        spotsDialog = new SpotsDialog(this, "正在加载...");
        spotsDialog.show();
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

    @Override
    public void showToast(String message) {
        ToastUtils.showShort(message);
    }

    /**
     * 当重新加载点击时被回调
     */
    @Override
    public void onRetryLoad() {

    }

    /**
     * @描述 加载布局文件
     */
    protected abstract int getLayoutId();

    protected T getPresenter() {
        return null;
    }

    /**
     * @描述 处理业务
     */
    protected abstract void processLogic();

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

    protected final <T> T $(int id) {
        return (T) findViewById(id);
    }

    protected final <T> T $(View view, int id) {
        return (T) view.findViewById(id);
    }

    protected void setTopTitle(String title) {
        top_view = (FrameLayout) findViewById(R.id.top_view);
        top_left = (ImageView) findViewById(R.id.top_left);
        top_title = (TextView) findViewById(R.id.top_news_title);
        top_title.setVisibility(View.VISIBLE);
        top_title.setText(title);
        top_left.setVisibility(View.VISIBLE);
        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setTopTitle(String title, String rightText, View.OnClickListener listener) {
        setTopTitle(title);
        top_right = (TextView) findViewById(R.id.top_right);
        top_right.setVisibility(View.VISIBLE);
        top_right.setText(rightText);
        top_right.setOnClickListener(listener);
    }

    protected void setTopTitle(String title, int rightres, View.OnClickListener listener) {
        setTopTitle(title);
        top_right_iv = (ImageView) findViewById(R.id.top_iv_right);
        top_right_iv.setVisibility(View.VISIBLE);
        top_right_iv.setImageResource(rightres);
        top_right_iv.setOnClickListener(listener);
    }

    /**
     * 设置状态view中的距离顶部的高度
     *
     * @return
     */
    protected int getStatusViewMarginTop() {
        return SizeUtils.dp2px(45);
    }

    /**
     * 绑定生命周期
     */
    @Override
    public <T> LifecycleTransformer<T> bindLifecycle() {
        return bindToLifecycle();
    }

    /**
     * 绑定生命周期
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull ActivityEvent event) {
        return bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull FragmentEvent event) {
        throw new IllegalStateException("Could not user FragmentEvent in Activity");
    }

    /**
     * 绑定生命周期
     */
    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull ActivityEvent activityEvent, @NonNull FragmentEvent fragmentEvent) {
        return bindLifecycle(activityEvent);
    }

    @Override
    public void onLowMemory() {
        Runtime.getRuntime().gc();
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 绑定view跟layoutmanager 快速回到顶部
     *
     * @param view
     * @param manager
     */
    public void setViewClickToTop(View view, final LinearLayoutManager manager) {
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

    public PictureHelper getPictureHelper() {
        if (null == pictureHelper) {
            pictureHelper = new PictureHelper(this);
        }
        return pictureHelper;
    }

    /**
     * 销毁用于加载的对话框
     */
    private void dimissLoadingDialog() {
        if (spotsDialog != null && spotsDialog.isShowing()) {
            spotsDialog.dismiss();
            spotsDialog = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 通知easyPermission权限申请的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 获取权限成功
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
    }

    /**
     * 获取权限失败
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            String message = PermissionsNameHelp.getPermissionsMulti(list);
            new AppSettingsDialog.Builder(this).setTitle("权限申请")
                    .setRationale(message)
                    .setNegativeButton("暂不")
                    .setPositiveButton("设置")
                    .setThemeResId(R.style.AlertDialogTheme)
                    .build()
                    .show();
        }
    }

    /**
     * EasyPermissions.hasPermissions 适用于单个权限申请，当多个权限传入时，一个失败则都失败
     * 当申请已经同意的权限时，如果点了拒绝会在应用内修改app权限导致闪退
     * 固 当同时申请多个时 使用自定义的hasPermissions
     *
     * @param requestCode
     * @param strings
     */
    public void requsetPerMission(int requestCode, String... strings) {
        //过滤出没有授权的权限。
        String[] perms = hasPermissions(this, strings);
        if (perms == null) {
            PerMissionSuccess(requestCode);
        } else {
            //进行申请权限的操作
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, requestCode, perms)
                            .setRationale(PermissionsNameHelp.getPermissionsMulti(strings))
                            .setPositiveButtonText("设置")
                            .setNegativeButtonText("暂不")
                            .setTheme(R.style.AlertDialogTheme)
                            .build());
        }
    }

    /**
     * 权限申请成功
     */
    private void PerMissionSuccess(int requestCode) {
        String[] permissions = new String[]{"SUCCESS"};
        int[] grantResults = new int[1];
        grantResults[0] = PackageManager.PERMISSION_GRANTED;
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public static String[] hasPermissions(Context context, @NonNull String... perms) {
        List<String> strings = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //小于android6.0的不需要动态申请权限。
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

    public void jumpToContainerActivity(Class c, Bundle bundle) {
        ContainerActivity.startContainerActivity(this, c.getCanonicalName(), bundle);
    }

    public void jumpToContainerActivity(Class c) {
        ContainerActivity.startContainerActivity(this, c.getCanonicalName());
    }

    @Override
    public Context getIviewContext() {
        return this;
    }
}
