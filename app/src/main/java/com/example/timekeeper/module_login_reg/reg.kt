package com.example.timekeeper.module_login_reg

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.EncodeAndDecode
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.login_layout.*
import kotlinx.android.synthetic.main.reg_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * Created by Administrator on 2018/5/15.
 */
class reg : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_layout)
        btn_reg.setOnClickListener {
            val flag = Common.isNetworkAvailable(this)
            if (flag == 0) {
                Common.display(this, "请开启手机网络")
            }else{
                if (editV_frag_register_password.text != null &&
                        editV_frag_register_password.text.toString().equals(editV_frag_register_password_agin.text.toString())){
                    if (editV_frag_register_phone.text.length == 11){
                        //Common.showProgressDialog("正在注册",this)
                        val phone = editV_frag_register_phone.text.toString()
                        val passWord = editV_frag_register_password.text.toString()
                        prePostData(phone, passWord,"unknow")
                    }else{
                        Common.display(this,"电话号码不正确")
                    }
                }else{
                    Common.display(this,"密码不正确")
                }
            }
        }
    }

    fun prePostData(phone:String,password:String,username:String){
        val map = HashMap<String, String>()
        map.put("id",phone)
        map.put("username",username)
        val md5pw = EncodeAndDecode.getMD5Str(password)
        map.put("password",md5pw)
        HttpHelper.sendOkHttpRequest(URL.REG_URL,map, object:Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                //Common.dismissProgressDialog(this@reg)
                runOnUiThread {
                    Common.display(this@reg,"注册失败")
                }
            }
            override fun onResponse(call: Call?, response: Response?) {
                if ("success".equals(HttpHelper.getMessage(response!!.body()!!.string()))){
                    runOnUiThread {
                        Common.display(this@reg,"注册成功")
                        val timer = Timer()
                        val task = object : TimerTask(){
                            override fun run() {
                                val intent = Intent()
                                finish()
                                intent.setClass(this@reg, login::class.java)
                                startActivity(intent)
                            }
                        }
                        timer.schedule(task,1400)
                    }
                }
            }
        })
    }
}