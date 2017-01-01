package cn.itcast.mobilesafe.m3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import cn.itcast.mobilesafe.m3.db.dao.BlackNumberDao;

/**
 * Created by min123 on 2016/12/22.
 */
public class InterceptSmsReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP=context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        boolean BlackNumStatus=mSP.getBoolean("BlackNumStatus",true);
        if(!BlackNumStatus){
            return;
        }
        BlackNumberDao dao=new BlackNumberDao(context);
        Object[] objs= (Object[]) intent.getExtras().get("pdus");
        for(Object obj:objs){
            SmsMessage smsMessage=SmsMessage.createFromPdu((byte[])obj);
            String sender=smsMessage.getOriginatingAddress();
            String body=smsMessage.getMessageBody();
            if(sender.startsWith("+86")){
                sender=sender.substring(3,sender.length());

            }
            int mode=dao.getBlackContactMode(sender);
            if(mode==2||mode==3){
                abortBroadcast();
            }
        }
    }
}
