package com.yunnex.checkversionupdate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zero on 2018/5/4.
 */

public class UpdateBean implements Parcelable {
    // --应用版本号
    private int versionCode;
    //是否强制更新 0 不强制更新 1 强制,必须给
    public int isForceUpdate = 0;
    // 应用下载地址
    private String srcUri;
    // 包名
    private String pkgName;
    // 应用md5值
    private String md5;
    // 应用名
    private String appName;

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public void setSrcUri(String srcUri) {
        this.srcUri = srcUri;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppSlogan() {
        return appSlogan;
    }

    public void setAppSlogan(String appSlogan) {
        this.appSlogan = appSlogan;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(String appDetails) {
        this.appDetails = appDetails;
    }

    // --应用版本名
    private String versionName;
    // 应用图标uri
    private String appIcon;
    // 应用标语
    private String appSlogan;
    // 应用大小
    private String appSize;
    // 应用简介
    private String appInfo;

    // 应用版本详情 相关
    // --开发者
    private String developer;
    // --更新日期
    private String updateDate;
    // --更新日志
    private String updateLog;
    // --应用介绍
    private String appDetails;

    public String getMd5() {
        return md5;
    }

    public String getPkgName() {
        return pkgName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public int getIsForceUpdate() {
        return isForceUpdate;
    }

    public String getSrcUri() {
        return srcUri;
    }

    public String toString() {
        return "UpdateBean{" +
                "versionCode=" + versionCode +
                ", isForceUpdate=" + isForceUpdate +
                ", srcUri='" + srcUri + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", md5='" + md5 + '\'' +
                ", appName='" + appName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", appSlogan='" + appSlogan + '\'' +
                ", appSize='" + appSize + '\'' +
                ", appInfo='" + appInfo + '\'' +
                ", developer='" + developer + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateLog='" + updateLog + '\'' +
                ", appDetails='" + appDetails + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.versionCode);
        dest.writeInt(this.isForceUpdate);
        dest.writeString(this.srcUri);
        dest.writeString(this.pkgName);
        dest.writeString(this.md5);
        dest.writeString(this.appName);
        dest.writeString(this.versionName);
        dest.writeString(this.appIcon);
        dest.writeString(this.appSlogan);
        dest.writeString(this.appSize);
        dest.writeString(this.appInfo);
        dest.writeString(this.developer);
        dest.writeString(this.updateDate);
        dest.writeString(this.updateLog);
        dest.writeString(this.appDetails);
    }

    public UpdateBean() {
    }

    protected UpdateBean(Parcel in) {
        this.versionCode = in.readInt();
        this.isForceUpdate = in.readInt();
        this.srcUri = in.readString();
        this.pkgName = in.readString();
        this.md5 = in.readString();
        this.appName = in.readString();
        this.versionName = in.readString();
        this.appIcon = in.readString();
        this.appSlogan = in.readString();
        this.appSize = in.readString();
        this.appInfo = in.readString();
        this.developer = in.readString();
        this.updateDate = in.readString();
        this.updateLog = in.readString();
        this.appDetails = in.readString();
    }

    public static final Parcelable.Creator<UpdateBean> CREATOR = new Parcelable.Creator<UpdateBean>() {
        @Override
        public UpdateBean createFromParcel(Parcel source) {
            return new UpdateBean(source);
        }

        @Override
        public UpdateBean[] newArray(int size) {
            return new UpdateBean[size];
        }
    };
}
