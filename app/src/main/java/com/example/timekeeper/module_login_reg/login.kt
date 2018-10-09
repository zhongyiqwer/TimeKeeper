package com.example.timekeeper.module_login_reg

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import com.alibaba.fastjson.JSON
import com.example.timekeeper.R
import com.example.timekeeper.activity.MainActivity
import com.example.timekeeper.activity.SelectTimeActivity
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.EncodeAndDecode
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.login_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.collections.HashMap

/**
 * Created by ZJX on 2018/5/4.
 */
class login : BaseActivity() ,View.OnClickListener{

    //internal var intentParam :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        btn_login.setOnClickListener(this)
        editV__register_toregister.setOnClickListener(this)
        editV_forget_password.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v == editV__register_toregister){
            val intent = Intent()
            intent.setClass(this@login, reg::class.java)
            startActivity(intent)
            finish()
        }
        if (v == editV_forget_password){
            Common.display(this@login, "暂未开放")
            val intent = Intent()
            intent.setClass(this@login, findPW::class.java)
            startActivity(intent)
        }

        if (v == btn_login){
            /*val intent = Intent()
            finish()
            intent.setClass(this@login, MainActivity::class.java)
            startActivity(intent)*/

            val flag = Common.isNetworkAvailable(this)
            if (flag == 0) {
                Common.display(this, "请开启手机网络")
            } else {

                if (editV_frag_login_phone.getText().length == 11 && editV_frag_login_password.getText().toString() != "") {
                    login1()
                }else{
                    Common.display(this@login, "用户名或密码错误")
                }
            }
        }
    }

    fun login1(){

        Common.showCat(supportFragmentManager,"")
        val userId = editV_frag_login_phone.getText().toString()
        val md5pw = EncodeAndDecode.getMD5Str(editV_frag_login_password.getText().toString())
        val map = getMap(userId,md5pw)
        HttpHelper.sendOkHttpRequest(URL.LOGIN_URL,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Common.dissCat()
                    Common.display(this@login, "登录失败")
                }
            }
            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println(body)
                if ("success".equals(HttpHelper.getMessage(body))){
                    runOnUiThread {
                        Common.dissCat()
                        val jsonObject = JSON.parseObject(body)
                        val userName = jsonObject.getString("userName")
                        Common.userName = userName
                        val edit = getSharedPreferences("config", 0).edit()
                        edit.putString("userId",userId)
                        edit.putString("password",md5pw)
                        edit.commit()
                        Common.userId = userId

                        println("lonig:"+Common.selecTimeActivityId)

                        if (Common.selecTimeActivityId!=null){
                            val intent = Intent()
                            intent.setClass(this@login, SelectTimeActivity::class.java)
                            intent.putExtra("activityId",Common.selecTimeActivityId)
                            Common.selecTimeActivityId = null
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent()
                            intent.setClass(this@login, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{
                    Looper.prepare()
                    Common.display(this@login, "登录失败")
                    Looper.loop()
                }
            }
        })
    }

    fun getMap(ln: String, pw: String): HashMap<String,String> {
        val paramHM = HashMap<String, String>()
        paramHM.put("id", ln)
        paramHM.put("password", pw)
        return paramHM
    }


}