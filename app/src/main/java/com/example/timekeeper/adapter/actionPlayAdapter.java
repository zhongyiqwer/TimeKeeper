package com.example.timekeeper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timekeeper.R;
import com.example.timekeeper.listener.ShareListener;
import com.example.timekeeper.util.Common;
import com.example.timekeeper.util.DateTimeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZY on 2018/5/23.
 */

public class actionPlayAdapter extends BaseAdapter implements View.OnClickListener{


    private Context context;
    private List<HashMap<String,String>> dataList;
    private String[][] data;
    private ShareListener shareListener;


    public actionPlayAdapter(Context context,ArrayList<HashMap<String,String>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    private class ViewHolder{
        TextView actionName;
        TextView actionData;
        TextView actionTime;
        ImageButton actionShare;
    }

    public void setShareListener(ShareListener shareListener){
        this.shareListener = shareListener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public HashMap<String,String> getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        HashMap<String,String> map = dataList.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.action_play_adapter_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.actionName = convertView.findViewById(R.id.action_name);
            viewHolder.actionData = convertView.findViewById(R.id.action_data);
            viewHolder.actionTime = convertView.findViewById(R.id.action_time);
            viewHolder.actionShare = convertView.findViewById(R.id.btn_share);
            viewHolder.actionShare.setOnClickListener(this);
            viewHolder.actionShare.setTag(position);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.actionShare.setOnClickListener(this);
        //viewHolder.actionShare.setOnClickListener(new MyShareListener(map.get("actionId")));
        //viewHolder.actionShare.setTag(position);

        //Log.e(Common.TAG," map.size="+map.size());
        if (!map.isEmpty() && map != null && map.get("actionData")!=null){
            String continueTime = map.get("actionContinueTime");
            String actionSelectDate = map.get("actionSelectDate");
            ArrayList<String> arrayList = Common.initAdapterData(continueTime, actionSelectDate);

            String timeCompute = map.get("actionData");
            //Log.e(Common.TAG," timeCompute="+timeCompute);
            String[] splits = timeCompute.split("_");
            String data1 = splits[0];
            long parseLong = DateTimeHelper.daytoMillis(data1);

            int mode;
            int time = Integer.parseInt(splits[1]);
            if (Double.parseDouble(continueTime)!=24.0){
                mode = time/24;
            }else {
                mode = time;
            }

            parseLong += 24*3600*1000*mode;

            Date date = new Date(parseLong);
            String format = DateTimeHelper.DATE_TILL_DAY_CURRENT_YEAR.format(date);

            String[] datas = format.split("-");

            viewHolder.actionName.setText(map.get("actionName"));
            viewHolder.actionData.setText(datas[0]+"月"+datas[1]+"日");
            viewHolder.actionTime.setText(arrayList.get(time));
        }

        return convertView;
    }

    /*private class MyShareListener implements View.OnClickListener {

        private String activityId;

        public MyShareListener(String activityId) {
            this.activityId = activityId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ShareActivity.class);
            intent.putExtra("activityId",activityId);
            context.startActivity(intent);
        }
    }*/

    @Override
    public void onClick(View v) {
        shareListener.btnShareCall(v);
    }
}
