package cn.itcast.mobilesafe.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m3.adapter.BlackContactAdapter;
import cn.itcast.mobilesafe.m3.db.dao.BlackNumberDao;
import cn.itcast.mobilesafe.m3.entity.BlackContactInfo;
import cn.itcast.mobilesafe.m3.adapter.BlackContactAdapter.BlackContactCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by min123 on 2016/12/22.
 */
public class SecurityPhoneActivity extends Activity implements View.OnClickListener{
    private FrameLayout mHaveBlackNumber;
    private FrameLayout mNoBlackNumber;
    private ListView mListView;
    private BlackNumberDao dao;
    private int pagenumber=0;
    private int pagesize=15;
    private int totalNumber;
    private List<BlackContactInfo>pageBlackNumber=new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_securityphone);
        initView();
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(totalNumber!=dao.getTotalNumber()){
            if(dao.getTotalNumber()>0){
                mHaveBlackNumber.setVisibility(View.VISIBLE);
                mNoBlackNumber.setVisibility(View.GONE);
            }else{
                mHaveBlackNumber.setVisibility(View.GONE);
                mNoBlackNumber.setVisibility(View.VISIBLE);
            }
            pagenumber=0;
            pageBlackNumber.clear();
            pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber,pagesize));
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
        }
    }
    private  void fillData(){
        dao=new BlackNumberDao(SecurityPhoneActivity.this);
        totalNumber=dao.getTotalNumber();
        if(totalNumber==0){
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        }else if(totalNumber>0){
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            if(pageBlackNumber.size()>0){
                pageBlackNumber.clear();
            }
            pageBlackNumber.addAll(
                    dao.getPageBlackNumber(pagenumber,pagesize));
            if(adapter==null){
                adapter=new BlackContactAdapter(pageBlackNumber,
                        SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackContactCallBack() {
                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });
                mListView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_purple));
        ImageView mLeftImgv= (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("通讯卫士");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mHaveBlackNumber= (FrameLayout) findViewById(R.id.fl_haveblacknumber);
        mNoBlackNumber= (FrameLayout) findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.lv_blacknumbers);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int lastVisiblePosition=mListView.getLastVisiblePosition();
                        if(lastVisiblePosition==pageBlackNumber.size()-1){
                            pagenumber++;
                            if(pagenumber*pagesize>=totalNumber){
                                Toast.makeText(SecurityPhoneActivity.this,"没有更多数据了",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                pageBlackNumber.addAll(dao.getPageBlackNumber(
                                        pagenumber,pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_addblacknumber:
                startActivity(new Intent(this,AddBlackNumberActivity.class));
                break;
        }

    }
}
