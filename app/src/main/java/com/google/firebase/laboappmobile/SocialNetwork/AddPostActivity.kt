package com.google.firebase.laboappmobile.SocialNetwork

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        post_picture.setOnClickListener {
            uploadPost()
        }
    }

    private fun uploadPost() {
        when {
            TextUtils.isEmpty(write_post.text.toString()) -> Toast.makeText(
                this,
                "Please write caption",
                Toast.LENGTH_LONG
            ).show()

            else -> {

                val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                val postid = ref.push().key

                val postMap = HashMap<String, Any>()

                postMap["postid"] = postid!!
                postMap["caption"] = write_post.text.toString()
                postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                ref.child(postid).updateChildren(postMap)

                Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()

                val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                startActivity(intent)
                finish()


            }
        }
    }
}
