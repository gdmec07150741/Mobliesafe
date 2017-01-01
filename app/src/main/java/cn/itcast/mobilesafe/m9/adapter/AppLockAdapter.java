package cn.itcast.mobilesafe.m9.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.m9.entity.AppInfo;

/**
 * Created by asus on 2016/12/24.
 */
public class AppLockAdapter extends BaseAdapter{

    private List<AppInfo> appInfos;
    private Context context;

    public AppLockAdapter(List<AppInfo> appInfos,Context context){
        super();
        this.appInfos = appInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return appInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null && view instanceof RelativeLayout){
            holder = (ViewHolder)  view.getTag();
        }else {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_list_applock,null);
            holder.mAppIconImgv = (ImageView) view.findViewById(R.id.imgv_appicon);
            holder.mAppNameTV = (TextView) view.findViewById(R.id.tv_appname);
            holder.mLockIcon = (ImageView) view.findViewById(R.id.imgv_lock);
            view.setTag(holder);
        }

        final AppInfo appInfo = appInfos.get(i);
        holder.mAppIconImgv.setImageDrawable(appInfo.icon);
        holder.mAppNameTV.setText(appInfo.appName);
        if (appInfo.isLock){
            holder.mLockIcon.setBackgroundResource(R.drawable.applock_icon);
        }else {
            holder.mLockIcon.setBackgroundResource(R.drawable.appunlock_icon);
        }
        return view;
    }

    static class ViewHolder{
        TextView mAppNameTV;
        ImageView mAppIconImgv;
        ImageView mLockIcon;
    }
}
