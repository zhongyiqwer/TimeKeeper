package com.example.timekeeper.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.alibaba.fastjson.JSON
import com.example.timekeeper.R
import com.example.timekeeper.activity.ShareActivity
import com.example.timekeeper.activity.detailMyAddActivity
import com.example.timekeeper.dapater.actionPlayAdapter
import com.example.timekeeper.listener.ShareListener
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.action_play_adapter_item.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Administrator on 2018/5/17.
 */
class allActionFragment : Fragment() ,ShareListener,AdapterView.OnItemClickListener{

    lateinit internal var dataList: ArrayList<HashMap<String,String>>
    lateinit internal var listView:ListView
    lateinit internal var adapter:actionPlayAdapter

   companion object {
       fun newInstance():allActionFragment{
           return allActionFragment()
       }
   }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmant_all_action, null)
        var fragmentType = getArguments().get("fragmentType")
        println("type="+fragmentType)
        listView = view.findViewById<ListView>(R.id.list_view)
        if (fragmentType == "0"){
            dataList = ArrayList<HashMap<String,String>>()
            adapter = actionPlayAdapter(activity, dataList)
            adapter.setShareListener(this)
            initData()
            listView.setAdapter(adapter)
        }else if(fragmentType == "1"){
            dataList = Common.getFragmentNoDone()
            val adapter1 = actionPlayAdapter(activity, dataList)
            adapter1.setShareListener(this)
            listView.setAdapter(adapter1)
        }else if(fragmentType == "2"){
            dataList = Common.getFragmentMy()
            val adapter2 = actionPlayAdapter(activity, dataList)
            adapter2.setShareListener(this)
            listView.setAdapter(adapter2)
        }else if(fragmentType == "3"){
            dataList = Common.getFragmentData()
            val adapter3 = actionPlayAdapter(activity, dataList)
            adapter3.setShareListener(this)
            listView.setAdapter(adapter3)
        }
        listView.setOnItemClickListener(this)
        return view
    }

    private fun initData(){
        val map = HashMap<String, String>()
        println("userID"+Common.userId)
        map.put("userID",Common.userId)

        HttpHelper.sendOkHttpRequest(URL.GET_ALL_Actions,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {

            }
            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println(body)
                if("success".equals(HttpHelper.getMessage(body))){
                    activity.runOnUiThread {
                        val jsonObject = JSON.parseObject(body)
                        val array = jsonObject.getString("array")

                        val parseArray = JSON.parseArray(array)
                        val size = parseArray.size-1
                        for (i in 0..size){
                            val parseObject = parseArray.getJSONObject(i)
                            val hashMap = HashMap<String, String>()
                            hashMap.put("actionId",parseObject.getString("activityID"))
                            hashMap.put("actionName",parseObject.getString("activityName"))
                            hashMap.put("actionData",parseObject.getString("time_compute"))
                            hashMap.put("actionState",parseObject.getString("activityState"))
                            hashMap.put("actionCreater",parseObject.getString("activityCreater"))
                            dataList.add(hashMap)
                            adapter.notifyDataSetChanged()
                        }
                        Common.putFragmentData(dataList)
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(activity, detailMyAddActivity::class.java)
            //val actionId = adapter.getItem(position).get("actionId")
            val actionId = dataList[position].get("actionId")
            intent.putExtra("activityId",actionId)
            startActivity(intent)
            activity.finish()
        }*/
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("allFragment的position = "+position)
        val actionId = dataList[position].get("actionId")
        val intent = Intent(activity, detailMyAddActivity::class.java)
        //val actionId = adapter.getItem(position).get("actionId")
        intent.putExtra("activityId",actionId)
        startActivity(intent)
        activity.finish()
    }


    override fun btnShareCall(view: View?) {
        val position = view!!.getTag()
        val activityId = dataList[position as Int].get("actionId")
        println("allFragment的Id = "+activityId)
        val intent = Intent(activity, ShareActivity::class.java)
        intent.putExtra("activityId",activityId)
        startActivity(intent)
        //activity.finish()
    }
}