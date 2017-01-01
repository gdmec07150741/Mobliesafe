package cn.itcast.mobilesafe.m2;


import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.itcast.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        initView();
    }
    private void initView(){
        ((RadioButton)findViewById(R.id.rb_first)).setChecked(true);
    }

    @Override
    public void showPre() {
        Toast.makeText(this,"当前页面已是第一页",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(Setup2Activity.class);

    }
}
