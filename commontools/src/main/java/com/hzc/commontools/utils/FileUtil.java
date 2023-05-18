package com.hzc.commontools.utils;

import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 12292
 */
public class FileUtil {

    private static final String TAG = "FileUtil";
    /**
     * 查看文件情况
     * @param activity 上下文
     * @param file 文件
     */
    public static long getBytes(Activity activity, File file, StorageManagerType storageManagerType){
        StorageManager storageManager = activity.getSystemService(StorageManager.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                UUID uuid = storageManager.getUuidForPath(file);
                switch (storageManagerType){
                    case getCacheSizeBytes:
                        return storageManager.getCacheSizeBytes(uuid);
                    case getCacheQuotaBytes:
                        return storageManager.getCacheQuotaBytes(uuid);
                    case getAllocatableBytes:
                        return storageManager.getAllocatableBytes(uuid);
                    default:
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public enum StorageManagerType{
        /**
         * 查询可用空间，您无需检查可用空间即可保存文件。您可以尝试立即写入文件，然后在出现 IOException 时将其捕获。
         */
        getAllocatableBytes,
        /**
         * 获取缓存配额字节
         * 如果您的应用程序超出此配额，您的缓存文件将在需要额外磁盘空间时首先被删除。
         * 相反，如果您的应用程序保持在该配额以下，则当需要额外的磁盘空间时，您的缓存文件将是最后删除的一部分。
         * 此配额会随着时间的推移而变化，具体取决于用户与您的应用程序交互的频率，以及系统范围内磁盘空间的使用量。
         */
        getCacheQuotaBytes,
        /**
         * 获取缓存大小字节
         */
        getCacheSizeBytes
    }

    /**
     * 获取应用专属文件，内部缓存路径下的指定文件
     * @param context 上下文
     * @param name 文件名
     * @return 文件路径
     */
    public static String getCacheDir(Context context, String name){
        String path = context.getCacheDir().getPath() + "/" + name;
        createCacheFile(path);
        return path + "/";
    }

    /**
     * 获取应用专属文件，内部路径下的指定文件
     * @param context 上下文
     * @param name 文件名
     * @return 文件路径
     */
    public static String getExternalCacheDir(Context context, String name){
        String path = context.getExternalCacheDir().getPath() + "/" + name;
        createCacheFile(path);
        return path + "/";
    }

    /**
     * 获取应用专属文件，内部路径下的指定文件
     * @param context 上下文
     * @param name 文件名
     * @return 文件路径
     */
    public static String getFilesDir(Context context, String name){
        String path = context.getFilesDir().getPath() + "/" + name;
        createCacheFile(path);
        return path + "/";
    }

    /**
     * 获取应用专属文件，外部缓存路径下的指定文件
     * @param context 上下文
     * @param name 文件名
     * @return 文件路径
     */
    public static String getExternalFilesDir(Context context, String name){
        String path = context.getExternalFilesDir(name).getPath();
        createCacheFile(path);
        return path + "/";
    }

    /**
     * 创建文件夹
     * @param path 文件路径
     */
    private static void createCacheFile(String path){
        File file = new File(path);
        if(!file.exists()){
            boolean isSuccess = file.mkdirs();
            if(isSuccess){
                Log.d(TAG,file + " 文件夹创建成功");
            }
        }
    }
}
