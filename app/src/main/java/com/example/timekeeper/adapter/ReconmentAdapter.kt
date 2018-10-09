package com.example.timekeeper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.example.timekeeper.R
import java.math.BigDecimal
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by ZY on 2018/8/6.
 */
class ReconmentAdapter:BaseAdapter {

    var context:Context
    var dataList: ArrayList<HashMap<String, String>>
    var mLatLng: LatLng?=null

    constructor(context: Context, dataList: ArrayList<HashMap<String, String>>) : super() {
        this.context = context
        this.dataList = dataList
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var viewHolder:ViewHolder?=null
        val map = dataList[position]
        if (view == null){
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.reconment_list_item_layout, parent, false)
            viewHolder.name = view.findViewById<TextView>(R.id.tv_list_name)
            viewHolder.address = view.findViewById<TextView>(R.id.tv_list_place)
            viewHolder.phoneNum = view.findViewById<TextView>(R.id.tv_list_phoneNum)
            viewHolder.distance = view.findViewById<TextView>(R.id.tv_list_distance)
            view.tag  = viewHolder
        }else{
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.name.text = map["name"]
        viewHolder.address.text = map["address"]
        if (map["phoneNum"]!!.isNotEmpty()){
            viewHolder.phoneNum.text = "电话："+map["phoneNum"]
        }
        var distanceStr = ""
        if (mLatLng!=null){
            val latLngs= map["distance"]!!.split("_")
            val distance = DistanceUtil.getDistance(mLatLng, LatLng(latLngs[0].toDouble(), latLngs[1].toDouble()))
            if (distance>1000){
                val double = BigDecimal(distance/1000).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                distanceStr = "距离活动地点${double}千米"
            }else{
                //val double = BigDecimal(distance).setScale(0, BigDecimal.ROUND_HALF_UP).toDouble()
                distanceStr = "距离活动地点${distance.toInt()}米"
            }
        }
        viewHolder.distance.text = distanceStr

        return view!!
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    inner class ViewHolder{
        lateinit var name:TextView
        lateinit var address:TextView
        lateinit var phoneNum:TextView
        lateinit var distance:TextView
    }

    fun setMyLatLng(latLng:LatLng){
        this.mLatLng = latLng
    }
}