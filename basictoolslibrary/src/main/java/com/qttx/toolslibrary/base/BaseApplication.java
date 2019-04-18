package com.qttx.toolslibrary.base;

import android.app.Application;
import android.app.PendingIntent;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.qttx.toolslibrary.BuildConfig;
import com.qttx.toolslibrary.utils.AppUtils;
import com.qttx.toolslibrary.utils.LogUtils;
import com.qttx.toolslibrary.utils.PathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Administrator on 2016/6/20 0020.
 */
public abstract class BaseApplication extends Application {
    private static BaseApplication instance;
    public static int mWidth = 480;
    public static int mHeight = 800;
    public String myPackname;

    @Override
    public void onCreate() {
        super.onCreate();
        doInit();// 初始化
    }

    public void doInit() {
        AppUtils.init(this);
        myPackname = getAppFileName();
        initResolution();
        PathUtils.initPath(myPackname);
        //收集错误日志
        initErrorLog();
    }


    // 获取屏幕宽高
    public void initResolution() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;
    }


    /**********************************
     * 记录崩溃日志--start
     **********************************/
    private PendingIntent restartIntent;
    private MyUncaughtExceptionHandler uncaughtExceptionHandler;

    public void initErrorLog() {
        // 记录崩溃日志
//        if (BuildConfig.DEBUG) {
            cauchException();
//        }
    }

    // -------------------异常捕获-----捕获异常后重启系统-----------------//
    private void cauchException() {
        // 程序崩溃时触发线程
        uncaughtExceptionHandler = new MyUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    // 创建服务用于捕获崩溃异常
    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            // 保存错误日志
            saveCatchInfo2File(ex);

            // 5秒钟后重启应用
            // AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, restartIntent);

            // 关闭当前应用
            finishProgram();
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称
     */
    private String saveCatchInfo2File(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String sb = writer.toString();
        LogUtils.e("exception", sb);
        try {
            DateFormat formatter = new SimpleDateFormat("MM-dd-HH-mm-ss", Locale.getDefault());
            String time = formatter.format(new Date());
            String fileName = time + ".txt";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(PathUtils.PATH_ERROR);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        // 创建目录失败: 一般是因为SD卡被拔出了
                        return "";
                    }
                }
                FileOutputStream fos = new FileOutputStream(PathUtils.PATH_ERROR + fileName);
                fos.write(sb.getBytes());
                fos.close();
                // 文件保存完了之后,在应用下次启动的时候去检查错误日志,发现新的错误日志,就发送给开发者
            }
            return fileName;
        } catch (Exception e) {
            System.out.println("an error occured while writing file..." + e.getMessage());
        }
        return null;
    }

    public void finishProgram() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    /**
     * 设置文件保存路径名
     * 路径规则:
     * Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getAppFileName() + "/";
     *
     * @return
     */
    public abstract String getAppFileName();

}
