package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
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
import java.util.ArrayList


/**
 * Created by Administrator on 2018/5/15.
 */
class selectTimeActivity :BaseActivity() ,View.OnClickListener,AdapterView.OnItemClickListener{

    internal var timeStyle :Int?=0

    lateinit internal var tv_name : TextView
    lateinit internal var tv_time : TextView
    lateinit internal var tv_introduce : TextView

    lateinit internal var have_time : ImageButton
    lateinit internal var no_time : ImageButton
    lateinit internal var no_select : ImageButton
    lateinit internal var confirm : Button
    lateinit internal var select_grid : GridView

    lateinit internal var adpater: GridViewAdapter

    lateinit var dataList : ArrayList<String>

    internal var select_type : Int?=0

    internal lateinit var activityId : String

    //val dataList = ArrayList<Map<String, String>>()
    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val noText = intArrayOf(0,1,2,3,4,8,12,16)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTimeStyle()
        setContentView(R.layout.select_time_activity)
        activityId = intent.getStringExtra("activityId")
        initView()
        initData()
    }

    override fun onClick(v: View?) {
        if(v == have_time){
            select_type = 0
        }
        if(v == no_time){
            select_type = 1
        }
        if(v == no_select){
        }
        if(v == confirm ){
            if (!gridTime()){
                Common.display(this,"不能为空")
            }else{
                val map = HashMap<String, String>()
                map.put("userId",Common.userId)
                map.put("activityId",this!!.activityId!!)
                map.put("time_campute",Common.getTimeString(flag))
                HttpHelper.sendOkHttpRequest(URL.TAKE_Action_Post,map,object :Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        runOnUiThread {
                            Common.display(this@selectTimeActivity,"加入失败")
                        }
                    }
                    override fun onResponse(call: Call?, response: Response?) {
                        if ("success".equals(HttpHelper.getMessage(response!!.body()!!.string()))){
                            runOnUiThread {
                                Common.display(this@selectTimeActivity,"加入成功")
                                val intent = Intent(this@selectTimeActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!noText.contains(position)){
            if (adpater.getItem(position) != "0"){
                println(flag[position].toString()+" "+select_type.toString())
                if (flag[position] == 0){
                    if (select_type == 0){
                        flag[position] =1
                        dataList[position] = "2"
                        //view!!.setBackgroundResource(R.color.lightgreen)
                    }else if (select_type == 1){
                        flag[position] =2
                        dataList[position] = "5"
                        //view!!.setBackgroundResource(R.color.light_red)
                    }
                }else if (flag[position] == 1){
                    if (select_type == 0){
                        flag[position] =0
                        dataList[position] = "1"
                        //view!!.setBackgroundResource(R.color.white)
                    }else if (select_type == 1){
                        flag[position] =2
                        dataList[position] = "5"
                        //view!!.setBackgroundResource(R.color.light_red)
                    }
                }else if(flag[position] == 2){
                    if (select_type == 0){
                        flag[position] =1
                        dataList[position] = "2"
                        //view!!.setBackgroundResource(R.color.lightgreen)
                    }else if (select_type == 1){
                        flag[position] =0
                        dataList[position] = "1"
                        //view!!.setBackgroundResource(R.color.white)
                    }
                }
                adpater.notifyDataSetChanged()
            }
        }
    }

    private fun initView() {
        tv_name = findViewById(R.id.tv_name)
        tv_time = findViewById(R.id.tv_time)
        tv_introduce = findViewById(R.id.tv_introduce)
        have_time = findViewById(R.id.have_time)
        no_time = findViewById(R.id.no_time)
        no_select = findViewById(R.id.no_select)
        confirm = findViewById(R.id.confirm)
        select_grid = findViewById(R.id.gridView2)

        have_time.setOnClickListener(this)
        no_time.setOnClickListener(this)
        no_select.setOnClickListener(this)
        confirm.setOnClickListener(this)

        select_grid.setOnItemClickListener(this)

    }

    private fun initData() {

        val map = HashMap<String, String>()
        //actionId = "794356ba-6d46-4cc2-8b1a-c7fd5198e746"
        println("selectTime的activityId = "+activityId)
        map.put("activityId",activityId!!)
        HttpHelper.sendOkHttpRequest(URL.GET_Select_Acion,map,object :Callback{
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
                        tv_name.text = action.activityName
                        tv_time.text = action.lastingTime
                        tv_introduce.text = action.description
                        val spiltString = Common.spiltString(action.start_time)
                        var data = Common.arrToArrarList(spiltString)
                        data.removeAt(12);
                        dataList = Common.initAdapterData(data);
                        adpater = GridViewAdapter(this@selectTimeActivity,dataList)
                        select_grid.setAdapter(adpater)
                        //adapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }

    private fun getTimeStyle() {
       timeStyle = 0
    }

    private fun gridTime() : Boolean{
        for (i in flag){
            if (i != 0){
                return true
            }
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}