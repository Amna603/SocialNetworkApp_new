package com.google.firebase.laboappmobile.SocialNetwork.model

class SocialUser
{
    var mail_id: String? = null
    var pseudo: String? = null
    var followed: MutableList<SocialUser> = mutableListOf()
    var posts: MutableList<Post> = mutableListOf()

    // Empty constructor needed for Firestore serialization
    constructor()

    constructor(mail_id: String?, pseudo: String?) {
        this.mail_id = mail_id
        this.pseudo = pseudo
    }
    fun follows(socialUser: SocialUser){
        this.followed.add(socialUser)
    }

    fun addPost(post: Post){
        this.posts.add(post)
    }



}