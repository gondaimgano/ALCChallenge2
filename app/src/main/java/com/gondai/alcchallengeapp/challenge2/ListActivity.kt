package com.gondai.alcchallengeapp.challenge2


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*


class ListActivity : AppCompatActivity() {
   val RC_SIGN_IN=1001
    lateinit var adapterTravels: FirebaseRecyclerAdapter<TravelItem, TravelHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options = FirebaseRecyclerOptions.Builder<TravelItem>()
            .setQuery(FirebaseUtil.getReference(), TravelItem::class.java)
            .build()

        adapterTravels = object : FirebaseRecyclerAdapter<TravelItem, TravelHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelHolder {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.travel_item, parent, false)

                return TravelHolder(view)
            }

            protected override fun onBindViewHolder(holder: TravelHolder, position: Int, model: TravelItem) {
               holder.bind(model){
                   startActivity(Intent(this@ListActivity,DealActivity::class.java).apply {
                      App.currentTravelItem=model
                   })
               }
            }
        }

        recyclertravels.apply {
            layoutManager=LinearLayoutManager(this@ListActivity)
            adapter=adapterTravels
        }



    }





    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        menu?.clear()
     if(FirebaseUtil.isAdministrator) menuInflater.inflate(R.menu.basic_admin,menu) else menuInflater.inflate(R.menu.basic_user,menu)



        return  super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.mnu_logout ->
                FirebaseUtil.signOut()
            R.id.mnu_save ->
                App.apply {
                    currentTravelItem=TravelItem()
                }.apply {
                    startActivity(Intent(this@ListActivity,DealActivity::class.java))
                }


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
                else
                FirebaseUtil.isAdmin {
                    invalidateOptionsMenu()
                }





        }
        adapterTravels.startListening()



    }



    override fun onPause() {
        adapterTravels.stopListening()
        FirebaseUtil.detachListener()
        super.onPause()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK)

         FirebaseUtil.isAdmin {
             invalidateOptionsMenu()
         }


    }
}
