package com.gondai.alcchallengeapp.challenge2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import kotlinx.android.synthetic.main.activity_sign_up.*

class DealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        butImagePicker.setOnClickListener {
            ImagePicker.create(this).start()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.mnu_savedeal->
                FirebaseUtil.getReference().push().apply {
                    val k = this.key
                    setValue(TravelItem().apply {
                        key = k
                        place = txtplace.text.toString()
                        description = txtdescription.text.toString()
                        amount = txtamount.text.toString()
                        imageURL = previewImage.toString()
                    })
                }.apply {
                    onBackPressed()
                }
            R.id.mnu_cancel ->
                onBackPressed()

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //invalidateOptionsMenu()
        menuInflater.inflate(R.menu.savedeal,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            // or get a single image only
            val image = ImagePicker.getFirstImageOrNull(data)
            Glide.with(this).load(image.path).into(previewImage)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
