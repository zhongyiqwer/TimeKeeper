package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.dapater.ActionPersoneAdapter
import com.example.timekeeper.listener.LevelChange
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by Administrator on 2018/5/18.
 */
class actionPersoneActivity : BaseActivity() ,AdapterView.OnItemClickListener,LevelChange{

    lateinit internal var listView: ListView
    lateinit internal var textView3: TextView
    lateinit internal var name: TextView
    lateinit internal var image: ImageView

    lateinit internal var adpater: ActionPersoneAdapter

    internal var actionId :String?="0"
    internal var isMy :Boolean?=false

    lateinit internal var dataList: ArrayList<HashMap<String, String>>

     lateinit var userLevel: HashMap<String, String>

     lateinit var userIdList: JSONArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionId = intent.getStringExtra("actionId")
        isMy = intent.getBooleanExtra("isMy",false)
        setContentView(R.layout.take_member_layout)
        initview()
        setList()
        initDate()
    }

    private fun initDate() {
      /*  //假数据
        for (i in 1 .. 20){
            val hashMap = HashMap<String, String>()
            hashMap.put("image","www.baidu.com"+i)
            hashMap.put("name","zhangsan"+i)
            hashMap.put("level","中")
            dataList.add(hashMap)
        }*/
        val map = HashMap<String, String>()
        map.put("activityId",this!!.actionId!!)
        HttpHelper.sendOkHttpRequest(URL.GET_ONE_Action_Person,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println(body)
                if ("success".equals(HttpHelper.getMessage(body))){
                    val jsonObject = JSON.parseObject(body)
                    val array = jsonObject.getString("array")

                    val parseArray = JSON.parseArray(array)
                    val size = parseArray.size-1
                    for (i in 0..size){
                        val parseObject = parseArray.getJSONObject(i)
                        val hashMap = HashMap<String, String>()
                        hashMap.put("userId",parseObject.getString("userId"))
                        hashMap.put("userName",parseObject.getString("userName"))
                        hashMap.put("userLevel",parseObject.getString("userLevel"))
                        dataList.add(hashMap)
                    }
                    runOnUiThread {
                        adpater.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    private fun setList() {
        dataList = ArrayList<HashMap<String, String>>()
        adpater = ActionPersoneAdapter(this, dataList,isMy!!)
        listView.setAdapter(adpater)
        listView.setOnItemClickListener(this)

        adpater.setLevelChangeListener(this)

        userLevel = LinkedHashMap<String, String>()
        userLevel.put("activityId",actionId!!)

        userIdList = JSONArray()
    }


    private fun initview() {
        listView = findViewById(R.id.listView)
        textView3 = findViewById(R.id.textView3)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            println("进入back")
            if (userLevel.size>1 && userIdList.size>0){
                //val jsonString = JSON.toJSONString(userIdList)
                val jsonString = JSONArray.toJSONString(userIdList)
                userLevel.put("userIds",jsonString)
                postLevelChange()
                /*val intent = Intent(this, detailMyAddActivity::class.java)
                startActivity(intent)
                finish()*/
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun postLevelChange() {
        HttpHelper.sendOkHttpRequest(URL.Change_Level,userLevel, object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Common.display(this@actionPersoneActivity,"更改权限失败")
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                if("success".equals(HttpHelper.getMessage(response!!.body()!!.string()))){
                    runOnUiThread {
                        Common.display(this@actionPersoneActivity,"更改权限成功")
                        val intent = Intent(this@actionPersoneActivity, detailMyAddActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        })
    }

    override fun userLevelChanghe(userId: String?, level: String) {
        userLevel.put(userId!!,level!!)
        userIdList.add(userId)
        println(userId+" : "+level)
    }


}