package com.yunnex.checkversionupdate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yunnex.checkversionupdate.http.DownloadListner;
import com.yunnex.checkversionupdate.http.DownloadManager;
import com.yunnex.logoutputlib.output.VLogOutput;
import com.yunnex.utils.UtilsApp;
import com.yunnex.utils.UtilsMd5;

import java.io.File;

public class AppUpdateDialogActivity extends Activity implements DownloadListner {

    private static final String TAG = "checkupdate";

    private DownloadProgressButton updateBtn;
    private Button cancelBtn;
    private TextView msgTipTv;

    private UpdateBean updateBean;
    private String fileSavePath;
    private String pkgName;

    private DownloadManager downloadManager;
    private String url;

    private InstallBroadcastReceiver installBroadcastReceiver;
    private NetTypeReceiver netTypeReceiver;

    private AsyncTask asyncTask;

    @Override
    public void onDownloadProgress(float progress) {
        updateBtn.setState(DownloadProgressButton.STATE_DOWNLOADING);
        updateBtn.setProgressText("", progress * 100);
    }

    @Override
    public void onDownloadPause() {
        setUpdateBtnPause();
    }

    @Override
    public void onDownloadFinished(String apkPath) {
        doAuthenticate(apkPath);
    }

    private void doAuthenticate(final String apkPath) {

        asyncTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                updateBtn.setState(DownloadProgressButton.STATE_FINISH);
                updateBtn.setCurrentText("认证中");
            }

