package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.timekeeper.R
import com.example.timekeeper.activity.AddActivity
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.fragment.allActionFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.R.id.tabs
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.uuzuche.lib_zxing.activity.CaptureActivity
import java.util.ArrayList
import android.widget.Toast
import com.example.timekeeper.module_login_reg.login
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.ImageUtil
import com.uuzuche.lib_zxing.activity.CodeUtils




class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener ,BottomNavigationBar.OnTabSelectedListener{

    companion object {
        val REQUEST_CODE:Int=1
        val REQUEST_IMAGE:Int=2
    }

    lateinit internal var allFragment :allActionFragment
    lateinit internal var allFragment1 :allActionFragment
    lateinit internal var allFragment2 :allActionFragment
    lateinit internal var allFragment3 :allActionFragment

    lateinit internal var bottomNavigationBar: BottomNavigationBar
    internal var lastSelectedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initBottomNavi()

        //新建活动
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }

        //drawer_layout的进场与退场动画
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(this, CaptureActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE)
            }
            R.id.nav_gallery -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_IMAGE)
            }
            R.id.nav_slideshow -> {
                val edit = getSharedPreferences("config", 0).edit()
                edit.clear()
                edit.commit()
            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {
                val edit = getSharedPreferences("config", 0).edit()
                edit.clear()
                edit.commit()
                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_send -> {
                KillAllActivitys()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun initBottomNavi(){
        allFragment = allActionFragment.newInstance()
        allFragment1 = allActionFragment.newInstance()
        allFragment2 = allActionFragment.newInstance()
        allFragment3 = allActionFragment.newInstance()
        bottomNavigationBar = findViewById<BottomNavigationBar>(R.id.bottomNavigationBar)
        bottomNavigationBar.setTabSelectedListener(this)
        bottomNavigationBar.clearAll()
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
        bottomNavigationBar
                .addItem(BottomNavigationItem(R.drawable.ic_all_activity, "所有任务").setActiveColorResource(R.color.orange))
                .addItem(BottomNavigationItem(R.drawable.ic_nodone_activity, "未完成").setActiveColorResource(R.color.teal))
                .addItem(BottomNavigationItem(R.drawable.ic_myadd_activity, "我创建").setActiveColorResource(R.color.blue))
                .addItem(BottomNavigationItem(R.drawable.ic_sharefile, "文件共享").setActiveColorResource(R.color.brown))
                .initialise()
        bottomNavigationBar.selectTab(if (lastSelectedPosition > 3) 3 else lastSelectedPosition, true)

        setDefaultFragment()
    }

    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("fragmentType","0")
            allFragment.setArguments(bundle)
            replace(R.id.fragment_contain, allFragment)
        }.commitAllowingStateLoss()
    }

    override fun onTabReselected(position: Int) {

    }

    override fun onTabUnselected(position: Int) {

    }

    override fun onTabSelected(position: Int) {
        lastSelectedPosition = position
        replaceFragments(position)
    }

    private fun replaceFragments(position: Int) {
        supportFragmentManager.beginTransaction().apply {
            when (position) {
                0 -> {  val bundle = Bundle()
                        bundle.putString("fragmentType","0")
                        allFragment.setArguments(bundle)
                        replace(R.id.fragment_contain, allFragment)}
                1 -> {val bundle = Bundle()
                      bundle.putString("fragmentType","1")
                      allFragment1.setArguments(bundle)
                      replace(R.id.fragment_contain, allFragment1)}
                2 -> {val bundle = Bundle()
                      bundle.putString("fragmentType","2")
                      allFragment2.setArguments(bundle)
                      replace(R.id.fragment_contain, allFragment2)}
                3 -> { val bundle = Bundle()
                       bundle.putString("fragmentType","3")
                       allFragment3.setArguments(bundle)
                       replace(R.id.fragment_contain, allFragment3)}
            }
        }.commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE){
            println("扫描返回")
           if (data != null){
               val bundle = data.extras
               if(bundle == null){
                   return
               }
               if (bundle.getInt(CodeUtils.RESULT_TYPE) === CodeUtils.RESULT_SUCCESS) {
                   val result = bundle.getString(CodeUtils.RESULT_STRING)
                   println("解析结果"+result)
                   val split = result!!.split("=")
                   val s = split[1]
                   println("id:"+s)

                   val intent = Intent(this@MainActivity, selectTimeActivity::class.java)
                   intent.putExtra("activityId",s)
                   startActivity(intent)
                   finish()

               } else if (bundle.getInt(CodeUtils.RESULT_TYPE) === CodeUtils.RESULT_FAILED) {
                   println("解析二维码失败")
                   Toast.makeText(this@MainActivity, "解析二维码失败", Toast.LENGTH_LONG).show()
               }
           }
        }

        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                val uri = data.data

                CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri),object :CodeUtils.AnalyzeCallback{
                    override fun onAnalyzeSuccess(mBitmap: Bitmap?, result: String?) {
                        println("解析结果"+result)
                        val split = result!!.split("=")
                        val s = split[1]
                        println("id:"+s)

                        val intent = Intent(this@MainActivity, selectTimeActivity::class.java)
                        intent.putExtra("activityId",s)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnalyzeFailed() {
                        Toast.makeText(this@MainActivity, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                })

            }
        }
    }
}
