package com.example.mobileproject

import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseManager(
    private val database: DatabaseReference? = Firebase.database.reference,
    private val uid: String? = Firebase.auth.uid
) {

    fun writeNewUser(userId: String, name: String?, email: String?, score: Int?){
        val user = UserModel(name,email,score)
        database!!.child("users").child(userId).setValue(user)
    }

}