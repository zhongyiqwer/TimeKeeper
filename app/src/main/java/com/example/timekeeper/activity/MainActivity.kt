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



class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener ,BottomNavigationBar.OnTabSelectedListener{

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
                val intent = Intent(this, selectTimeActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {
                KillAllActivitys()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun initBottomNavi(){
        allFragment = allActionFragment()
        allFragment1 = allActionFragment()
        allFragment2 = allActionFragment()
        allFragment3 = allActionFragment()
        bottomNavigationBar = findViewById<BottomNavigationBar>(R.id.bottomNavigationBar)
        bottomNavigationBar.setTabSelectedListener(this)
        bottomNavigationBar.clearAll()
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
        bottomNavigationBar
                .addItem(BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.orange))
                .addItem(BottomNavigationItem(R.drawable.ic_book_white_24dp, "Books").setActiveColorResource(R.color.teal))
                .addItem(BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Music").setActiveColorResource(R.color.blue))
                .addItem(BottomNavigationItem(R.drawable.ic_tv_white_24dp, "Movies & TV").setActiveColorResource(R.color.brown))
                .initialise()
        bottomNavigationBar.selectTab(if (lastSelectedPosition > 3) 3 else lastSelectedPosition, true)

        setDefaultFragment()
    }

    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction().apply {
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
                0 -> replace(R.id.fragment_contain, allFragment)
                1 -> replace(R.id.fragment_contain, allFragment1)
                2 -> replace(R.id.fragment_contain, allFragment2)
                3 -> replace(R.id.fragment_contain, allFragment3)
            }
        }.commitAllowingStateLoss()
    }
}
