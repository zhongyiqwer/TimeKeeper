package com.example.timekeeper

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.DateTimeHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by ZY on 2018/10/11.
 */
class ActionService :Service(){

    lateinit var notifyMap: HashMap<String,Long>
    lateinit var list :ArrayList<String>

    override fun onCreate() {
        super.onCreate()
        println("ActionService onCreate")
        notifyMap = HashMap()
        list = ArrayList()
    }

    //开启线程监控未完成活动，当距离开始时间小于半小时提醒用户
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("ActionService onStartCommand")
        Thread(Runnable {
            val noDoneList = Common.getFragmentNoDone()
            while (true){
                if (noDoneList.isNotEmpty()){
                    list.clear()
                    for (map in noDoneList){
                        getStartTime(map)
                    }
                    notification()
                }
                Thread.sleep(60*1000)
            }
        }).start()
        return START_STICKY
    }

    private fun notification() {
        if (list.isNotEmpty()){
            println("listSize=${list.size}")
            val intent = Intent()
            intent.action = "com.example.timekeeper.notify"
            intent.putStringArrayListExtra("actions",list)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun getStartTime(map:HashMap<String,String>) {
        val timeCompute = map["actionData"]!!
        val splits = timeCompute.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val data1 = splits[0]
        var parseLong = DateTimeHelper.daytoMillis(data1)

        val time = Integer.parseInt(splits[1])
        parseLong += (3600 * 1000 * time).toLong()
        val date = Date(parseLong)
        val format = DateTimeHelper.DATE_FORMAT_TILL_SECOND.format(date)
        //println("活动开始时间为=$format")
        val currentTimeMillis = System.currentTimeMillis()

        //活动时间小于半小时
        if (parseLong-currentTimeMillis<=1000*60*30 && parseLong-currentTimeMillis>0){
            val actionId = map["actionId"]!!
            if(!notifyMap.contains(actionId) || (currentTimeMillis-notifyMap[actionId]!!)>=1000*60*10){
                println("添加提醒")
                notifyMap[actionId] = currentTimeMillis
                list.add(actionId)
            }
        }
    }

    inner class LocalBinder:Binder(){
        fun getService():ActionService{
            return ActionService()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}