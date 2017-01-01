package cn.itcast.mobilesafe.m7;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.mobilesafe.m7.adapter.ProcessManagerAdapter;
import cn.itcast.mobilesafe.m7.entity.TaskInfo;
import cn.itcast.mobilesafe.m7.utils.SystemInfoUtils;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m7.utils.TaskInfoParser;

/**
 * Created by asus on 2016/12/21.
 */
public class ProcessManagerActivity extends Activity implements View.OnClickListener {
    private TextView mRunProcessNum;
    private TextView mMemoryTV;
    private TextView mProcessNumTV;
    private ListView mListView;
    ProcessManagerAdapter adapter;
    private List<TaskInfo> runningTaskInfos;
    private List<TaskInfo> userTaskInfos = new ArrayList<TaskInfo>();
    private List<TaskInfo> sysTaskInfo = new ArrayList<TaskInfo>();
    private ActivityManager manager;
    private int runningPocessCount;
    private long totalMem;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_processmanager);
        initView();
        fillData();
    }

    @Override
    protected void onResume() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    private  void  initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgv.setImageResource(R.drawable.processmanager_setting_icon);
        mRightImgv.setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title)).setText("进程管理");
        mRunProcessNum = (TextView) findViewById(R.id.tv_runningprocess_num);
        mMemoryTV = (TextView) findViewById(R.id.tv_memory_processmanager);
        mProcessNumTV = (TextView) findViewById(R.id.tv_user_runningprocess);
        runningPocessCount = SystemInfoUtils.getRunningPocessCount(ProcessManagerActivity.this);
        mRunProcessNum.setText("运行中的进程："+runningPocessCount+"个");
        long totalAvailNum = SystemInfoUtils.getAvailMem(this);
        totalMem = SystemInfoUtils.getTotalMem();
        mMemoryTV.setText("可用/总内存:"+ Formatter.formatFileSize(this,totalAvailNum)+"/"+
        Formatter.formatFileSize(this,totalMem));
        mListView = (ListView) findViewById(R.id.lv_runningapps);
        initListener();
    }

    private void initListener(){
        findViewById(R.id.btn_selectall).setOnClickListener(this);
        findViewById(R.id.btn_select_inverse).setOnClickListener(this);
        findViewById(R.id.btn_cleanprocess).setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object = mListView.getItemAtPosition(i);
                if (object != null & object instanceof TaskInfo){
                    TaskInfo info = (TaskInfo) object;
                    if (info.packageName.equals(getPackageName())){
                        return;
                    }
                    info.isChecked = !info.isChecked;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                 if (i >= userTaskInfos.size()+1){
                     mProcessNumTV.setText("系统进程："+sysTaskInfo.size()+"个");
                 }else {
                     mProcessNumTV.setText("用户进程："+userTaskInfos.size()+"个");
                 }
            }
        });
    }

    private void fillData(){
        userTaskInfos.clear();
        sysTaskInfo.clear();
        new Thread(){
            public void run(){
                runningTaskInfos = TaskInfoParser.getRunningTaskInfos(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      for (TaskInfo taskInfo: runningTaskInfos){
                          if (taskInfo.isUserApp){
                              userTaskInfos.add(taskInfo);
                          }else {
                              sysTaskInfo.add(taskInfo);
                          }
                      }
                        if (adapter == null){
                            adapter = new ProcessManagerAdapter(
                                    getApplicationContext(),userTaskInfos,sysTaskInfo
                            );
                            mListView.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                        if (userTaskInfos.size()>0){
                            mProcessNumTV.setText("用户进程："+userTaskInfos.size()+"个");
                        }else {
                            mProcessNumTV.setText("系统进程："+sysTaskInfo.size()+"个");
                        }
                    }
                });
            };
        }.start();
    }
    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.imgv_leftbtn:
              finish();
              break;
          case R.id.imgv_rightbtn:
              startActivity(new Intent(this,ProcessManagerSettingActivity.class));
              break;
          case R.id.btn_selectall:
              selectAll();
              break;
          case R.id.btn_select_inverse:
              inverse();
              break;
          case R.id.btn_cleanprocess:
              cleanProcess();
              break;
      }
    }

    private void cleanProcess(){
        manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int count = 0;
        long saveMemory = 0;
        List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
        for(TaskInfo info:userTaskInfos){
            if (info.isChecked){
                count++;
                saveMemory += info.appMemory;
                manager.killBackgroundProcesses(info.packageName);
                killedTaskInfos.add(info);
            }
        }
        for (TaskInfo info :sysTaskInfo){
            if (info.isChecked) {
                count++;
                saveMemory += info.appMemory;
                manager.killBackgroundProcesses(info.packageName);
                killedTaskInfos.add(info);
            }
        }
        for (TaskInfo info : killedTaskInfos){
            if (info.isUserApp){
                userTaskInfos.remove(info);
            }else {
                sysTaskInfo.remove(info);
            }
        }
        runningPocessCount -= count;
        mRunProcessNum.setText("运行中的进程："+runningPocessCount+"个");
        mMemoryTV.setText("可用/总内存："+Formatter.formatFileSize(this,SystemInfoUtils.getAvailMem(this))+"/"+
        Formatter.formatFileSize(this,totalMem));
        Toast.makeText(this,"清理了"+count+"个进程，释放了"+Formatter.formatFileSize(this,saveMemory)+"内存",Toast.LENGTH_SHORT).show();
        mProcessNumTV.setText("用户进程:"+userTaskInfos.size()+"个");
        adapter.notifyDataSetChanged();
    }

    private void inverse(){
        for (TaskInfo taskInfo: userTaskInfos){
            if (taskInfo.packageName.equals(getPackageName())){
                continue;
            }
            boolean checked = taskInfo.isChecked;
            taskInfo.isChecked = !checked;
        }
        for (TaskInfo taskInfo:sysTaskInfo){
            boolean checked = taskInfo.isChecked;
            taskInfo.isChecked = !checked;
        }
        adapter.notifyDataSetChanged();
    }

    private  void selectAll(){
      for (TaskInfo taskInfo : userTaskInfos){
          if (taskInfo.packageName.equals(getPackageName())){
              continue;
          }
          taskInfo.isChecked = true;
      }
        for (TaskInfo taskInfo:sysTaskInfo){
            taskInfo.isChecked = true;
        }
        adapter.notifyDataSetChanged();
    }
}
