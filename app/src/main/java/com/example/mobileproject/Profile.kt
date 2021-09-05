package com.example.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
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
        database.getUserDetail(this)

        signout.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(Intent(this, MainActivity::class.java))
        }

        reset_score.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.reset_title))
            builder.setMessage(getString(R.string.remove_subtitle))


            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                DatabaseManager().resetScore()
                DatabaseManager().getUserDetail(this)
            }
            builder.setNegativeButton(android.R.string.no){_,_ ->

            }
            builder.show()
        }

        remove_account.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.remove_title))
            builder.setMessage(getString(R.string.remove_subtitle))
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            //parameters "dialog" and "which" are never used, according to kotlin convention, they are renamed to "_"
            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                LoginManager.getInstance().logOut();
                DatabaseManager().removeAccount()
                val user = Firebase.auth.currentUser!!

                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }

            }
            builder.setNegativeButton(android.R.string.no){_,_ ->

            }
            builder.show()
        }

        back_button.setOnClickListener{
            onBackPressed()
        }

    }
}