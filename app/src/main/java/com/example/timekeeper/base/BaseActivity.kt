package com.example.timekeeper.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by ZJX on 2018/5/4.
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val mActivitys = ArrayList<Activity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitys.add(this)
    }

    fun KillAllActivitys(){
       for (activity in mActivitys){
           activity.finish()
       }
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivitys.remove(this)
    }
}