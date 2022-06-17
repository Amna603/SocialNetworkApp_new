package com.google.firebase.laboappmobile.SocialNetwork.Adapter


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.laboappmobile.SocialNetwork.Fragments.PostDetailFragment
import com.google.firebase.laboappmobile.SocialNetwork.Fragments.ProfileFragment
import com.google.firebase.laboappmobile.SocialNetwork.MainActivity
import com.google.firebase.laboappmobile.SocialNetwork.model.Post
import com.google.firebase.laboappmobile.SocialNetwork.R
import com.google.firebase.laboappmobile.SocialNetwork.ShowUsersActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.mypost_layout.view.*

class MyPostAdapter(private val mContext: Context, private  val mPost:List<Post>): RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.mypost_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: MyPostAdapter.ViewHolder, position: Int) {
        val post = mPost[position]
        val postid = post.getPostId()

        holder.caption.text = post.getCaption()




        holder.caption.setOnClickListener {

            val intent = Intent(mContext, MainActivity::class.java).apply {
                putExtra("PUBLISHER_ID", post.getPublisher())
            }
            mContext.startActivity(intent)
        }

        holder.caption.setOnClickListener {
            if (holder.caption.tag.toString() == "like") {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .removeValue()
            }

        }


    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var caption: TextView

        init {
            caption = itemView.findViewById(R.id.caption)

        }
    }

}
