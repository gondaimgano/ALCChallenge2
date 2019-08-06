package com.gondai.alcchallengeapp.challenge2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File

class DealActivity : AppCompatActivity() {
lateinit var image: Image
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
                    val key_ = this.key
                    val ref=this
                    progressBarGo.visibility=View.VISIBLE
                    FirebaseStorage.getInstance().reference.child(key_!!).putFile(
                        Uri.fromFile(File(image.path))
                    ).addOnSuccessListener {
                        it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                            // it.toString()
                            ref.setValue(TravelItem().apply {
                                key = key_
                                place = txtplace.text.toString()
                                description = txtdescription.text.toString()
                                amount = txtamount.text.toString()
                                imageURL = it.toString()
                            }).apply {
                                progressBarGo.visibility=View.GONE
                                onBackPressed()
                            }
                        }
                    }
                        .addOnFailureListener{
                            Toast.makeText(this@DealActivity,it.message,Toast.LENGTH_SHORT).show()
                        }

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
            image = ImagePicker.getFirstImageOrNull(data)
            Glide.with(this).load(image.path).into(previewImage)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
