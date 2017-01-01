package cn.itcast.mobilesafe.m4.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import cn.itcast.mobilesafe.m4.entity.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by min123 on 2016/12/23.
 */
public class AppInfoParser {
    public static List<AppInfo>getAppInfos(Context context){
        PackageManager pm=context.getPackageManager();
        List<PackageInfo>packageInfos=pm.getInstalledPackages(0);
        List<AppInfo>appInfos=new ArrayList<AppInfo>();
        for(PackageInfo packageInfo:packageInfos){
            AppInfo appInfo=new AppInfo();
            String packgename=packageInfo.packageName;
            appInfo.packageName=packgename;
            Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
            appInfo.icon=icon;
            String appname=packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.appName=appname;

            String apkpath=packageInfo.applicationInfo.sourceDir;
            appInfo.apkPath=apkpath;
            File file=new File(apkpath);
            long appSize=file.length();
            appInfo.appSize=appSize;

            int flags=packageInfo.applicationInfo.flags;
            if((ApplicationInfo.FLAG_EXTERNAL_STORAGE&flags)!=0){
                appInfo.isInRoom=false;
            }else{
                appInfo.isInRoom=true;
            }
            if((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
                appInfo.isUserApp=false;
            }else{
                appInfo.isUserApp=true;
            }
            appInfos.add(appInfo);
            appInfo=null;

        }
        return appInfos;
    }
}
