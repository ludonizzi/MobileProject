package com.example.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val database = DatabaseManager()
        database.getUserDetail(this,Firebase.auth.currentUser.uid)

        signout.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}