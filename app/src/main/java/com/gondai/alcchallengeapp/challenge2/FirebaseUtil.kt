package com.gondai.alcchallengeapp.challenge2

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

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
    fun isAdmin():Boolean
        =checkAdmin(mFirebaseAuth.currentUser)


    private  fun checkAdmin(user: FirebaseUser?):Boolean{
        var exists=false;
        user.let {
            mFirebaseDatabase.reference.child("admins")
                .orderByChild("uid").equalTo(it?.uid)
                .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                   exists=p0.exists()
                }

                override fun onCancelled(p0: DatabaseError) {
//
                }
            })
        }
        return exists
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