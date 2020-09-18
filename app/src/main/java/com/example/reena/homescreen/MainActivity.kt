package com.example.reena.homescreen

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.reena.R
import com.example.reena.login.LoginActivity
import com.example.reena.utility.UserLoginDetail.Companion.flag
import com.example.reena.utility.UserLoginDetail.Companion.sharedPrefFile
import com.example.reena.viewpager.ViewPagerAdapter
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.view_pager.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var doubleBackToExitPressedOnce = false

    private var name: String?= " "
     private var emaiId: String?= " "

    private lateinit var toggle: ActionBarDrawerToggle


    lateinit private var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        val header: View = (findViewById<NavigationView>(R.id.nav_view)).getHeaderView(0)

        setDrawer()
        setProfileData(header)

        setTabLayout()
        setAdapter()
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayoutListener()

}

    private fun setProfileData(header: View) {
        val sharePref = this.getSharedPreferences(
            sharedPrefFile,
            AppCompatActivity.MODE_PRIVATE
        )
        name = sharePref.getString("name_key", " ")
        emaiId = sharePref.getString("email_id", " ")

        if (name.isNullOrEmpty()) {
            header.nav_header_textView.text = " "

        } else {
            header.nav_header_textView.text =  name+"\n"+emaiId

        }
    }

    private fun setDrawer() {
        nav_view.setNavigationItemSelectedListener(this)


        setSupportActionBar(toolbar_main)
        toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
         drawer_layout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

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
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_Logout -> {

                logout()
            }
            R.id.share_app -> {
                shareIntent()

            }
            R.id.nav_item_one -> Toast.makeText(this, "Clicked item one", Toast.LENGTH_SHORT).show()
            R.id.nav_item_two -> Toast.makeText(this, "Clicked item two", Toast.LENGTH_SHORT).show()
            R.id.nav_item_three -> Toast.makeText(this, "Clicked item three", Toast.LENGTH_SHORT)
                .show()
            R.id.nav_item_four -> Toast.makeText(this, "Clicked item four", Toast.LENGTH_SHORT)
                .show()
        }
        return true
    }

    private fun logout() {
        val alertDialog = AlertDialog.Builder(this)

        // set message of alert dialog
        alertDialog.setMessage("Are you sure you want to logout?")
            .setTitle("Logout")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
           .setPositiveButton("Yes") { dialogInterface, which ->

               Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback { // ...
                   Toast.makeText(applicationContext, "Logged Out successfully", Toast.LENGTH_SHORT)
                       .show()
                  flag = false

                   val intent = Intent(this, LoginActivity::class.java)
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // To clean up all activities
                   startActivity(intent)
               }

           }
            // negative button text and action
        .setNegativeButton("No"){ dialogInterface, which ->
            dialogInterface.dismiss()
            }
        val alert = alertDialog.create()
        alert.show()

    }

    private fun shareIntent() {

        /** ACTION_SEND: Deliver some data to someone else.
        createChooser (Intent target, CharSequence title): Here, target- The Intent that the user will be selecting an activity to perform.
        title- Optional title that will be displayed in the chooser.
        Intent.EXTRA_TEXT: A constant CharSequence that is associated with the Intent, used with ACTION_SEND to supply the literal data to be sent.
         */
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Todo app")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage = """
                ${shareMessage}https://play.google.com/store/apps/details?id=com.example.reena
                
                
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share via "))
        } catch (e: Exception) {
            //e.toString();

    }}
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }


}

