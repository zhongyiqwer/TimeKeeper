package com.example.timekeeper

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.example.timekeeper.util.CrashHandler
import com.uuzuche.lib_zxing.activity.ZXingLibrary

/**
 * Created by ZY on 2018/6/10.
 */
class MyApp :Application(){
    override fun onCreate() {
        super.onCreate()
        //未捕获异常处理
        //CrashHandler.getInstance().init(applicationContext)
        //百度地图
        SDKInitializer.initialize(applicationContext)
        SDKInitializer.setCoordType(CoordType.BD09LL)
        //二维码扫描
        ZXingLibrary.initDisplayOpinion(applicationContext)
    }
}