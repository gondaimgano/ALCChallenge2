package com.gondai.alcchallengeapp.challenge2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI

class ListActivity : AppCompatActivity() {
   val RC_SIGN_IN=1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        invalidateOptionsMenu()
        menuInflater.inflate(R.menu.save,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.mnu_logout ->
                FirebaseUtil.mFirebaseAuth.signOut()
            R.id.mnu_save ->
                startActivity(Intent(this,DealActivity::class.java))
            else ->
                Toast.makeText(this,"No choice",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()



        FirebaseUtil.openFbReference("travels"){
            if(it.currentUser==null)
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(arrayListOf(
                       AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build(),
                RC_SIGN_IN)
        }

    }



    override fun onPause() {
        FirebaseUtil.detachListener()
        super.onPause()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK)
            Toast.makeText(this,"Welcome Back",Toast.LENGTH_SHORT).show()

    }
}
