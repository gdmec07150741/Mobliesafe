package cn.itcast.mobilesafe.m9.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by asus on 2016/12/23.
 */
public class UIUtils {
    public static void showToast(final Activity content,final String msg){
        if ("main".equals(Thread.currentThread().getName())){
            Toast.makeText(content,msg,Toast.LENGTH_SHORT).show();
        }else {
            content.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(content,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
