package cn.itcast.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import cn.itcast.mobilesafe.m1.utils.MyUtils;
import cn.itcast.mobilesafe.m1.utils.VersionUpdateUtils;

/**
 * Created by asus on 2016/12/20.
 */
public class SplashActivity extends Activity {
    private TextView mVersionTV;
    private String mVersion;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mVersion = MyUtils.getVersion(getApplicationContext());
        initView();
        final VersionUpdateUtils updateUtils =new VersionUpdateUtils(mVersion,SplashActivity.this);
        new Thread(){
            public void run(){
                updateUtils.getCloudVersion();
            };
        }.start();
    }
    private void  initView(){
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号:"+mVersion);
    }
}
