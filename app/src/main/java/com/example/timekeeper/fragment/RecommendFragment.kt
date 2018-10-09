package com.example.timekeeper.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.baidu.mapapi.search.poi.*
import com.example.timekeeper.R
import com.example.timekeeper.adapter.ReconmentAdapter
import com.example.timekeeper.util.Common
import kotlinx.android.synthetic.main.fragment_recomment.*
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by ZY on 2018/8/6.
 */
class RecommendFragment:Fragment(),AdapterView.OnItemSelectedListener,OnGetPoiSearchResultListener,OnGetGeoCoderResultListener,View.OnClickListener,AdapterView.OnItemClickListener{

    lateinit var arrayList:ArrayList<HashMap<String,String>>
    lateinit var dataList: ArrayList<HashMap<String,String>>
    lateinit var adapter:ReconmentAdapter

    lateinit var mPoiSearch: PoiSearch
    lateinit var mSearch:GeoCoder
    lateinit var mLatLng:LatLng

    val radius = 2000

    var keyWord = "酒店"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.fragment_recomment,null)
        //我创建活动
        arrayList = Common.getFragmentMy()
        println("listLen=${arrayList.size}")

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance()
        mSearch.setOnGetGeoCodeResultListener(this)

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance()
        mPoiSearch.setOnGetPoiSearchResultListener(this)

        dataList = ArrayList<HashMap<String,String>>()
        adapter = ReconmentAdapter(context!!, dataList)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //放在onCreateView中会空指针异常
        btn_hotel.setOnClickListener(this)
        btn_food.setOnClickListener(this)
        btn_happy.setOnClickListener(this)
        tv_reconment.movementMethod = ScrollingMovementMethod.getInstance()

        list_reconment.adapter = adapter
        list_reconment.setOnItemClickListener(this)

        //获取活动的名称
        val linkedList = LinkedList<String>()
        for (map in arrayList){
            println(map.toString())
            linkedList.add(map["actionName"]!!)
        }
        spinner_recommend.attachDataSource(linkedList)
        spinner_recommend.setOnItemSelectedListener(this)
        spinner_recommend.selectedIndex = 0

        println("arr:"+arrayList.toString())
        val map = arrayList[0]
        val stringBuilder = StringBuilder()
        stringBuilder.append("活动地点："+map["actionPlace"]+"\r\n")
        stringBuilder.append("活动介绍："+map["actionDescription"]+"\r\n")
        tv_reconment.text = stringBuilder.toString()
        //Geo搜索
        println("arr:"+map["actionPlace"])
        mSearch.geocode(GeoCodeOption().city("").address(map["actionPlace"]!!))
    }

    override fun onClick(v: View?) {
        when(v){
            btn_hotel->{
                dataList.clear()
                adapter.notifyDataSetChanged()
                keyWord = "酒店"
                startPoiSearch(mLatLng)
            }
            btn_food->{
                dataList.clear()
                adapter.notifyDataSetChanged()
                keyWord = "美食"
                startPoiSearch(mLatLng)
            }
            btn_happy->{
                dataList.clear()
                adapter.notifyDataSetChanged()
                keyWord = "娱乐"
                startPoiSearch(mLatLng)
            }
        }
    }

    //listview的点击事件
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("listPosition = "+position)
    }

   //spinner的选择事件
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val map = arrayList[position]
        val stringBuilder = StringBuilder()
        stringBuilder.append("活动地点："+map["actionPlace"]+"\r\n")
        stringBuilder.append("活动介绍："+map["actionDescription"]+"\r\n")
        tv_reconment.text = stringBuilder.toString()
        //Geo搜索
        mSearch.geocode(GeoCodeOption().city("").address(map["actionPlace"]))

    }


    //地理编码
    override fun onGetGeoCodeResult(result: GeoCodeResult?) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(activity, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show()
            return
        }
        mLatLng = LatLng(result.getLocation().latitude, result.getLocation().longitude)
        //开始pio搜索
        println("Latlng=${mLatLng.toString()}")
        adapter.setMyLatLng(mLatLng)
        startPoiSearch(mLatLng)
    }
    override fun onGetReverseGeoCodeResult(p0: ReverseGeoCodeResult?) {
    }

    //poi搜索
    private fun startPoiSearch(latLng: LatLng) {
        val searchOption = PoiNearbySearchOption().keyword(keyWord).sortType(PoiSortType.distance_from_near_to_far)
                .location(latLng).radius(radius)
        mPoiSearch.searchNearby(searchOption)
    }

    //poi结果
    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
    }
    override fun onGetPoiResult(result: PoiResult?) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(activity, "未找到结果", Toast.LENGTH_SHORT).show()
            return
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR){
            if (result.allPoi!=null){
                for (info in result.allPoi!!){
                    println("info:${info.name} ${info.address} ${info.phoneNum} ${info.location}")
                    val map = HashMap<String,String>()
                    map["name"]=info.name
                    map["address"]=info.address
                    map["phoneNum"]=info.phoneNum.split(",")[0]
                    map["distance"]="${info.location.latitude}_${info.location.longitude}"
                    dataList.add(map)
                }
                adapter.notifyDataSetChanged()
            }
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD){
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            println("PoiResult:"+result.toString())
        }
    }
    override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mPoiSearch.destroy()
        mSearch.destroy()

    }
}