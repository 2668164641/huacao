package com.qttx.toolslibrary.library.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.qttx.toolslibrary.R;
import com.qttx.toolslibrary.utils.EmptyUtils;
import com.qttx.toolslibrary.utils.FileUtils;
import com.qttx.toolslibrary.utils.ImageUtils;
import com.qttx.toolslibrary.utils.PathUtils;
import com.qttx.toolslibrary.widget.loading.SpotsDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.filter.GifSizeFilter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huang on 2017/11/6.
 * 拍照帮助类
 */

public class PictureHelper implements Parcelable {

    private String mCurrentPhotoPath;

    private Uri mCurrentPhotoUri;
    /**
     * 拍照code
     */
    public static final int REQUEST_TAKE_PHOTO = 20000;

    /**
     * 裁图code
     */
    public static final int REQUEST_TAKE_PHOTO_CROP = 20001;

    /**
     * 相册列表code
     */
    public static final int REQUEST_CHOSE_PICKTURE = 20002;


    private RxAppCompatActivity activity;

    public RxAppCompatActivity getActivity() {
        return activity;
    }

    public void setActivity(RxAppCompatActivity activity) {
        this.activity = activity;
    }

    public RxFragment getFragment() {
        return fragment;
    }

    public void setFragment(RxFragment fragment) {
        this.fragment = fragment;
    }

    private RxFragment fragment;

    /**
     * 最大选择数
     */
    private int maxSize = 1;

    /**
     * 是否在图片列表启用相机
     */
    private boolean hasCamera = true;

    /**
     * 是否采用裁剪，单个图片有效
     */
    private boolean hasCrop = false;
    /**
     * 是否采用压缩
     */
    private boolean hasZip = false;

    private boolean hasZipDialog = true;



    protected SpotsDialog dialog;

    private SelectionCreator selectionCreator;

    /**
     * 数据结果回调
     */
    private PictureResultListener listener;

    public PictureHelper setMaxSize(int maxSize) {
        if (maxSize <= 0) {
            maxSize = 1;
        }
        this.maxSize = maxSize;
        return this;
    }


    public PictureHelper setHasCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
        return this;
    }


    public PictureHelper setHasCrop(boolean hasCrop) {
        this.hasCrop = hasCrop;
        return this;
    }

    public PictureHelper setHasZip(boolean hasZip) {
        this.hasZip = hasZip;
        return this;
    }

    public PictureHelper setHasZipDialog(boolean hasZipDialog) {
        this.hasZipDialog = hasZipDialog;
        return this;
    }



    /**
     * 请求参数会重置为SelectionCreator
     *
     * @param selectionCreator
     * @return
     */
    public PictureHelper setSelectionCreator(SelectionCreator selectionCreator) {
        this.selectionCreator = selectionCreator;
        return this;
    }
