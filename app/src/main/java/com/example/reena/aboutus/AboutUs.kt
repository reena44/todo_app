package com.example.reena.aboutus

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.reena.R
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        cv_rate_app.setOnClickListener{
            rateApp()
        }
    }

    private fun rateApp() {


        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
// To count with Play market backstack, After pressing back button,
// to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    fun giveFeedback(view: View) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("reenabains@zapbuild.com");
        emailIntent.type = "text/plain";
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , arrayListOf("Recipient"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , "Message Body");


    }
}
/*
*/
