package com.gondai.alcchallengeapp.challenge2

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil {

  private  val mFirebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
  private lateinit var mAuthListener:FirebaseAuth.AuthStateListener
     val mFirebaseDatabase:FirebaseDatabase= FirebaseDatabase.getInstance()
    private lateinit var mDatabaseReference: DatabaseReference


    fun openFbReference(s:String,action:(FirebaseAuth)->Unit){


        mDatabaseReference= mFirebaseDatabase.reference.child(s)

        mAuthListener= FirebaseAuth.AuthStateListener {
            action(it)
        }
        attachListener()

    }

    fun getReference()= mDatabaseReference

    fun signOut()= mFirebaseAuth.signOut()

    fun attachListener(){
          mFirebaseAuth.apply {
              addAuthStateListener(mAuthListener)
          }
    }
    fun detachListener(){
        mFirebaseAuth.apply {
            removeAuthStateListener(mAuthListener)
        }
    }
}