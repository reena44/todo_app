package com.example.reena.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.reena.R
import com.example.reena.homescreen.MainActivity
import com.example.reena.login.LoginActivity
import com.example.reena.utility.UserLoginDetail
import com.example.reena.utility.UserLoginDetail.Companion.flag


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            if ( flag) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))

                }
            finish()
        }, 5 * 1000)
    }
}
/* val TempDialog: ProgressDialog
        val CDT: CountDownTimer
        var i = 5

        TempDialog = ProgressDialog(this)
        TempDialog.setMessage("Please wait...")
        TempDialog.setCancelable(false)
        TempDialog.progress = i
        TempDialog.show()

        CDT = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                TempDialog.setMessage("Please wait..$i sec")
                i--
            }

            override fun onFinish() {
                TempDialog.dismiss()
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }
        }.start()
*/