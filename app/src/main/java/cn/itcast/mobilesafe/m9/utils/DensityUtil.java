package cn.itcast.mobilesafe.m9.utils;

import android.content.Context;

/**
 * Created by asus on 2016/12/22.
 */
public class DensityUtil {
    //dip转px
    public static int dip2px(Context context,float dpValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dpValue*scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) dpValue;
    }

    //px转dip
    public static  int px2dip(Context context,float pxValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue/scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) pxValue;
    }
}