            @Override
            protected Boolean doInBackground(String[] params) {
                return authenticate(params[0]);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (!result) {
                    //认证失败,删除apk
                    Toast.makeText(AppUpdateDialogActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                    File deleteApk = new File(apkPath);
                    deleteApk.delete();
                    setUpdateBtnError();
                    return;
                }
                registerInstallReceiver();
                updateBtn.setCurrentText("安装中");
                install(apkPath);
            }
        }.execute(apkPath);
    }

    private boolean authenticate(String apkPath) {
        PackageInfo packageInfo = UtilsApp.getUninstallApkInfo(AppUpdateDialogActivity.this, apkPath);
        if (packageInfo == null) return false;
        if (!TextUtils.equals(pkgName, packageInfo.packageName)) return false;
        if (!UtilsMd5.checkMd5(updateBean.getMd5(), new File(apkPath))) return false;
        return true;
    }

    @Override
    public void onDownloadCancel() {

    }

    @Override
    public void onDownloadError(int errorCode, String error) {
        updateBtn.setState(DownloadProgressButton.STATE_ERROR);
        msgTipTv.setText(error);
        updateBtn.setCurrentText("下载出错了");
    }

    class NetTypeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isWifi(context)) {//不是wifi先暂停下载任务
                Toast.makeText(context, "当前网络不是wifi了", Toast.LENGTH_SHORT).show();
                downloadManager.pause(url);

            }
        }
    }

    private void registerNetReceiver() {
        netTypeReceiver = new NetTypeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netTypeReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (netTypeReceiver != null) {
            unregisterReceiver(netTypeReceiver);
            netTypeReceiver = null;
        }
    }

    class InstallBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (updateBean == null) return;
            final String action = intent.getAction();
            if (InstallConfig.ACTION_callback_start.equals(action)) {
                boolean isSuccess = intent.getBooleanExtra(InstallConfig.KEY_isSuccess, false);
                final String filePath = intent.getStringExtra(InstallConfig.KEY_filePath);
                if (isSuccess) return;
                //安装失败,删除apk
                Toast.makeText(AppUpdateDialogActivity.this, "静默安装失败", Toast.LENGTH_SHORT).show();
                File deleteApk = new File(filePath);
                deleteApk.delete();
                setUpdateBtnError();
            }
        }
    }

    private void setUpdateBtnError() {
        downloadManager.cancel(url);
        updateBtn.setState(DownloadProgressButton.STATE_NORMAL);
        updateBtn.setCurrentText("重新下载");
    }

    private void registerInstallReceiver() {
        installBroadcastReceiver = new InstallBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(InstallConfig.ACTION_callback_start);
        registerReceiver(installBroadcastReceiver, filter);
    }

    public static void startAppUpdateDialogActivity(final Context context, final UpdateBean updateBean, final String pkgName, final String fileSavePath) {
        Intent intent = new Intent(context, AppUpdateDialogActivity.class);
        intent.putExtra("updatebean", updateBean);
        intent.putExtra("pkgName", pkgName);
        intent.putExtra("fileSavePath", fileSavePath);
        context.startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        boolean canBack = (updateBean.isForceUpdate == 0);
        if (canBack) {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_app_update_dialog);
        initUI();
        setListener();
    }

    private void initUI() {
        msgTipTv = (TextView) findViewById(R.id.appupdate_msg);
        msgTipTv.setText("检测到当前应用有新版本\n是否去更新");
        updateBtn = (DownloadProgressButton) findViewById(R.id.appupdate_btn);
        updateBtn.setCurrentText("立即更新");
        updateBtn.setState(DownloadProgressButton.STATE_NORMAL);
        cancelBtn = (Button) findViewById(R.id.appupdate_cancel_btn);
    }

    private void setListener() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDownloading = downloadManager.isDownloading(url);
                if (isDownloading) {//正在下载状态,点击后进入暂停
                    downloadManager.pause(url);
                    return;
                }
                if (updateBtn.getState() != DownloadProgressButton.STATE_CONTINUE && !NetworkUtils.isWifi(AppUpdateDialogActivity.this)) {
                    setUpdateBtnContinue();
                    msgTipTv.setText("当前网络不是wifi,是否继续下载");
                    return;
                }
                if (!isDownloading) {//开始下载了
                    msgTipTv.setText("检测到当前应用有新版本\n是否去更新");
                    downloadManager.download(url);
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitIfForceUpdate();
            }
        });
    }

    private void exitIfForceUpdate() {
        if (updateBean.isForceUpdate == 1) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            downloadManager.cancel(url);
            downloadManager.remove(url);
            downloadManager = null;
            finish();
        }
    }

    private void setUpdateBtnPause() {
        updateBtn.setState(DownloadProgressButton.STATE_PAUSE);
        updateBtn.setCurrentText("继续");
    }

    private void setUpdateBtnContinue() {
        updateBtn.setState(DownloadProgressButton.STATE_CONTINUE);
        updateBtn.setCurrentText("继续");
    }

    private void install(String apkFile) {
        apkFile = testInstall();
        Intent intent = new Intent(InstallConfig.ACTION_install);
        intent.putExtra(InstallConfig.KEY_filePath, apkFile);
        intent.putExtra(InstallConfig.KEY_pkgName, updateBean.getPkgName());
        AppUpdateDialogActivity.this.startService(Utils.getExplicitIntent(AppUpdateDialogActivity.this, intent));
    }

    private String testInstall() {
        String apkPatha = "/sdcard/SmartCanteen-v50080076-appStore-debug.apk";
        File file = new File(apkPatha);
        VLogOutput.d(TAG, "apkPatha: " + file.getAbsolutePath());
        if (!file.exists()) {
            VLogOutput.d(TAG, "apk不存在");
        }
        return apkPatha;
    }

    private void initData() {
        updateBean = getIntent().getParcelableExtra("updatebean");
        pkgName = getIntent().getStringExtra("pkgName");
        fileSavePath = getIntent().getStringExtra("fileSavePath");
        VLogOutput.d(TAG, "initData updateBean: " + updateBean);
        VLogOutput.d(TAG, "initData pkgName: " + pkgName);
        VLogOutput.d(TAG, "initData fileSavePath: " + fileSavePath);
        if (updateBean == null || TextUtils.isEmpty(pkgName)) return;

        url = updateBean.getSrcUri();
        downloadManager = DownloadManager.getInstance();
        downloadManager.add(url, fileSavePath, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (installBroadcastReceiver != null) {
            unregisterReceiver(installBroadcastReceiver);
            installBroadcastReceiver = null;
        }
        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(false);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return true;
    }

}
