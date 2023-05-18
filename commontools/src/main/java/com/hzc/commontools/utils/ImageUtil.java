package com.hzc.commontools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author 12292
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";
    private static final float DEFAULT_WW = 480;
    private static final float DEFAULT_HH = 800;
    private static final int DEFAULT_MAX_SIZE = 1024;

    /**
     * 对原文件的处理方式
     */
    public enum DELETE_TYPE{
        //默认删除，但只删除缓存路径下的文件
        DEFAULT,
        //删除文件
        DELETE,
        //不删除文件
        NOT_DELETE
    }

    /**
     * 质量压缩
     * @param context 上下文
     * @param filePath 原文件路径
     * @return 压缩后图片的路径
     */
    public static String qualityCompress(Context context, String filePath){
        return qualityCompress(context, "qualityCompress", filePath,DEFAULT_MAX_SIZE,DELETE_TYPE.DEFAULT);
    }

    /**
     * 质量压缩
     * png格式压缩没有作用，bytes.length不会变化，因为png图片是无损的，不能进行压缩。
     * @param context 上下文
     * @param imagePath 缓存图片的文件
     * @param filePath 原文件路径
     * @param maxSize 最大压缩大小
     * @param type 对原文件的处理方式
     * @return 压缩后图片的路径
     */
    public static String qualityCompress(Context context,String imagePath, String filePath,int maxSize,DELETE_TYPE type){
        FileInputStream is = null;
        String returnString = null;
        try {
            //原文件后缀
            String suffix = filePath.substring(filePath.lastIndexOf("."));
            //压缩后缓存图片的路径
            long time = System.currentTimeMillis();
            String newFilePath = FileUtil.getCacheDir(context,imagePath) + time + "_quality" + suffix;
            File newFile = new File(newFilePath);
            File oldFile = new File(filePath);
            is = new FileInputStream(oldFile);
            Bitmap bm = BitmapFactory.decodeStream(is);
            if(bm != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int options = 100;
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
                while (baos.toByteArray().length / 1024 > maxSize) {
                    baos.reset();
                    options -= 10;
                    bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
                }
                try {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(newFile.exists()){
                    deleteOldFile(context,oldFile,type);
                    returnString = newFilePath;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return returnString;
    }

    /**
     * 比例压缩
     * @param context 上下文
     * @param filePath 原文件路径
     * @return 压缩后图片的路径
     */
    public static String proportionCompress(Context context,String filePath){
        return proportionCompress(context, "proportionCompress", filePath,DEFAULT_WW,DEFAULT_HH,DELETE_TYPE.DEFAULT);
    }

    /**
     * 比例压缩
     * @param context 上下文
     * @param imagePath 缓存图片的文件
     * @param filePath 原文件路径
     * @param ww 宽度
     * @param hh 高度
     * @param type 对原文件的处理方式
     * @return 压缩后图片的路径
     */
    public static String proportionCompress(Context context,String imagePath,String filePath,float ww,float hh,DELETE_TYPE type) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        //如果该值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。但是允许我们查询图片的信息这其中就包括图片大小信息
        newOpts.inJustDecodeBounds = true;
        //此时返回bm为空，
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为2400*1440
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        //be=1表示不缩放
        int be = 1;
        //如果宽度大的话根据宽度固定大小缩放
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
            //如果高度高的话根据宽度固定大小缩放
        } else if (h > w && h > hh) {
            int beHeight = (int) (newOpts.outHeight / hh);
            if(beHeight > be){
                be = (int) (newOpts.outHeight / hh);
            }
        }
        if (be <= 0){
            be = 1;
        }
        //设置缩放比例
        newOpts.inSampleSize = be;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(filePath, newOpts);
        //原文件后缀
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        long time = System.currentTimeMillis();
        String newFilePath = FileUtil.getCacheDir(context,imagePath) + time + "_proportion" + suffix;
        File newFile = new File(newFilePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(newFile.exists()){
            File oldFile = new File(filePath);
            deleteOldFile(context,oldFile,type);
            return newFilePath;
        }
        return null;
    }

    /**
     * 默认压缩
     * @param context 上下文
     * @param filePath 原文件路径
     * @return 压缩后图片的路径
     */
    public static String defaultCompress(Context context,String filePath){
        String compress = proportionCompress(context, filePath);
        return qualityCompress(context, compress);
    }

    /**
     * 默认压缩
     * @param context 上下文
     * @param imagePath 缓存图片的文件
     * @param filePath 原文件路径
     * @param maxSize 最大压缩大小
     * @param ww 宽度
     * @param hh 高度
     * @param type 对原文件的处理方式
     * @return 压缩后图片的路径
     */
    public static String defaultCompress(Context context,String imagePath,String filePath,int maxSize,float ww,float hh,DELETE_TYPE type){
        String compress = proportionCompress(context, imagePath, filePath, ww, hh, type);
        return qualityCompress(context, imagePath, compress, maxSize, type);
    }

    /**
     * 删除旧文件
     * @param context 上下文
     * @param oldFile 旧文件
     * @param type 删除方式
     */
    private static void deleteOldFile(Context context,File oldFile,DELETE_TYPE type){
        String oldPath = oldFile.getPath();
        String cachePath = context.getCacheDir().getPath();
        String filesPath = context.getFilesDir().getPath();
        String externalCachePath = context.getExternalCacheDir().getPath();
        String externalFilesPath = context.getExternalFilesDir(null).getPath();
        switch (type){
            //只删除缓存中的图片，不删除本机照片
            case DEFAULT:
                if(oldPath.startsWith(cachePath) || oldPath.startsWith(filesPath) || oldPath.startsWith(externalCachePath) || oldPath.startsWith(externalFilesPath)){
                    boolean isSuccess = oldFile.delete();
                    Log.d(TAG, oldPath + "文件删除操作" + isSuccess);
                }
                break;
            //删除照片
            case DELETE:
                boolean isSuccess = oldFile.delete();
                Log.d(TAG, oldPath + "文件删除操作" + isSuccess);
                break;
            //不删除照片
            case NOT_DELETE:
                break;
            default:
        }
    }

}
