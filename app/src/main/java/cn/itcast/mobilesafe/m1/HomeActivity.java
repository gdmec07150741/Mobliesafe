package cn.itcast.mobilesafe.m1;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m1.adapter.HomeAdapter;
import cn.itcast.mobilesafe.m10.SettingsActivity;
import cn.itcast.mobilesafe.m2.LostFindActivity;
import cn.itcast.mobilesafe.m2.dialog.InterPasswordDialog;
import cn.itcast.mobilesafe.m2.dialog.SetUpPasswordDialog;
import cn.itcast.mobilesafe.m2.receiver.MyDeviceAdminReceiver;
import cn.itcast.mobilesafe.m2.utils.MD5Utils;
import cn.itcast.mobilesafe.m3.SecurityPhoneActivity;
import cn.itcast.mobilesafe.m4.AppManagerActivity;
import cn.itcast.mobilesafe.m5.VirusScanActivity;
import cn.itcast.mobilesafe.m6.CacheClearListActivity;
import cn.itcast.mobilesafe.m7.ProcessManagerActivity;
import cn.itcast.mobilesafe.m8.TrafficMonitoringActivity;
import cn.itcast.mobilesafe.m9.AdvancedToolsActivity;

/**
 * Created by asus on 2016/12/21.
 */
public class HomeActivity extends Activity {
    private GridView gv_home;
    private SharedPreferences msharedPreferences;
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //9个方法
              switch (i){
                    case 0:
                        if (isSetupPassword()){
                            showInterPswdDialog();
                        }else {
                            showSetUpPswdDialog();
                        }
                        break;
                   case 1:
                        startActivity(SecurityPhoneActivity.class);
                        break;
                    case 2:
                        startActivity(AppManagerActivity.class);
                        break;
                    case 3:
                        startActivity(VirusScanActivity.class);
                        break;
                    case 4:
                        startActivity(CacheClearListActivity.class);
                        break;
                    case 5:
                        startActivity(ProcessManagerActivity.class);
                        break;
                    case 6:
                        startActivity(TrafficMonitoringActivity.class);
                        break;
                     case 7:
                        startActivity(AdvancedToolsActivity.class);
                        break;
                   case 8:
                        startActivity(SettingsActivity.class);
                        break;
                }
            }
        });

        //获取设备管理员
        policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //需要实现“手机防盗功能”,才可以实现
      componentName = new ComponentName(this,MyDeviceAdminReceiver.class);

        boolean active = policyManager.isAdminActive(componentName);
        if (!active){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"获取超级管理员权限，用于远程锁屏和清理数据");
            startActivity(intent);
        }
        //  gv_home的监听最后一个}
    }

    private void showSetUpPswdDialog(){
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(HomeActivity.this);
        setUpPasswordDialog.setMyCallBack(new SetUpPasswordDialog.MyCallBack(){


            @Override
            public void ok() {
               String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();
                String affirmPwsd = setUpPasswordDialog.mAffirmET.getText().toString().trim();
                if (!TextUtils.isEmpty(firstPwsd) && !TextUtils.isEmpty(affirmPwsd)){
                    if (firstPwsd.equals(affirmPwsd)){
                        savePswd(affirmPwsd);
                        setUpPasswordDialog.dismiss();
                        showInterPswdDialog();
                    }else {
                        Toast.makeText(HomeActivity.this,"两次密码不一致！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HomeActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
               setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();

    }
     private void showInterPswdDialog(){
         final String password = getPassword();
         final InterPasswordDialog mInPswdDialog = new InterPasswordDialog(HomeActivity.this);
         mInPswdDialog.setCallBack(new InterPasswordDialog.MyCallBack(){

             @Override
             public void confirm() {
                 if (TextUtils.isEmpty(mInPswdDialog.getPassword())){
                     Toast.makeText(HomeActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                 } else if (password.equals(MD5Utils.encode(mInPswdDialog.getPassword()))) {
                     mInPswdDialog.dismiss();
                     startActivity(LostFindActivity.class);
                 }else {
                     mInPswdDialog.dismiss();
                     Toast.makeText(HomeActivity.this,"密码有误，请重新输入！",Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void cancle() {
                 mInPswdDialog.dismiss();

             }
         });
         mInPswdDialog.setCancelable(true);
         mInPswdDialog.show();
     }
    private void savePswd(String affirmPwsd){
          SharedPreferences.Editor editor= msharedPreferences.edit();
          editor.putString("PhoneAntiTheftPWD",MD5Utils.encode(affirmPwsd));
          editor.commit();
      }

    private String getPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)){
            return "";
        }
        return password;
    }

    private boolean isSetupPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)){
            return false;
        }
        return true;
    }

    //开启新的Activity不关闭自己
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);
    }
    //退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
