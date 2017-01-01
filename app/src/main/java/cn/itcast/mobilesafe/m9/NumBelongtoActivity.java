package cn.itcast.mobilesafe.m9;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m9.db.dao.NumBelongtoDao;

/**
 * Created by asus on 2016/12/23.
 */
public class NumBelongtoActivity extends Activity implements View.OnClickListener{

    private EditText mNumET;
    private TextView mResultTV;
    private String dbName = "address.db";
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){

        };
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_numbelongto);
        initView();
        copyDB(dbName);
    }

    private void  initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv= (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("号码归属地查询");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.btn_searchnumbelongto).setOnClickListener(this);
        mNumET = (EditText) findViewById(R.id.et_num_numbelongto);
        mResultTV = (TextView) findViewById(R.id.tv_searchresult);

        mNumET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editable.toString().toString().trim();
                if (string.length() ==0){
                    mResultTV.setText("");
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.imgv_leftbtn:
              finish();
              break;
          case R.id.btn_searchnumbelongto:
              String phonenumber = mNumET.getText().toString().trim();
              if (!TextUtils.isEmpty(phonenumber)){
                  File file = new File(getFilesDir(),dbName);
                  if (!file.exists() || file.length() <= 0){
                      copyDB(dbName);
                  }
                  //查询数据库'
                  String location = NumBelongtoDao.getLocation(phonenumber);
                  mResultTV.setText("归属地："+location);
               }else {
                  Toast.makeText(this,"请输入需要查询的号码",Toast.LENGTH_SHORT).show();
              }
              break;
      }
    }

    private void  copyDB(final String dbName){
        new Thread(){
            public void run(){
                try{
                    File file = new File(getFilesDir(),dbName);
                    if (file.exists() && file.length() > 0){
                        Log.i("NumBelongtoActivity","数据库已存在");
                        return;
                    }
                    InputStream is = getAssets().open(dbName);
                    FileOutputStream fos = openFileOutput(dbName,MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1){
                       fos.write(buffer,0,len);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
}
