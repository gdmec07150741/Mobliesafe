package cn.itcast.mobilesafe.m7;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m7.service.AutoKillProcessService;
import cn.itcast.mobilesafe.m7.utils.SystemInfoUtils;

/**
 * Created by asus on 2016/12/21.
 */
public class ProcessManagerSettingActivity extends Activity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{

    private ToggleButton mShowSysAppsTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences mSP;
    private boolean running;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_processmanagersetting);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ((TextView)findViewById(R.id.tv_title)).setText("进程管理设置");
        mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
        mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
        running = SystemInfoUtils.isServiceRunning(this,"cn.itcast.mobliesafe.m7.service.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
        initListener();
    }


    private void initListener(){
        mKillProcessTgb.setOnCheckedChangeListener(this);
        mShowSysAppsTgb.setOnCheckedChangeListener(this);
    }
    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.imgv_leftbtn:
              finish();
              break;
      }
    }

    private void saveStatus(String string,boolean isChecked){
        SharedPreferences.Editor edit = mSP.edit();
        edit.putBoolean(string,isChecked);
        edit.commit();
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.tgb_showsys_process:
                saveStatus("showSystemProcess",b);
                break;
            case R.id.tgb_killprocess_lockscreen:
                Intent service = new Intent(this,AutoKillProcessService.class);
                if (b){
                    startService(service) ;
                }else{
                    stopService(service);
                }
                break;
        }
    }
}
