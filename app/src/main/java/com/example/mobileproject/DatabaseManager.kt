package com.example.mobileproject

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
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

    fun writeNewUser(name: String?, email: String?, score: Int){
        val user = UserModel(name,email,score)
        database!!.child("users").child(uid!!).setValue(user)
    }

    fun getAndSetBestScore(current_score: Int){
        database!!.child("users").child(uid!!).get().addOnSuccessListener {
            if(current_score > it.child("score").value.toString().toInt()){
                database.child("users").child(uid).child("score").setValue(current_score.toString())
            }
        }
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
                myDataset.sortByDescending { it -> it.score }
                fragment.requireView().recycler_view_users.apply {
                    layoutManager = LinearLayoutManager(fragment.activity)
                    adapter = UserAdapter(this.context, myDataset, fragment)
                }
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting users", it)
        }
    }


    fun getUserDetail(activity: Activity, uid: String){
        database!!.child("users").child(uid).get().addOnSuccessListener {
            activity.requireViewById<EditText>(R.id.edit_name).setText(it.child("name").value.toString())
            activity.requireViewById<TextView>(R.id.view_email).text = it.child("email").value.toString()
            activity.requireViewById<TextView>(R.id.view_score).text = it.child("score").value.toString()
                activity.requireViewById<EditText>(R.id.edit_name).addTextChangedListener(object :
                    TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(s: CharSequence, start: Int,
                                                   count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int,
                                               before: Int, count: Int) {
                        database.child("users").child(uid).child("name").setValue(s.toString())
                    }
                })
            }
        }

}