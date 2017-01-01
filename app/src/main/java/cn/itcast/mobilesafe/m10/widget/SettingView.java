package cn.itcast.mobilesafe.m10.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.itcast.mobilesafe.R;

/**
 * Created by asus on 2016/12/24.
 */
public class SettingView extends RelativeLayout {

    private String setTitle = "";
    private String status_on = "";
    private String status_off="";
    private TextView mSettingTitleTV;
    private TextView mSettingStatusTV;
    private ToggleButton mToggleBtn;
    private boolean isChecked;
    private OnCheckedStatusIsChanged onCheckedStatusIsChanged;

    public SettingView(Context context) {
        super(context);
        init(context);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingView);
        setTitle = mTypedArray.getString(R.styleable.SettingView_settitle);
        status_on = mTypedArray.getString(R.styleable.SettingView_status_on);
        status_off = mTypedArray.getString(R.styleable.SettingView_status_off);
        isChecked= mTypedArray.getBoolean(R.styleable.SettingView_status_ischecked,false);
        mTypedArray.recycle();
        init(context);
        setStatus(status_on,status_off,isChecked);
    }

    private void  init (Context context){
        View view = View.inflate(context,R.layout.ui_settings_view,null);
        this.addView(view);
        mSettingStatusTV = (TextView) findViewById(R.id.tv_setting_status);
        mSettingTitleTV = (TextView) findViewById(R.id.tv_setting_title);
        mToggleBtn = (ToggleButton) findViewById(R.id.toggle_setting_status);
        mSettingTitleTV.setText(setTitle);
        mToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setChecked(isChecked);
                onCheckedStatusIsChanged.onCheckedChanged(SettingView.this,isChecked);
            }
        });
    }

    public void setChecked(boolean checked){
        mToggleBtn.setChecked(checked);
        isChecked = checked;
        if (checked){
            if (!TextUtils.isEmpty(status_on)){
                mSettingStatusTV.setText(status_on);
            }
        }else {
            if (!TextUtils.isEmpty(status_off)){
                mSettingStatusTV.setText(status_off);
            }
        }
    }

    public void setStatus(String status_on,String status_off,boolean checked){
        if (checked){
            mSettingStatusTV.setText(status_on);
        }else {
            mSettingStatusTV.setText(status_off);
        }
        mToggleBtn.setChecked(checked);
    }

    public void setOnCheckedStatusIsChanged(OnCheckedStatusIsChanged onCheckedStatusIsChanged){
        this.onCheckedStatusIsChanged = onCheckedStatusIsChanged;
    }

    public interface OnCheckedStatusIsChanged{
        void onCheckedChanged(View view,boolean isChecked);
    }
}
