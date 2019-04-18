package com.qttx.toolslibrary.library.update;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.base.BaseActivity;
import com.qttx.toolslibrary.utils.ActivityManagerUtils;
import com.qttx.toolslibrary.utils.PathUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;


/**
 * 软件更新页面
 */
public class UpdateActivity extends BaseActivity {

    private TextView txt_update_msg;
    private TextView txt_progress;
    private TextView txt_version_name;
    private Button btn_yes;
    private Button btn_not;
    private LinearLayout lLayout_updating;

    private ApkUpdate apkUpdate;
    private File apkFile;
    private String fileDir;
    private String fileName;
    private boolean isUpdating = false;

    private Disposable disposable;

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_update;
    }

    @Override
    protected void processLogic() {
        initView();
        initListener();
        init();
    }

    private void initView() {
        txt_version_name = (TextView) findViewById(R.id.txt_version_name);
        txt_progress = (TextView) findViewById(R.id.txt_progress);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_not = (Button) findViewById(R.id.btn_not);
        lLayout_updating = (LinearLayout) findViewById(R.id.lLayout_updating);

        txt_update_msg = (TextView) findViewById(R.id.txt_update_msg);
        txt_update_msg.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void initListener() {
        //点击关闭
        btn_not.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        //点击立即更新
        btn_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requsetPerMission(10001, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
    }

    private void init() {
        apkUpdate = (ApkUpdate) getIntent().getExtras().getSerializable("apkUpdate");
        if (apkUpdate == null) {
            return;
        }
        txt_version_name.setText(String.valueOf("V" + apkUpdate.getVersionNo()));
        txt_update_msg.setText(String.valueOf("更新内容：\n" + apkUpdate.getRemark()));
        if (!"01".equals(apkUpdate.isForceUpdate())) {
            btn_not.setVisibility(View.VISIBLE);
        } else {
            UpdateActivity.this.setFinishOnTouchOutside(false);
        }

        fileDir = PathUtils.PATH_FILE;
        fileName = System.currentTimeMillis() + ".apk";
        apkFile = new File(fileDir, fileName);
    }

    /**
     * 获取手机下载目录
     *
     * @return
     */
    public static File getDownloadDir() {
        File download_dir = new File(Environment.getExternalStorageDirectory()
                + "/Download/");
        if (!download_dir.exists()) {
            download_dir.mkdirs();
        }

        return download_dir;
    }

    /**
     * 下载apk
     */
    private void downloadApk() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                URL url1 = new URL(apkUpdate.getApk());
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Charset", "UTF-8");
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
                connection.setUseCaches(false);
                //打开连接
                connection.connect();
                //获取内容长度
                int contentLength = connection.getContentLength();
                InputStream inputStream = connection.getInputStream();
                //输出流
                FileOutputStream fileOutputStream = new FileOutputStream(apkFile);
                byte[] bytes = new byte[1024];
                long totalReaded = 0;
                int temp_Len;
                while ((temp_Len = inputStream.read(bytes)) != -1) {
                    totalReaded += temp_Len;
                    int progressSize = (int) (totalReaded / (double) contentLength * 100);
                    e.onNext(progressSize);
                    fileOutputStream.write(bytes, 0, temp_Len);
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .compose(this.<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer s) {
                        txt_progress.setText("更新中..." + s + "%");
                    }

                    @Override
                    public void onError(Throwable e) {
                        txt_progress.setText("暂时无法更新");
                    }

                    @Override
                    public void onComplete() {
                        //更新包文件
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= 24) {
                            // Android7.0及以上版本
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(UpdateActivity.this, getPackageName() + ".FileProvider", apkFile);
                            //参数二:应用包名+".fileProvider"(和步骤二中的Manifest文件中的provider节点下的authorities对应)
                            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        } else {
                            // Android7.0以下版本
                            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        startActivity(intent);
                    }

                });
    }

    /**
     * 下载更新
     */
    @AfterPermissionGranted(10001)
    private void toUpdate() {
        if (isUpdating) {
            toStopDownload();
            ActivityManagerUtils.getActivityManager().finishAllActivity();
        } else {
            isUpdating = true;
            btn_yes.setText("放弃更新");
            btn_not.setVisibility(View.GONE);
            lLayout_updating.setVisibility(View.VISIBLE);
            downloadApk();
        }
    }

    /**
     * 停止下载，删除已下载文件
     */
    private void toStopDownload() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (apkFile.exists()) {
            apkFile.delete();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return true;
    }

}
