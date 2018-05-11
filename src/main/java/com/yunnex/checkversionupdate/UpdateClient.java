package com.yunnex.checkversionupdate;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.yunnex.logoutputlib.output.VLogOutput;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;
import com.yunnex.vpay.lib.volley.VPayVolleyRequest;

/**
 * Created by zero on 2018/5/7.
 */

public class UpdateClient {

    private static final String TAG_1 = "checkupdate";

    private final Context context;
    private final String pkgName;
    private String fileSavePath;

    private UpdateClient(final Builder builder) {
        this.context = builder.context;
        this.fileSavePath = builder.fileSavePath;
        this.pkgName = builder.pkgName;
    }


    public void check() {
        AppDetailsRequest appDetailsRequest = new AppDetailsRequest();
        appDetailsRequest.setPkgName(pkgName);
        VPayVolleyRequest<?> request = new VPayUIRequestV2<AppDetailsResponse>(AppStoreHttpUtils.getAppStoreUrl(context, AppStoreHttpUtils.FUN_APP_DETAILS), appDetailsRequest, context, true) {
            @Override
            protected boolean onResponse(AppDetailsResponse appDetailsReponse) {
                switch (appDetailsReponse.getCode()) {
                    case HttpUtils.CODE_SUCCESS: {
                        if (appDetailsReponse == null) return false;
                        UpdateBean updateBean = appDetailsReponse.getAppRecommendedItem();
                        if (updateBean == null) return false;
                        int currVersionCode = Utils.getVersionCode(context);
                        VLogOutput.d(TAG_1, "currVersionCode: " + currVersionCode);
                        currVersionCode = 50080070;
                        if (updateBean.getVersionCode() > currVersionCode) {
                            //升级
                            AppUpdateDialogActivity.startAppUpdateDialogActivity(context, updateBean, pkgName, fileSavePath);
                        }
                        return true;
                    }
                    case HttpUtils.CODE_FAIL:
                        return true;
                    default:
                        break;
                }
                return false;
            }

            @Override
            protected void onResponseError(VolleyError error, Context context) {
                super.onResponseError(error, context);
                VLogOutput.d(TAG_1, error.getLocalizedMessage());
            }
        };
        request.setShouldCache(false);
        request.send();
    }

    public static final class Builder {

        private Context context;
        private String pkgName;
        private String fileSavePath;

        public Builder setContext(final Context context) {
            this.context = context;
            return this;
        }

        public Builder setFileSavePath(String fileSavePath) {
            this.fileSavePath = fileSavePath;
            return this;
        }

        public Builder setPkgName(String pkgName) {
            this.pkgName = pkgName;
            return this;
        }

        public final UpdateClient build() {
            if (context == null) throw new RuntimeException("context Can't be empty");
            if (TextUtils.isEmpty(pkgName)) throw new RuntimeException("pkgName Can't be empty");
            return new UpdateClient(this);
        }

    }
}
