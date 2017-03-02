package cn.com.condenast.coverdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by cysion.liu on 2016/6/20.
 * app程序总体状况帮助类，目前包括
 * 1--获得目标包信息，
 * 2--判断app是否安装
 * 3--安装指定文件
 * 4--获得设备的uuid
 */
public class TdAppUtils {

    /**
     * 获取APK包信息
     * @param context 上下文，一般为Activity
     * @param packageName 包名
     * @return 包存在则返回true，否则返回false
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        if(TextUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("Package name cannot be null or empty !");
        }
        try {
            PackageInfo info=context.getPackageManager().getPackageInfo(packageName, 0);
            return info;
        } catch(PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 判断指定apk是否已经安装
     * @param context 上下文，一般为Activity
     * @param packageName 包名
     * @return 包存在则返回true，否则返回false
     */
    public static boolean isAppExists(Context context, String packageName) {
        if(null == packageName || "".equals(packageName)) {
            throw new IllegalArgumentException("Package name cannot be null or empty !");
        }
        try {
            ApplicationInfo info=
                    context.getPackageManager().getApplicationInfo(packageName, 0);
            return null != info;
        } catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 安装指定APK文件
     * @param activity Activity
     * @param apkFile APK文件对象
     */
    public static void install(Activity activity, File apkFile) {
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }


    /**
     * 获得设备的唯一的id
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm=((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE ));
        String DEVICE_ID = tm.getDeviceId();
        return DEVICE_ID;

    }
}
