package cn.itcast.mobilesafe.m10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m10.utils.SystemInfoUtils;
import cn.itcast.mobilesafe.m10.widget.SettingView;
import cn.itcast.mobilesafe.m9.service.AppLockService;

/**
 * Created by asus on 2016/12/24.
 */
public class SettingsActivity extends Activity implements View.OnClickListener,SettingView.OnCheckedStatusIsChanged {

    private SettingView mBlackNumSV;
    private SettingView mAppLockSV;
    private SharedPreferences mSP;
    private boolean running;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_blue));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("设置中心");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mBlackNumSV = (SettingView) findViewById(R.id.sv_blacknumber_set);
        mAppLockSV = (SettingView) findViewById(R.id.sv_applock_set);
        mBlackNumSV.setOnCheckedStatusIsChanged(this);
        mAppLockSV.setOnCheckedStatusIsChanged(this);
    }

    @Override
    protected void onStart() {

        //
        running = SystemInfoUtils.isServiceRunning(this,"cn.itcast.tyxing.m9.service.AppLockService");
        mAppLockSV.setChecked(running);
        mBlackNumSV.setChecked(mSP.getBoolean("BlackNumStatus",true));
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
       switch (view.getId()){
           case R.id.sv_blacknumber_set:
               saveStatus("BlackNumStatus",isChecked);
               break;
           case R.id.sv_applock_set:
               saveStatus("AppLockStatus",isChecked);
               if (isChecked){
                   intent = new Intent(this, AppLockService.class);
                   startService(intent);
               }else {
                   stopService(intent);
               }
               break;
       }
    }

    private void saveStatus(String Keyname,boolean isChecked){
        if (!TextUtils.isEmpty(Keyname)){
            SharedPreferences.Editor  editor = mSP.edit();
            editor.putBoolean(Keyname,isChecked);
            editor.commit();
        }
    }


}
