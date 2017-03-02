package cn.com.condenast.coverdemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by cysion.liu on 2016/6/21.
 * 文件操作工具类，主要包括：
 * 1--创建文件；
 * 2--删除文件和文件夹；
 * 3--获得文件或者文件夹的内容大小
 * 4--获得文件根目录
 */
public class TdFileUtils {

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 删除文件
     *
     * @param path 文件的路径
     */
    public static void delFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            file.delete();
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹的路径
     */
    public static void delFolder(String folderPath) {
        delFile(folderPath);
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete();
    }


    /**
     * 取得文件大小或者文件夹中所有文件大小
     *
     * @param f
     * @return
     */
    public static long getFileSize(File f) {
        long size = 0;
        if (f.isFile()) {
            size = f.length();
            return size;
        }
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;

    }

    /**
     * 文件存储根目录
     * 有SD卡时返回SD根目录，否则返回内部存储根目录
     *
     * @param context the context
     * @return file root
     */
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    /***
     * 转换文件大小单位(b/kb/mb/gb)
     ***/
    public static String FormatFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("0");
        String fileSizeString = "";
        fileSizeString = df.format((double) fileS / 1048576) + "M";
        return fileSizeString;
    }

    //根据字符串获得默认目标文件
    public static File getFile(String path) {
        int index = path.lastIndexOf("/");
        path = path.substring(index + 1);
        File newFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File destDir = new File(Environment.getExternalStorageDirectory() + "/cover_temp/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            newFile = new File(destDir, path);
        } else {
            File destDir = new File(Environment.getDownloadCacheDirectory() + "/cover_temp/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            newFile = new File(destDir, path);
        }
        return newFile;
    }

}
