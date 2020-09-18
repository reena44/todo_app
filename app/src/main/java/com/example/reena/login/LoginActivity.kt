package com.example.reena.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reena.R
import com.example.reena.homescreen.MainActivity
import com.example.reena.utility.UserLoginDetail.Companion.flag
import com.example.reena.utility.UserLoginDetail.Companion.sharedPrefFile
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce = false

    lateinit var  mGoogleSignInClient: GoogleSignInClient

    private val REQUEST_CODE: Int = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signin_btn.setOnClickListener {
            progrees_bar.visibility = View.VISIBLE
            signView()
        }
       /* btn_logout.setOnClickListener {
            signOut()
        }*/


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(getString(R.string.web_id))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

   val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    private fun signOut() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Log out successfully", Toast.LENGTH_LONG)
                        .show()
                    updateUI(GoogleSignIn.getLastSignedInAccount(this))

                }
                }

    }
    private fun signView() {
      val signIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signIntent, REQUEST_CODE)

    }
    private fun updateUI(account: GoogleSignInAccount?) {
         account?.email
         if (account != null){
             Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()

             progrees_bar.visibility = View.GONE
             signin_btn.visibility = View.GONE
             flag = true

             /*tv_output.text= account.displayName +"\n"+account.email
             btn_logout.visibility = View.VISIBLE*/
             val sharePref = this.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
             val editor:SharedPreferences.Editor =  sharePref.edit()
             editor.putString("name_key", account.displayName)
             editor.putString("email_id", account.email)
             editor.apply()
             editor.commit()
             this.finish()
             startActivity(Intent(applicationContext, MainActivity::class.java))

         }
        else{
             signin_btn.visibility = View.VISIBLE
            /* btn_logout.visibility = View.GONE
             tv_output.text= "user not login"*/
         }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE){
            val accounttask = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSigninAccount(accounttask)
        }
    }

    private fun handleGoogleSigninAccount(accounttask: Task<GoogleSignInAccount>?) {

        try {
           val result = accounttask?.getResult(ApiException::class.java)
            updateUI(result)

        }
        catch (e: ApiException){
            //tv_output.text = GoogleSignInStatusCodes.getStatusCodeString(e.statusCode)
            Log.d("log", "rhandleGoogleSigninAccount : Eror status code" + e.statusCode)
            Log.d(
                "log", "rhandleGoogleSigninAccount : Eror status meassage" +
                        GoogleSignInStatusCodes.getStatusCodeString(e.statusCode)
            )

            e.printStackTrace()
        }

    }
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}