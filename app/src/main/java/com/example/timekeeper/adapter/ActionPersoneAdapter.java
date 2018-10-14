package com.example.timekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timekeeper.R;
import com.example.timekeeper.listener.LevelChange;
import com.example.timekeeper.util.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/19.
 */

public class ActionPersoneAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String,String>> mapList;

    private ArrayList<String> spinneData;

    private boolean isMy;

    private HashMap<Integer,Integer> lastLevel;

    private LevelChange levelChange;


    public ActionPersoneAdapter(Context context, ArrayList<HashMap<String,String>> mapList,boolean isMy) {
        this.context = context;
        this.mapList = mapList;
        this.isMy = isMy;
        initdata();
        lastLevel = new HashMap<>();
    }
    private class ViewHolder {
         ImageView imageView;
         TextView name;
         TextView level;
         Spinner spinner;
    }

    public void setLevelChangeListener(LevelChange listener){
        this.levelChange = listener;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        HashMap<String,String> map = mapList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.take_member_item_layout, parent, false); //加载布局
            holder = new ViewHolder();
            holder.imageView =  convertView.findViewById(R.id.imageView2);
            holder.name = convertView.findViewById(R.id.textView5);
            //holder.level = convertView.findViewById(R.id.textView7);
            holder.spinner = convertView.findViewById(R.id.spinner);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,spinneData);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter1);
            holder.spinner.setTag(position);
            if (isMy){
                holder.spinner.setOnItemSelectedListener(new ItemClickSelectListener(holder,position));
            }else {
                holder.spinner.setClickable(false);
                holder.spinner.setEnabled(false);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));

        /*if (isMy){
            if (map.get("userId").equals(Common.userId)){
                holder.name.setText("自己");
                holder.spinner.setSelection(2);
                lastLevel.put(position,2);
                return convertView;
            }else {
                holder.name.setText(map.get("userName"));
            }
        }else {*/
            if (map.get("userId").equals(Common.userId)){
                holder.name.setText("自己");
            }else {
                holder.name.setText(map.get("userName"));
            }
        //}

        holder.spinner.setSelection(Integer.parseInt(map.get("userLevel"))-1);
        lastLevel.put(position,Integer.parseInt(map.get("userLevel"))-1);

        return convertView;
    }

    private void initdata() {
        spinneData = new ArrayList<>();
        spinneData.add("低");
        spinneData.add("中");
        spinneData.add("高");
    }

    private int getSpinnerLevel(String level){
        return spinneData.indexOf(level);
    }

    private class ItemClickSelectListener implements AdapterView.OnItemSelectedListener{
        ViewHolder holder;
        int levelPositon;

        public ItemClickSelectListener(ViewHolder holder,int positon) {
            this.holder = holder;
            this.levelPositon = positon;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!lastLevel.isEmpty() && lastLevel.get(levelPositon)!= position) {
                holder.spinner.setSelection(position);
                //holder.level.setText(spinneData.get(position));
               //调整权限
                levelChange.userLevelChanghe(mapList.get(levelPositon).get("userId"),(position+1)+"");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
