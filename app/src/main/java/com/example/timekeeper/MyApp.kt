package com.example.timekeeper

import android.app.Application
import com.uuzuche.lib_zxing.activity.ZXingLibrary

/**
 * Created by ZY on 2018/6/10.
 */
class MyApp :Application(){
    override fun onCreate() {
        super.onCreate()
        ZXingLibrary.initDisplayOpinion(this)
    }
}