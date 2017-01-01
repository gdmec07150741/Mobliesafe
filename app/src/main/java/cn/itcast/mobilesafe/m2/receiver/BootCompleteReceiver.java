package cn.itcast.mobilesafe.m2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.itcast.mobilesafe.App;

/**
 * Created by min123 on 2016/12/21.
 */
public class BootCompleteReceiver extends BroadcastReceiver{
    private  static final String TAG = BootCompleteReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ((App)context.getApplicationContext()).correctSIM();
    }
}
