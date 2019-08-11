package com.gondai.alcchallengeapp.challenge2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TravelHolder(v: View): RecyclerView.ViewHolder(v){
    private var text1: TextView
    private var text2: TextView
    private var text3: TextView
    private var image: ImageView
    init {
        text1=v.findViewById(R.id.text1)
        text2=v.findViewById(R.id.text2)
        text3=v.findViewById(R.id.text3)
        image=v.findViewById(R.id.image1)
    }
    fun bind(t:TravelItem,action:(TravelItem)->Unit){
        // Toast.makeText(itemView.context,"${t.imageURL}",Toast.LENGTH_SHORT).show()
        itemView.setOnClickListener {
        if(FirebaseUtil.isAdministrator)
            action(t)
        }
        with(t){
            text1.text=place
            text2.text=description
            text3.text=amount
            Glide.with(itemView).load(imageURL).placeholder(R.color.colorPrimary).into(image)
        }
    }

}