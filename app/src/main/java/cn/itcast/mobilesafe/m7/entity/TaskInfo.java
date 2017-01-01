package cn.itcast.mobilesafe.m7.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by asus on 2016/12/21.
 */
public class TaskInfo {
    public String appName;
    public long appMemory;
    //程序是否被选中
    public boolean isChecked;
    public Drawable appIcon;
    public boolean isUserApp;
    public String packageName;
}
