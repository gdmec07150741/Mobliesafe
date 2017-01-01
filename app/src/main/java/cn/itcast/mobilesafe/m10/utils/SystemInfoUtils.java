package cn.itcast.mobilesafe.m10.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by asus on 2016/12/24.
 */
public class SystemInfoUtils {

    public static boolean isServiceRunning(Context context,String className){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(200);
        for (ActivityManager.RunningServiceInfo info:infos){
            String serviceClassName = info.service.getClassName();
            if (className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }
}
