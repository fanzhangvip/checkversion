package com.yunnex.checkversionupdate.http;

import java.io.File;

/**
 * Created by zero on 2018/5/8.
 */

public interface DownloadListner {

    int ERROR_NOT_206 = 1;
    int ERROR_RESPONSE_FAIL = 2;

    void onDownloadProgress(float progress);

    void onDownloadPause();

    void onDownloadFinished(String apkPath);

    void onDownloadCancel();

    void onDownloadError(int errorCode, String error);
}
