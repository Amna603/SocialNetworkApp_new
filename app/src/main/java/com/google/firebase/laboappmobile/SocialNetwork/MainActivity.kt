package com.google.firebase.laboappmobile.SocialNetwork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.laboappmobile.SocialNetwork.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: ArrayAdapter<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        db = Firebase.database
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)
        val lv_listView = findViewById(R.id.lv_listView) as ListView
        val tv_emptyTextView = findViewById(R.id.tv_emptyTextView) as TextView
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, resources.getStringArray(R.array.users_array_test))
        lv_listView.adapter = adapter
        lv_listView.onItemClickListener = AdapterView.OnItemClickListener{parent, view, position, id ->
            Toast.makeText(applicationContext,parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
        }
        lv_listView.emptyView = tv_emptyTextView

        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        getUser()

    }

    fun getUser(){
        var messageEditText = findViewById(R.id.messageEditText) as TextView
        val user = auth.currentUser
        if (user != null) {

           db.reference.child("socialUsers").get().addOnSuccessListener {

               Log.i("firebase", "Got value ${it.value}")
                    messageEditText.setText(it.value.toString())

                //val listUsers: MutableList<SocialUser> = it.value<SocialUser>
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)

            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val search :MenuItem? = menu?.findItem(R.id.nav_search)
        val searchView :SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search something!"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }








}