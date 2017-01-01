package cn.itcast.mobilesafe.m3.entity;

/**
 * Created by min123 on 2016/12/22.
 */
public class BlackContactInfo {
    public String phoneNumber;
    public String contactName;
    public int mode;
    public String getModeString(int mode){
        switch (mode){
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话，短信拦截";
        }
      return "";
    }
}
