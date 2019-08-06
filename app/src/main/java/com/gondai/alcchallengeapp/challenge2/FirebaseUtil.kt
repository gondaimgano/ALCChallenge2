package com.gondai.alcchallengeapp.challenge2

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object FirebaseUtil {

  private  val mFirebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
  private lateinit var mAuthListener:FirebaseAuth.AuthStateListener
     val mFirebaseDatabase:FirebaseDatabase= FirebaseDatabase.getInstance()
    private var mDatabaseReference: DatabaseReference=FirebaseDatabase.getInstance().reference.child("travels")
     var isAdministrator:Boolean=false


    fun openFbReference(s:String,action:(FirebaseAuth)->Unit){


        mDatabaseReference= mFirebaseDatabase.reference.child(s)

        mAuthListener= FirebaseAuth.AuthStateListener {
         action(it)

        }
        attachListener()

    }
    fun isAdmin(action: (Boolean) -> Unit):Boolean
        = checkAdmin(FirebaseAuth.getInstance().currentUser?.uid!!,action)


   private fun checkAdmin(s:String,action:(Boolean)->Unit):Boolean{


            mFirebaseDatabase.reference.child("admins")
                .child(s)
                .addListenerForSingleValueEvent(

                    object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            isAdministrator=p0.exists()
                            action(isAdministrator)
                        }

                    }
                )

        return false
    }
    fun getReference()= mDatabaseReference

    fun signOut()= mFirebaseAuth.apply {
        this.signOut()
       // detachListener()

    }

    fun attachListener(){
          mFirebaseAuth.apply {
              currentUser
              addAuthStateListener(mAuthListener)
          }
    }
    fun detachListener(){
        mFirebaseAuth.apply {
            removeAuthStateListener(mAuthListener)
        }
    }
}