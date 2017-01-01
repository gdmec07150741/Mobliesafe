package cn.itcast.mobilesafe.m8.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import cn.itcast.mobilesafe.m8.Utils.SystemInfoUtils;
import cn.itcast.mobilesafe.m8.service.TrafficMonitoringService;

/**
 * Created by min123 on 2016/12/26.
 */
public class BootCompleteReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!SystemInfoUtils.isServiceRunning(context,"com.example.min123." +
                "mobilesecurityguard.m8.service.TrafficMonitoringService")){
            Log.d("traffic service","turn on");
            context.startService(new Intent(context, TrafficMonitoringService.class));
        }
    }
}
