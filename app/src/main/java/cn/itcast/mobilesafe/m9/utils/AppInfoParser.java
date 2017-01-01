package cn.itcast.mobilesafe.m9.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobilesafe.m9.entity.AppInfo;

/**
 * Created by asus on 2016/12/24.
 */
public class AppInfoParser {

    public static List<AppInfo> getAppInfos(Context context){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packInfo:packInfos){
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
           appinfo. packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
             appinfo.icon =icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;

            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            appInfos.add(appinfo);
            appinfo = null;
        }
        return appInfos;
    }
}
