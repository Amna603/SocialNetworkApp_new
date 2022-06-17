package com.google.firebase.laboappmobile.SocialNetwork.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.laboappmobile.SocialNetwork.Fragments.ProfileFragment
import com.google.firebase.laboappmobile.SocialNetwork.MainActivity
import com.google.firebase.laboappmobile.SocialNetwork.model.User
import com.google.firebase.laboappmobile.SocialNetwork.model.Post
import com.google.firebase.laboappmobile.SocialNetwork.ShowUsersActivity
import  com.google.firebase.laboappmobile.SocialNetwork.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_add_post.*


class PostAdapter
    (private val mContext:Context,private  val mPost:List<Post>):RecyclerView.Adapter<PostAdapter.ViewHolder>()
{
    private var firebaseUser:FirebaseUser?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return  mPost.size
    }

    //code for events
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser= FirebaseAuth.getInstance().currentUser
        val post=mPost[position]
        val postid=post.getPostId()

        holder.caption.text=post.getCaption()
        publisherInfo(holder.profileImage,holder.username,post.getPublisher())
        isLiked(post.getPostId(),holder.likeButton,holder.caption)
        getCountofLikes(post.getPostId(),holder.likes)


        holder.profileImage.setOnClickListener {

            val intent = Intent(mContext, MainActivity::class.java).apply {
                putExtra("PUBLISHER_ID", post.getPublisher())
            }
            mContext.startActivity(intent)
        }

        holder.username.setOnClickListener {

            val intent = Intent(mContext, MainActivity::class.java).apply {
                putExtra("PUBLISHER_ID", post.getPublisher())
            }
            mContext.startActivity(intent)
        }

        holder.caption.setOnClickListener {

            val intent = Intent(mContext, MainActivity::class.java).apply {
                putExtra("PUBLISHER_ID", post.getPublisher())
            }
            mContext.startActivity(intent)
        }

        holder.caption.setOnClickListener {
            if (holder.caption.tag.toString() == "like") {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .removeValue()
            }

        }


        holder.likeButton.setOnClickListener{
            if (holder.likeButton.tag.toString()=="like")
            {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .setValue(true)
            }
            else
            {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .removeValue()
            }
        }

        holder.likes.setOnClickListener {
            val intent = Intent(mContext, ShowUsersActivity::class.java)
            intent.putExtra("id",post.getPostId())
            intent.putExtra("title","likes")
            mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var profileImage:CircleImageView
        var likeButton:ImageView
        var likes:TextView
        var username:TextView
        var caption:TextView


        init {
            profileImage=itemView.findViewById(R.id.publisher_profile_image_post)
            likeButton=itemView.findViewById(R.id.post_image_like_btn)
            likes=itemView.findViewById(R.id.likes)
            username=itemView.findViewById(R.id.publisher_user_name_post)
            caption=itemView.findViewById(R.id.caption)

        }
    }

    private fun isLiked(postid: String, imageView: ImageView, caption: TextView) {

        firebaseUser=FirebaseAuth.getInstance().currentUser
        val postRef=FirebaseDatabase.getInstance().reference.child("Likes").child(postid)

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.child(firebaseUser!!.uid).exists()) {
                    imageView.setImageResource(R.drawable.heart_clicked)
                    imageView.tag = "liked"
                }
                else {
                    imageView.setImageResource(R.drawable.heart_not_clicked)
                    imageView.tag = "like"
                }
            }
        })
    }

    private fun getCountofLikes(postid:String,likesNo: TextView) {
        val postRef=FirebaseDatabase.getInstance().reference.child("Likes").child(postid)
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(datasnapshot: DataSnapshot) {
                likesNo.text = datasnapshot.childrenCount.toString()+" likes"
            }
        })
    }


    private fun publisherInfo(profileImage: CircleImageView, username: TextView, publisherID: String) {

        val userRef=FirebaseDatabase.getInstance().reference.child("Users").child(publisherID)
        userRef.addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    username.text =(user.getUsername())
                }
            }
        })
    }
}