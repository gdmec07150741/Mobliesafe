package cn.itcast.mobilesafe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
/**
 * Created by asus on 2016/12/20.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        correctSIM();
    }
    public void correctSIM(){
        //检查sim卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取防盗保护
        boolean protecting = sp.getBoolean("protecting",true);
        if (protecting){
            String bindsim = sp.getString("sim","");
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String realsim = tm.getSimSerialNumber();
            if (bindsim.equals(realsim)){
                Log.i("","sim卡未发生变化，还是你的手机");
            }else {
                Log.i("","SIM卡变化了");
                String safenumber = sp.getString("safephone","");
                if (!TextUtils.isEmpty(safenumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber,null,"你的亲友手机的SIM卡已经被更换！",null,null);
                }

            }
        }
    }
}
