package com.yunnex.checkversionupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yunnex.logoutputlib.output.VLogOutput;

/**
 * Created by zero on 2018/5/9.
 */

public class AppInstallReceiver extends BroadcastReceiver {
    private final String TAG = "checkupdate";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
            final String pkgName = Utils.getProcessName(context);
            final String packageName = intent.getData().getSchemeSpecificPart();
            VLogOutput.d(TAG, "pkgName " + pkgName);
            VLogOutput.d(TAG, "替换成功" + packageName);
            if (!TextUtils.equals(packageName, pkgName)) return;
            int code = Utils.getVersionCode(context);
            VLogOutput.d(TAG, "code " + code);
            Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            context.startActivity(LaunchIntent);
        }
    }
}
