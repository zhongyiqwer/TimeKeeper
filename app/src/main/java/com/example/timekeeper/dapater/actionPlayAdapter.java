package com.example.timekeeper.dapater;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timekeeper.MyApp;
import com.example.timekeeper.R;
import com.example.timekeeper.activity.ShareActivity;
import com.example.timekeeper.listener.ShareListener;
import com.example.timekeeper.util.Common;
import com.example.timekeeper.util.DateTimeHelper;

import java.text.SimpleDateFormat;
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
        initdata();
    }

    private void initdata() {
        data = new String[4][3];
        for (int i=0;i<4;i++){
            for (int j=0; j<3;j++){
                if (i == 0){
                    data[i][j] = "9:00 - 11:00";
                }else if (i == 1){
                    data[i][j] = "15:00 - 17:00";
                }else if (i==2){
                    data[i][j] = "17:00 - 19:00";
                }else {
                    data[i][j] = "19:00 - 21:00";
                }
            }
        }
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

        if (!map.isEmpty() && map != null && map.size()>0){
            String timeCompute = map.get("actionData");
            String[] splits = timeCompute.split("_");
            String data1 = splits[0];
            long parseLong = Long.parseLong(data1);

            String realTime = "";

            int time = Integer.parseInt(splits[1])-1;
            int mode = time % 3;
            int shang = time / 3;

            if (mode == 2){
                parseLong += 2*24*60*60*1000;
            }else if (mode ==1){
                parseLong += 24*60*60*1000;
            }

            Date date = new Date(parseLong);
            String format = DateTimeHelper.DATE_TILL_DAY_CURRENT_YEAR.format(date);

            String[] datas = format.split("-");
            realTime = data[shang][mode];

            viewHolder.actionName.setText(map.get("actionName"));
            viewHolder.actionData.setText(datas[0]+"月"+datas[1]+"日");
            viewHolder.actionTime.setText(realTime);
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
