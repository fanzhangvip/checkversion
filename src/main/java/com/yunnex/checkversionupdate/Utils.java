package com.yunnex.checkversionupdate;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.File;
import java.util.List;

/**
 * Created by zero on 2018/5/8.
 */

public class Utils {

    public static int getVersionCode(final Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getProcessName(final Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am
                .getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return "";
    }


    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public static void makeDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 重启整个APP
     *
     * @param context
     * @param Delayed 延迟多少毫秒
     */
    public static void restartAPP(Context context, long Delayed) {

        /**开启一个新的服务，用来重启本APP*/
        Intent intent1 = new Intent(context, killSelfService.class);
        intent1.putExtra("PackageName", context.getPackageName());
        intent1.putExtra("Delayed", Delayed);
        context.startService(intent1);

        /**杀死整个进程**/
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /***重启整个APP*/
    public static void restartAPP(Context context) {
        restartAPP(context, 2000);
    }


    public static void deleteDir(File dir){
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(int i=0; i<files.length; i++) {
                deleteDir(files[i]);
            }
        }
        dir.delete();
    }

}
