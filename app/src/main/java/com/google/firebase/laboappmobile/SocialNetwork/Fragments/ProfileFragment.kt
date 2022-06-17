package com.google.firebase.laboappmobile.SocialNetwork.Fragments

import android.R.attr.spacing
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.laboappmobile.SocialNetwork.AccountSettings
import com.google.firebase.laboappmobile.SocialNetwork.Adapter.MyPostAdapter
import com.google.firebase.laboappmobile.SocialNetwork.model.Post
import com.google.firebase.laboappmobile.SocialNetwork.model.User
import com.google.firebase.laboappmobile.SocialNetwork.ShowUsersActivity
import com.google.firebase.laboappmobile.SocialNetwork.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment: Fragment() {
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    var postList:List<Post>?=null
    var myPostAdapter:MyPostAdapter?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId", "none")!!
        }






                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId)
                            .setValue(true)

                        pushNotification()
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Followers").child(it1.toString())
                            .setValue(true)



                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId)
                            .removeValue()
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Followers").child(it1.toString())
                            .removeValue()
                    }
                }



        view.total_followers.setOnClickListener {
            val intent = Intent(context, ShowUsersActivity::class.java)
            intent.putExtra("id",profileId)
            intent.putExtra("title","followers")
            startActivity(intent)
        }
        //to get own feeds
        var recyclerView: RecyclerView?=null
        recyclerView=view.findViewById(R.id.recyclerview_profile)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
        postList=ArrayList()
        myPostAdapter= context?.let { MyPostAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter=myPostAdapter

        //Adding recycler view for saved posts
        var recyclerViewSavedImages:RecyclerView
        recyclerViewSavedImages=view.findViewById(R.id.recycler_view_saved_pic)
        recyclerViewSavedImages.setHasFixedSize(true)
        val linearLayoutManager2:LinearLayoutManager=GridLayoutManager(context,3)
        recyclerViewSavedImages.layoutManager=linearLayoutManager2



        //Default
        recyclerViewSavedImages.visibility=View.GONE
        recyclerView.visibility=View.VISIBLE



        //to fill in data in profile page
        getFollowers()
        getNoofPosts()
        getUserInfo(view)
        myPosts()

        return view
    }





   /* private fun checkFollowOrFollowingButtonStatus() {

        val followingRef = firebaseUser.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }
    }*/

    private fun getFollowers() {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    view?.total_followers?.text = snapshot.childrenCount.toString()
                }
            }
        })
    }


    private fun getNoofPosts() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var i:Int=0
                for(snapshot in p0.children)
                {
                    val post=snapshot.getValue(Post::class.java)
                    if(post!!.getPublisher().equals(profileId))
                    {
                        i=i+1
                    }
                }
                view?.total_posts?.text = ""+i
            }
        })
    }

    private fun myPosts() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                (postList as ArrayList<Post>).clear()
                for(snapshot in p0.children)
                {
                    val post=snapshot.getValue(Post::class.java)
                    if(post!!.getPublisher().equals(profileId))
                        (postList as ArrayList<Post>).add(post)
                }
                Collections.reverse(postList)
                myPostAdapter!!.notifyDataSetChanged()
            }
        })
    }





    private fun pushNotification() {

        val ref = FirebaseDatabase.getInstance().reference.child("Notification").child(profileId)

        val notifyMap = HashMap<String, Any>()
        notifyMap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
        notifyMap["text"] = "➱Started following you "
        notifyMap["postid"] = ""
        notifyMap["ispost"] = true

        ref.push().setValue(notifyMap)
    }


    private fun getUserInfo(view: View) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileId)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(view.profile_image_profile)
                    view.profile_toolbar_username?.text=user.getUsername()
                    view.fullname_in_profile?.text= user.getFullname()
                    view.username_in_profile?.text= user.getUsername()

                }
            }
        })
    }

    override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
}