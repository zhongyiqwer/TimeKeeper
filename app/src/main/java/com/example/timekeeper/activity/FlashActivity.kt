package com.example.timekeeper.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.WindowManager
import com.alibaba.fastjson.JSON
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.module_login_reg.login
import com.example.timekeeper.util.CheckPermissionUtils
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*


/**
 * Created by ZJX on 2018/5/3.
 */
class FlashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.flash_activity_layout)
        getScanResult()
        //进行一些初始化
        val timer = Timer()
        val task = object : TimerTask(){
            override fun run() {
                val permissions = CheckPermissionUtils.checkPermission(this@FlashActivity)
                if (permissions.size == 0){
                    setId()
                }else{
                    ActivityCompat.requestPermissions(this@FlashActivity, permissions, 100);
                }

            }

        }
        timer.schedule(task,1400)
    }

    private fun getScanResult(){
        val intent = intent
        val dataString = intent.dataString
        println("dataString:$dataString")
        val uri = intent.data
        println("uri:"+uri)
        if (uri!=null && uri.scheme=="m"){
            val parameter = uri.getQueryParameter("activityId")
            Common.selecTimeActivityId = parameter
            println("Id = "+Common.selecTimeActivityId)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100){
           if (permissions.size>0){
               for (grant in grantResults){
                   if (grant != PackageManager.PERMISSION_GRANTED){
                       Common.display(this@FlashActivity,"拒绝权限无法使用部分功能")
                       return
                   }
               }
               setId()
           }
        }
    }

    private fun setId(){
        val preferences = getSharedPreferences("config", 0)
        val userId = preferences.getString("userId", "")
        val password = preferences.getString("password", "")
        if (userId.isNotEmpty() && password.isNotEmpty()){
            autoLogin(userId,password)
        }else{
            val intent = Intent(this@FlashActivity, login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun autoLogin(userId:String,password:String){

        if (userId.isNotEmpty() && password.isNotEmpty()){
            val paramHM = HashMap<String, String>()
            paramHM.set("id",userId)
            paramHM.set("password",password)
            HttpHelper.sendOkHttpRequest(URL.LOGIN_URL,paramHM,object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    runOnUiThread {
                        val intent = Intent(this@FlashActivity, login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val body = response!!.body()!!.string()
                    println(body)
                    if ("success".equals(HttpHelper.getMessage(body))){
                        runOnUiThread {
                            val jsonObject = JSON.parseObject(body)
                            val userName = jsonObject.getString("userName")
                            Common.userName = userName

                            Common.userId = userId
                            if (Common.selecTimeActivityId!=null) {
                                val intent = Intent()
                                intent.setClass(this@FlashActivity, SelectTimeActivity::class.java)
                                intent.putExtra("activityId", Common.selecTimeActivityId)
                                Common.selecTimeActivityId = null
                                startActivity(intent)
                                finish()
                            }else{
                                val intent = Intent()
                                intent.setClass(this@FlashActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }else{
                        val intent = Intent(this@FlashActivity, login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }
    }
}