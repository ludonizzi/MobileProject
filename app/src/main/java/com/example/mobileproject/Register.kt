package com.example.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*


class Register : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")

        // Initialize Firebase Auth
        auth = Firebase.auth
        signupbutton.setOnClickListener{
            signUpUser()
        }

    }

    private fun sendData() {

    }

    private fun signUpUser(){
        if(signupemail.text.toString().isEmpty()){
            signupemail.error = "Please enter email"
            signupemail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(signupemail.text.toString()).matches()){
            signupemail.error = "Please enter valid email"
            signupemail.requestFocus()
            return
        }
        if(signuppassword.text.toString().isEmpty()){
            signuppassword.error = "Please enter password"
            signuppassword.requestFocus()
            return
        }
        if(!(signuppasswordagain.text.toString().equals(signuppassword.text.toString()))) {
            signuppasswordagain.error = "password_mismatch\": \"The two password fields don't match."
            signuppasswordagain.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(signupemail.text.toString(),signuppassword.text.toString())
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        //val user:FirebaseUser? = auth.currentUser
                        //user?.sendEmailVerification()
                                //?.addOnCompleteListener { task ->
                                    //if (task.isSuccessful) {
                                        var name = signupfullname.text.toString().trim()
                                        var email = signupemail.text.toString().trim()
                                        var bestScore = 0
                                        var model = DatabaseModel(name,email,bestScore)
                                        var id = reference.push().key

                                        //Here we can send data to database
                                        reference.child(id!!).setValue(model)
                                        signupfullname.setText("")
                                        signupemail.setText("")
                                        startActivity(Intent(this, Login::class.java))
                                        finish()
                                    //}
                                //}

                    } else {
                        Toast.makeText(baseContext, "Signup failed"+ task.exception, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //reload()
        }
    }

    fun updateUI(currentUser: FirebaseUser?){

    }
}