//    int requestCode, int resultCode, Intent data, final RxAppCompatActivity act, final RxFragment fragment, final boolean hasCrop, final ZipListener zipListener

    public PictureHelper(@Nullable RxAppCompatActivity activity) {
        this.activity = activity;
    }

    public PictureHelper(@Nullable RxFragment fragment) {
        this.fragment = fragment;
    }

    private PictureHelper() {

    }

    /**
     * 用自带相机拍照
     */
    public void takePhotoWithCamera() {
        startActivity(getTakePhotoIntent(getContext()), REQUEST_TAKE_PHOTO);
    }

    /**
     * 获取拍照的Intent
     *
     * @param context
     * @return
     */
    private Intent getTakePhotoIntent(Context context) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                mCurrentPhotoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", photoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    List<ResolveInfo> resInfoList = context.getPackageManager()
                            .queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        context.grantUriPermission(packageName, mCurrentPhotoUri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
            }
        }
        return captureIntent;
    }
    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 packname 文件夹中 照片的命名规则为：bodtec_20130125_173729.jpg
     */
    public  File createImageFile() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = format.format(new Date());
        String imageFileName = PathUtils.packname + "_" + timeStamp + ".jpg";
        // 获取保存图片的目录
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PathUtils.packname);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File imageFile = new File(dir, imageFileName);
        return imageFile;
    }
    /**
     * 跳转到照片选择界面
     */
    public void takePhoto() {
        SelectionCreator defaultselectionCreator;
        if (selectionCreator == null) {
            Matisse matisse;
            if (fragment != null) {
                matisse = Matisse.from(fragment);
            } else {
                matisse = Matisse.from(activity);
            }
            defaultselectionCreator = matisse.choose(MimeType.ofImage(), true)
                    .countable(true)
                    .capture(hasCamera)
                    .captureStrategy(
                            new CaptureStrategy(true, getContext().getPackageName() + ".FileProvider"))
                    .maxSelectable(maxSize)
                    .showSingleMediaType(true)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getContext().getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine());
        } else {
            defaultselectionCreator = selectionCreator;
        }
        defaultselectionCreator.forResult(REQUEST_CHOSE_PICKTURE);
    }

    public void getPhotoList(int requestCode, int resultCode, Intent data, PictureResultListener listener) {

        if (resultCode != Activity.RESULT_OK || data == null || listener == null) {
            return;
        }
        if (requestCode == REQUEST_TAKE_PHOTO_CROP) {
            /**
             * 如果是裁剪后的图片直接回调
             */
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                String path = resultUri.getPath();
                List<String> list = new ArrayList<>();
                list.add(path);
                listener.complete(list);
            } else {
                listener.error();
            }

        } else if (requestCode == REQUEST_CHOSE_PICKTURE) {
            /**
             * 如果是图库返回的根据配置处理图片
             */
            List<String> pathlist = Matisse.obtainPathResult(data);
            if (EmptyUtils.isNotEmpty(pathlist)) {
                dealPathData(pathlist, listener);
            } else {
                listener.error();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            /**
             * 如果是相机的返回的根据配置处理图片
             */
            File imageFile = new File(mCurrentPhotoPath);
            if (imageFile.exists()) {
                // 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
                // ImageUtils.galleryAddPic(act, imageFilePath);
                List<String> pathlist = new ArrayList<>();
                pathlist.add(mCurrentPhotoPath);
                //处理逻辑
                dealPathData(pathlist, listener);
            } else {
                listener.error();
            }
        }

    }

    private void dealPathData(List<String> listPaths, PictureResultListener listener) {
        /**
         * 如果图片是单张，并且设置了要进行裁剪。
         * 跳转到裁剪界面，压缩在裁剪界面完成
         * （防止图片过大，压缩时间长时，进入裁剪界面有等待时间，体验不好）
         */

        String exName= FileUtils.getFileExtension(listPaths.get(0));

        if (hasCrop && listPaths.size() == 1&&!"gif".equalsIgnoreCase(exName)) {
            startToCrop(listPaths.get(0), getDefaultCropOption());
        } else if (hasZip) {
            if (fragment != null) {
                beginZip(listPaths, fragment.<String>bindToLifecycle(), listener);
            } else {
                beginZip(listPaths, activity.<String>bindToLifecycle(), listener);
            }
        } else {
            listener.complete(listPaths);
        }
    }

    /**
     * Rxjava压缩图片
     *
     * @param list
     * @param lifecycleTransformer
     * @param listener
     */
    private void beginZip(final List<String> list, LifecycleTransformer<String> lifecycleTransformer, final PictureResultListener listener) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (String path : list) {
                    String exName= FileUtils.getFileExtension(path);
                    if ("gif".equalsIgnoreCase(exName))
                    {
                        e.onNext(path);
                    }else
                    {
                        e.onNext(ImageUtils.getSmallImagePath(path));
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer)
                .subscribe(new Observer<String>() {
                    List<String> path = new ArrayList<>();

                    @Override
                    public void onSubscribe(Disposable d) {
                        dimissLoadingDialog();
                        if (hasZipDialog) {
                            dialog = new SpotsDialog(getContext());
                            dialog.show();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        path.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.error();
                        dimissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dimissLoadingDialog();
                        listener.complete(path);
                    }
                });

    }

    /**
     * 默认的defug裁剪设置
     *
     * @return
     */
    private UCrop.Options getDefaultCropOption() {
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片的保存格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        //设置裁剪图片的图片质量
        options.setCompressionQuality(90);
        options.withAspectRatio(1, 1);
        options.setFreeStyleCropEnabled(true);
        options.setImageNeddZip(hasZip);
        return options;
    }

    private void startToCrop(String inputString, UCrop.Options options) {
        String path = PathUtils.PATH_CROP + String.valueOf((new Date())
                .getTime()) + ".jpg";
        File f = new File(PathUtils.PATH_CROP);
        if (!f.exists()) {
            f.mkdirs();
        }
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(inputString)), Uri.fromFile(new File(path)));
        uCrop.withOptions(options);
        if (fragment != null) {
            uCrop.start(fragment.getContext(), fragment, REQUEST_TAKE_PHOTO_CROP);
        } else {
            uCrop.start(activity, REQUEST_TAKE_PHOTO_CROP);
        }
    }

    private Context getContext() {
        if (fragment != null) {
            return fragment.getActivity();
        } else {
            return activity;
        }
    }

    private void startActivity(Intent intent, int code) {
        if (fragment != null) {
            fragment.startActivityForResult(intent, code);
        } else {
            activity.startActivityForResult(intent, code);
        }
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

    public void destroy() {
        dimissLoadingDialog();
        this.activity = null;
        this.fragment = null;
        this.listener = null;
    }

    public interface PictureResultListener {
        void complete(List<String> list);

        void error();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCurrentPhotoPath);
        dest.writeParcelable(this.mCurrentPhotoUri, flags);
        dest.writeInt(this.maxSize);
        dest.writeByte(this.hasCamera ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasCrop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasZip ? (byte) 1 : (byte) 0);
    }

    protected PictureHelper(Parcel in) {
        this.mCurrentPhotoPath = in.readString();
        this.mCurrentPhotoUri = in.readParcelable(Uri.class.getClassLoader());
        this.maxSize = in.readInt();
        this.hasCamera = in.readByte() != 0;
        this.hasCrop = in.readByte() != 0;
        this.hasZip = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PictureHelper> CREATOR = new Parcelable.Creator<PictureHelper>() {
        @Override
        public PictureHelper createFromParcel(Parcel source) {
            return new PictureHelper(source);
        }

        @Override
        public PictureHelper[] newArray(int size) {
            return new PictureHelper[size];
        }
    };
}
