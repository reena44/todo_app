package com.example.reena.homescreen

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reena.R
import com.example.reena.VideoViewModel
import com.example.reena.aboutus.AboutUs
import com.example.reena.login.LoginActivity
import com.example.reena.utility.GridSpacingItemDecoration
import com.example.reena.utility.UserLoginDetail.Companion.flag
import com.example.reena.utility.UserLoginDetail.Companion.sharedPrefFile
import com.example.reena.videolink.VideoViewAdapter
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.videos_layout.*
import kotlinx.android.synthetic.main.view_pager.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,VideoViewAdapter.OnItemClick{
    var doubleBackToExitPressedOnce = false
    val spanCount = 2 // 2 columns
    val spacing = 5 // 0px
    val includeEdge = false

    private var name: String?= " "
     private var emaiId: String?= " "
    var arrayList =ArrayList<VideoViewModel>()

    private lateinit var toggle: ActionBarDrawerToggle


    lateinit private var mGoogleApiClient: GoogleApiClient

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        addDataTOModdel()
        val header: View = (findViewById<NavigationView>(R.id.nav_view)).getHeaderView(0)

        setDrawer()
        setProfileData(header)
        setAdapter()

      /* // setTabLayout()
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayoutListener()*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
}


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addDataTOModdel() {

        arrayList.add(VideoViewModel(getString(R.string.title1), getString(R.string.link1), this.getDrawable(R.drawable.mera_bapu)))
        arrayList.add(VideoViewModel(getString(R.string.title2), getString(R.string.link2), this.getDrawable(R.drawable.udeak)))
        arrayList.add(VideoViewModel(getString(R.string.title3), getString(R.string.link3), this.getDrawable(R.drawable.vapari_hunda)))
        arrayList.add(VideoViewModel(getString(R.string.title4), getString(R.string.link4), this.getDrawable(R.drawable.din_char)))
        arrayList.add(VideoViewModel(getString(R.string.title5), getString(R.string.link5), this.getDrawable(R.drawable.aitbar)))
        arrayList.add(VideoViewModel(getString(R.string.title6), getString(R.string.link6), this.getDrawable(R.drawable.rooh)))
        arrayList.add(VideoViewModel(getString(R.string.title7), getString(R.string.link7), this.getDrawable(R.drawable.ki_pta_c)))
        arrayList.add(VideoViewModel(getString(R.string.title8), getString(R.string.link8), this.getDrawable(R.drawable.pehchan)))

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
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        rv_videos_link.layoutManager = GridLayoutManager(this, spanCount, LinearLayout.VERTICAL, false)
        rv_videos_link.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        rv_videos_link.adapter = VideoViewAdapter(this, arrayList)
        /*val adapter = ViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter
*/
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
            R.id.home -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            R.id.about_us ->{
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this,AboutUs::class.java))
                }
            }


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

     fun shareIntent() {

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

    override fun onClickListener(youtubeID: String) {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(youtubeID)
        )
        try {
            this@MainActivity.startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
        }
    }


}

