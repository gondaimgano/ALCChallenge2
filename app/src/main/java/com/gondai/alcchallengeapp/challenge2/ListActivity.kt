package com.gondai.alcchallengeapp.challenge2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class TravelHolder(v: View):RecyclerView.ViewHolder(v){
    private var text1:TextView
    private var text2:TextView
    private var text3:TextView
    private var image:ImageView
    init {
        text1=v.findViewById(R.id.text1)
        text2=v.findViewById(R.id.text2)
        text3=v.findViewById(R.id.text3)
        image=v.findViewById(R.id.image1)
    }
    fun bind(t:TravelItem){
       // Toast.makeText(itemView.context,"${t.imageURL}",Toast.LENGTH_SHORT).show()
        with(t){
            text1.text=place
            text2.text=description
            text3.text=amount
            Glide.with(itemView).load(imageURL).placeholder(R.color.colorPrimary).into(image)
        }
    }

}

class ListActivity : AppCompatActivity() {
   val RC_SIGN_IN=1001
    lateinit var adapterTravels: FirebaseRecyclerAdapter<TravelItem, TravelHolder>
    // var isAdmin:Boolean=false
    //private lateinit var mMenu: Menu

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
               holder.bind(model)
            }
        }

        recyclertravels.apply {
            layoutManager=LinearLayoutManager(this@ListActivity)
            adapter=adapterTravels
        }



    }





    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
       // Log.d("Menu Man :)","Called bro prepare !!!")

        if(FirebaseUtil.isAdministrator)
        {
             menu?.clear()
            menuInflater.inflate(R.menu.basic_admin,menu)

        }
        else
        {
            menu?.clear()
            menuInflater.inflate(R.menu.basic_user,menu)
        }


        return  super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.mnu_logout ->
                FirebaseUtil.signOut()
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
