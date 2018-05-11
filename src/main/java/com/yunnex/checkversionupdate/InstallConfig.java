package com.yunnex.checkversionupdate;

/**
 * Created by lion on 2017/8/10.
 */

public class InstallConfig
{
	/**
	 * 1、静默安装发送服务
	 */
	// action
	public static final String ACTION_install = "com.yunnex.vpay.sdk.installAppHide.install";
	// key - 安装文件路径, value(String)
	public static final String KEY_filePath   = "filePath";
	// key - 包名(可为空), value(String)
	public static final String KEY_pkgName    = "pkgName";

	/**
	 * 2、静默安装结束回调接收广播
	 * <p>
	 * code value
	 * <p>
	 * //Installation success responsing code
	 * public static final int PACKAGE_INSTALL_SUCCESS = 0;
	 * //Default failed
	 * public static final int ERROR_PACKAGE_INSTALL_FAILED = -100;
	 * //Illegal APK type
	 */
	// action
	public static final String ACTION_callback = "com.yunnex.vpay.sdk.installAppHide.callback";
	// key - 安装状态, value(int)
	public static final String KEY_code        = "code";
	// key - 安装状态信息, value(String)
	public static final String KEY_message     = "message";
	// key - 安装应用包名, value(String)
	public static final String KEY_packageName = "packageName";

	/**
	 * 3、静默安装是否成功启动回调接收广播
	 */
	// action
	public static final String ACTION_callback_start = "com.yunnex.vpay.sdk.installAppHide.callback.start";
	// key - 启动状态, value(boolean)
	public static final String KEY_isSuccess = "isSuccess";
	// key - 安装应用包名, value(String) -- 同上复用
//	public static final String KEY_packageName = "packageName";
	// key - 安装文件路径, value(String) -- 同上复用
//	public static final String KEY_filePath   = "filePath";
}
