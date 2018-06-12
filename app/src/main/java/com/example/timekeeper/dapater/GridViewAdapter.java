package com.example.timekeeper.dapater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timekeeper.R;
import com.example.timekeeper.util.Common;

import java.util.ArrayList;

/**
 * Created by ZY on 2018/5/25.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> dataList;

    private ArrayList<Integer> noText;
    private String [] name = {"\\", "今天", "明天", "后天",
            "9:00", "", "", "",
            "15:00", "", "", "",
            "17:00", "", "", "",
            "19:00", "", "", ""};

    public GridViewAdapter(Context context, ArrayList<String> dataList) {
        this.context = context;
        this.dataList = dataList;
        Log.e(Common.TAG,"dataList:"+dataList.toString());
        //initdata();
    }

    private void initdata() {
        noText = new ArrayList();
        noText.add(0);
        noText.add(1);
        noText.add(2);
        noText.add(3);
        noText.add(4);
        noText.add(8);
        noText.add(12);
        noText.add(16);
        int j=0;
        for (int i=0;i<name.length;i++){
            if (!noText.contains(i) && !dataList.isEmpty()){
                if (dataList.size()>=j){
                    name[i]=dataList.get(j);
                }
                j++;
            }
        }
    }

    private class ViewHolder{
        TextView textView;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        String data = dataList.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.grid_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (data){
            case "0":viewHolder.textView.setBackgroundResource(
                    R.color.lightGray);
                break;
            case "1": viewHolder.textView.setBackgroundResource(
                    R.color.white);
                    break;
            case "2": viewHolder.textView.setBackgroundResource(
                    R.color.bar_deepgreen);
                break;
            case "3": viewHolder.textView.setBackgroundResource(
                    R.color.bar_lightgreen);
                break;
            case "4": viewHolder.textView.setBackgroundResource(
                    R.color.bar_orange);
                break;
            case "5": viewHolder.textView.setBackgroundResource(
                    R.color.bar_red);
                break;
            default: viewHolder.textView.setText(data);
        }
        return convertView;
    }
}
