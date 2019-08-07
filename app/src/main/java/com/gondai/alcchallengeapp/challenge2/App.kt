package com.gondai.alcchallengeapp.challenge2

import android.app.Application

class App:Application() {
    companion object{
       var currentTravelItem:TravelItem=TravelItem()
    }


}