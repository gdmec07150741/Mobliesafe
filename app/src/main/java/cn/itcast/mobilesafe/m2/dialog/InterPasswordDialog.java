package cn.itcast.mobilesafe.m2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.itcast.mobilesafe.R;

/**
 * Created by min123 on 2016/12/21.
 */
public class InterPasswordDialog extends Dialog implements View.OnClickListener{
    private TextView mTitleTV;
    private EditText mInterET;
    private Button mOKBtn;
    private Button mCancelBtn;
    private MyCallBack myCallBack;
    private Context context;
    public InterPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
        this.context=context;
    }
    public void setCallBack(MyCallBack myCallBack){
        this.myCallBack=myCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        mTitleTV= (TextView) findViewById(R.id.tv_interpwd_title);
        mInterET= (EditText) findViewById(R.id.et_inter_password);
        mOKBtn= (Button) findViewById(R.id.btn_comfirm);
        mCancelBtn= (Button) findViewById(R.id.btn_dismiss);
        mOKBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }
    public void setTitle(String title){
        if(TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }

    }
    public String getPassword(){
        return mInterET.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_comfirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.cancle();
                break;
        }

    }
    public interface MyCallBack{
        void confirm();
        void cancle();
    }
}
