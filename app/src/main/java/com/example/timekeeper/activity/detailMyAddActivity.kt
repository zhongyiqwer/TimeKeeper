package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.dao.Action
import com.example.timekeeper.dapater.GridViewAdapter
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by Administrator on 2018/5/15.
 */
class detailMyAddActivity :BaseActivity(),View.OnClickListener{

    lateinit internal var select_grid : GridView
    lateinit internal var button : Button
    lateinit internal var tv_name : TextView
    lateinit internal var tv_time : TextView
    lateinit internal var tv_introduce : TextView
    lateinit internal var adapter : GridViewAdapter

    internal var timeStyle :Int?=0
    internal var actionId :String?="0"
    internal var creater :String?="0"

    var dataList = ArrayList<String>()
    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val noText = intArrayOf(0,1,2,3,4,8,12,16)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionId = intent.getStringExtra("activityId")
        getTimeStyle()
        setContentView(R.layout.detail_action_layout)
        initView()
        initData()
    }

    private fun initView() {
        select_grid = findViewById(R.id.gridView2)
        button = findViewById(R.id.button)
        button.setOnClickListener(this)
        tv_name = findViewById(R.id.tv_name)
        tv_time = findViewById(R.id.tv_time)
        tv_introduce = findViewById(R.id.tv_introduce)

        //adapter = GridViewAdapter(this@detailMyAddActivity,dataList)
        //select_grid.setAdapter(adapter)
    }

    private fun initData() {
        val map = HashMap<String,String>()
        map.put("activityId", this!!.actionId!!)
        HttpHelper.sendOkHttpRequest(URL.GET_ONE_Action,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {

            }
            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                if ("success".equals(HttpHelper.getMessage(body))){
                    //发序列化数据到Action
                    val activity = JSON.parseObject(body).getString("activity")
                    val action = JSON.parseObject(activity, Action::class.java)
                    println(action.toString())
                    runOnUiThread {
                        creater = action.creater
                        tv_name.text = action.activityName
                        tv_time.text = action.lastingTime
                        tv_introduce.text = action.description
                        val spiltString = Common.spiltString(action.time)
                        var data = Common.arrToArrarList(spiltString)
                        data.removeAt(12);
                        dataList = Common.initAdapterData(data);
                        adapter = GridViewAdapter(this@detailMyAddActivity,dataList)
                        select_grid.setAdapter(adapter)
                        //adapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }

    override fun onClick(v: View?) {
        if (v == button){
            val intent = Intent(this, actionPersoneActivity::class.java)
            println("crater:"+creater.toString())
            if (Common.userId!!.equals(creater)){
                intent.putExtra("isMy",true)
            }else {
                intent.putExtra("isMy", false)
            }
            intent.putExtra("actionId",actionId)
            startActivity(intent)
            //finish()
        }
    }

    private fun getTimeStyle() {
        timeStyle = 0
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}