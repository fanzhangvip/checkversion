/**
 * @author wangdong
 * @date 2014-12-4
 */

package com.yunnex.checkversionupdate;

import android.content.Context;

import com.yunnex.vpay.lib.oem.OemUtils;

public class AppStoreHttpUtils {
    // 新网络请求参数
    public static final String MODE_APPSTORE = "appStore";
    public static final String FUN_APP_DETAILS = "app_details"; // 获取 应用详情 地址

    // =======================================
    // Methods
    // =======================================
    public static String getAppStoreUrl(final Context context, final String function) {
        return OemUtils.getOemUrl(context, MODE_APPSTORE, function);
    }
}
