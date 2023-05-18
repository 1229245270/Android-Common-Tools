package com.hzc.commontools.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.hzc.commontools.ui.imagelookview.AvoidOnResult;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 12292
 */
public class PhotoUtil {

    /**
     * 选取相册照片
     * @param activity 上下文
     * @param requestCode 请求码
     */
    /*public static void takePhotoFromAlbum(Activity activity, int requestCode){
        EasyPhotos.createAlbum(activity, true, false, GlideEngine.getInstance())
                .setFileProviderAuthority(activity.getPackageName() + ".provider")
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .start(requestCode);
    }*/

    /**
     * 选取相册照片
     * @param activity 上下文
     * @param isShowCamera 是否显示拍照按钮
     * @param selectPhoto 选择照片
     * @param callback 回调
     */
    public static void takePhotoFromAlbum(Activity activity,boolean isShowCamera, ArrayList<Photo> selectPhoto,TakePhotoFormAlbumCallback callback){
        Intent intent = EasyPhotos.createAlbum(activity, isShowCamera, false, GlideEngine.getInstance())
                .setFileProviderAuthority(activity.getPackageName() + ".provider")
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                //当传入已选中图片时，按照之前选中的顺序排序
                .setSelectedPhotos(selectPhoto)
                .start();
        new AvoidOnResult(activity).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if(resultCode == Activity.RESULT_OK){
                    ArrayList<Photo> photos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                    callback.toImageList(photos);
                }else{
                    callback.toImageList(null);
                }
            }
        });
    }


    /**
     * 拍摄原图照片
     * @param activity 上下文
     * @return 返回图片路径
     */
    public static void takePhotoFromCamera(AppCompatActivity activity, TakePhotoFormCameraCallback callback){
        takePhotoFromCamera(activity, "camera" , callback);
    }

    /**
     * 拍摄原图照片
     * @param activity 上下文
     * @param fileName 文件夹地址
     * @param callback 图片回调，返回null为无拍摄
     */
    public static void takePhotoFromCamera(AppCompatActivity activity, String fileName,TakePhotoFormCameraCallback callback){
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions).subscribe(granted -> {
            if(granted){
                long time = System.currentTimeMillis();
                String filePath;
                // 启动系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri;
                //判断7.0android系统以上
                //通过FileProvider获取uri
                //临时添加一个拍照权限
                //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filePath = FileUtil.getCacheDir(activity,fileName) + time + ".jpg";
                    uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", new File(filePath));
                } else {
                    //不知道原因，必须使用外部路径，否则模拟器点确认没反应，实机request返回0
                    filePath = FileUtil.getExternalCacheDir(activity,fileName) + time + ".jpg";
                    uri = Uri.fromFile(new File(filePath));
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                new AvoidOnResult(activity).startForResult(intent, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if(resultCode == Activity.RESULT_OK){
                            callback.toImagePath(filePath);
                        }else{
                            callback.toImagePath(null);
                        }
                    }
                });
            }else{
                callback.toImagePath(null);
            }
        });
    }

    public interface TakePhotoFormCameraCallback{
        /**
         * 返回图片存放路径
         * @param filePath 图片路径
         */
        void toImagePath(String filePath);
    }

    public interface TakePhotoFormAlbumCallback{
        /**
         * 返回图片列表存放路径
         * @param list 图片列表路径
         */
        void toImageList(ArrayList<Photo> list);
    }

}
