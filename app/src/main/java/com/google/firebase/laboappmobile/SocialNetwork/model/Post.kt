package com.google.firebase.laboappmobile.SocialNetwork.model

class Post {
    private var postid:String=""
    private var publisher:String=""
    private var caption:String=""

    constructor()

    constructor(postid: String, publisher: String, caption: String) {
        this.postid = postid
        this.publisher = publisher
        this.caption = caption
    }

    //getters
    fun getPostId():String{
        return postid
    }

    fun getPublisher():String{
        return publisher
    }
    fun getCaption():String{
        return caption
    }

    //setters
    fun setPostId(postid: String)
    {
        this.postid=postid
    }


    fun setPublisher(publisher: String)
    {
        this.publisher=publisher
    }

    fun setCaption(caption: String)
    {
        this.caption=caption
    }
}
