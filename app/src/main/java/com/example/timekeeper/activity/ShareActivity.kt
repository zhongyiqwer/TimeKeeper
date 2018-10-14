package com.example.timekeeper.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.View
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.ImageUtil
import com.example.timekeeper.util.URL
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.activity_share.*
import java.io.File

/**
 * Created by ZY on 2018/6/10.
 */
class ShareActivity :BaseActivity(),View.OnClickListener{

    lateinit var bitmap:Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        val activityId = intent.getStringExtra("activityId")
        val url = URL.Share_Action + "?activityId="+activityId
        println("URL:$url")
        bitmap = CodeUtils.createImage(url, 400, 400, BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
        image_content.setImageBitmap(bitmap)
        btn_shareTo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v==btn_shareTo){
            shareTo()
        }
    }

    fun shareTo(){
        println("bitmap:"+bitmap.toString())
        val file = ImageUtil.bitmap2File(bitmap)
        println(file!=null)
        println(file.isFile)
        if (file!=null && file.isFile){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                println("android 8.0")
                val uri = getUri(file)
                println("uri = $uri")
                val intent = Intent()
                intent.flags = Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_STREAM,uri)
                intent.setType("image/*")
                intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
                this.startActivity(Intent.createChooser(intent,"分享活动"))
                finish()
            }else{
                val uri = getUri(file)
                val intent = Intent()
                intent.setAction(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_STREAM,uri)
                intent.setType("image/*")
                startActivity(Intent.createChooser(intent,"分享活动"))
                finish()
            }
        }
    }

    private fun getUri(file:File):Uri{
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(applicationContext, "com.example.timekeeper.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
    }
}