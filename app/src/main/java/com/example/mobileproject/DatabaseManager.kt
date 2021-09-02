package com.example.mobileproject

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.view.*

class DatabaseManager(
    private val database: DatabaseReference? = Firebase.database.reference,
    private val uid: String? = Firebase.auth.currentUser!!.uid
) {

    fun writeNewUser(userId: String, name: String?, email: String?, score: Int){
        val user
        = UserModel(name,email,score)
        database!!.child("users").child(userId).setValue(user)
    }

    fun getUser(fragment: Fragment){
        val listUser: MutableList<UserModel> = mutableListOf()
        val lm: List<UserModel> = listUser

        database!!.child("users").get().addOnSuccessListener {
            if(it.hasChildren()){
                for(i in it.children) {
                    val name = i.child("name").value.toString()
                    val email = i.child("email").value.toString()
                    val score = i.child("score").value.toString().toInt()
                    listUser.add(UserModel(name, email, score))
                }

                var myDataset = listUser
                myDataset.sortByDescending {it.score }
                fragment.requireView().recycler_view_users.apply {
                    layoutManager = LinearLayoutManager(fragment.activity)
                    adapter = UserAdapter(this.context, myDataset, fragment)
                }
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting medicines data", it)
        }
    }

}