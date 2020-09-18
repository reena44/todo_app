package com.example.reena.homescreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reena.R
import com.example.reena.login.LoginActivity
import com.example.reena.utility.UserLoginDetail
import com.example.reena.utility.UserLoginDetail.Companion.sharedPrefFile
import com.example.reena.viewpager.ViewPagerAdapter
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit private var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTabLayout()
        setAdapter()
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayoutListener()

}
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (item.itemId) {
            R.id.signout -> {
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback { // ...
                    Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT)
                        .show()
                    val sharePref = this.getSharedPreferences(
                        sharedPrefFile,
                        MODE_PRIVATE
                    )
                    UserLoginDetail().flag = false


                    val editor: SharedPreferences.Editor = sharePref.edit()
                    editor.remove("name_key")
                    editor.remove("email_id")
                    editor.clear()
                    editor.apply()
                    editor.commit()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("finish", true)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // To clean up all activities

                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun tabLayoutListener() {
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    private fun setAdapter() {
        val adapter = ViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

    }

    private fun setTabLayout() {
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Home"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Sport"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Movie"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
    }
    override fun onStart() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        mGoogleApiClient.connect()
        super.onStart()
    }
}

