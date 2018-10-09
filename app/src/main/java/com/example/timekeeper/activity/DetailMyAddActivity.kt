package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.dao.Action
import com.example.timekeeper.adapter.GridViewAdapter
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.detail_action_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by Administrator on 2018/5/15.
 */
class DetailMyAddActivity :BaseActivity(),View.OnClickListener{

    lateinit internal var select_grid : GridView
    lateinit internal var button : Button
    lateinit internal var tv_name : TextView
    lateinit internal var tv_time : TextView
    lateinit internal var tv_introduce : TextView
    lateinit internal var adapter : GridViewAdapter

    internal var timeStyle :Int?=0
    internal var actionId :String?="0"
    internal var creator :String?="0"

    lateinit var dataList: ArrayList<String>
    lateinit var timedata: ArrayList<String>

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
        seekBar.isClickable = false
        dataList = ArrayList<String>()
        timedata = ArrayList<String>()

        tv_introduce.post {
            val ellipsisCount = tv_introduce.layout.getEllipsisCount(tv_introduce.layout.lineCount - 1)
            if (ellipsisCount>0){
                println("有省略")
                tv_introduce.maxHeight = resources.displayMetrics.heightPixels
            }else{
                println("无省略")
                tv_introduce.setSingleLine(true)
            }
        }

        var indroduce = true
        tv_introduce.setOnClickListener {
            if (indroduce){
                indroduce = false
                tv_introduce.ellipsize = null
                tv_introduce.setSingleLine(indroduce)
            }else{
                indroduce =true
                tv_introduce.ellipsize = TextUtils.TruncateAt.END
                tv_introduce.setSingleLine(indroduce)
            }
        }

        var palce = true
        tv_place.setOnClickListener {
            if (palce){
                palce = false
                tv_place.ellipsize = null
                tv_place.setSingleLine(palce)
            }else{
                palce =true
                tv_place.ellipsize = TextUtils.TruncateAt.END
                tv_place.setSingleLine(palce)
            }
        }
    }

    private fun initData() {
        val map = HashMap<String,String>()
        map.put("activityId", this!!.actionId!!)
        HttpHelper.sendOkHttpRequest(URL.GET_ONE_Action,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {

            }
            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println("detail:$body")
                if ("success".equals(HttpHelper.getMessage(body))){
                    //发序列化数据到Action
                    val activity = JSON.parseObject(body).getString("activity")
                    val action = JSON.parseObject(activity, Action::class.java)
                    println(action.toString())
                    runOnUiThread {
                        creator = action.creator
                        tv_name.text = action.activityName
                        tv_type.text = action.activityType
                        tv_num.text = action.activityNum
                        tv_time.text = action.lastingTime+"小时"
                        tv_place.text = action.activityPlace
                        tv_introduce.text = action.description

                        val activitySelectDate = action.activitySelectDate
                        val activityContinueTime = action.lastingTime

                        val spiltString = Common.spiltString(action.time)
                        var data = Common.arrToArrarList(spiltString)
                        dataList = data
                        timedata = Common.initAdapterData(activityContinueTime,activitySelectDate)
                        adapter = GridViewAdapter(this@DetailMyAddActivity,dataList,timedata)
                        select_grid.setAdapter(adapter)
                        //adapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }

    override fun onClick(v: View?) {
        if (v == button){
            val intent = Intent(this, ActionPersoneActivity::class.java)
            println("creator:"+creator.toString())
            if (Common.userId!!.equals(creator)){
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