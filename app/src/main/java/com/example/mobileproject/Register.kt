package com.example.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
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


        //Show password checkbox listener
        signuppass_check.setOnClickListener{
            showPassword()
        }

        //Show confirm password checkbox listener
        signuppass_repeat_check.setOnClickListener{
            showConfirmPassword()
        }
        signupbutlogin.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }


        signupbutton.setOnClickListener{
            if(checkForm()){
                signUpUser()
            }

        }

    }

    private fun sendData() {

    }

    private fun checkForm(): Boolean {
        var flag = true
        if(signupemail.text.toString() == ""){
            signupemail.error = resources.getString(R.string.email_blank)
            flag = false
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(signupemail.text.toString()).matches()) {
            signupemail.requestFocus()
            signupemail.error = resources.getString(R.string.email_miss_error)
            flag = false
        }
        if(signuppassword.text.toString().isEmpty()){
            signuppassword.error = resources.getString(R.string.password_blank)
            flag = false
        }
        else if(signuppassword.text.toString().length < 6) {
            signuppassword.error = resources.getString(R.string.password_format_error)
            flag = false
        }
        if(!signuppasswordrepeat.text.toString().equals(signuppassword.text.toString())) {
            signuppasswordrepeat.error = resources.getString(R.string.password_confirmation_error)
            flag = false
        }
        return flag
    }

    private fun signUpUser(){
        auth.createUserWithEmailAndPassword(signupemail.text.toString(),signuppassword.text.toString())
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    //val user:FirebaseUser? = auth.currentUser
                    //user?.sendEmailVerification()
                            //?.addOnCompleteListener { task ->
                                //if (task.isSuccessful) {
                                    addDataToDatabase()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                //}
                            //}

                } else {
                    Toast.makeText(baseContext, "Signup failed"+ task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Show Password function, used for signup form
    private var pass_checked : Boolean = true
    private fun showPassword(){

        if (pass_checked){
            signuppassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            pass_checked = !pass_checked
            return
        }


        signuppassword.transformationMethod = PasswordTransformationMethod.getInstance()
        pass_checked = !pass_checked
    }

    //Show confirm password function, used for signup form
    private var pass_confirm_checked : Boolean = true
    private fun showConfirmPassword(){

        if (pass_confirm_checked){
            signuppasswordrepeat.transformationMethod = HideReturnsTransformationMethod.getInstance()
            pass_confirm_checked = !pass_confirm_checked
            return
        }


        signuppasswordrepeat.transformationMethod = PasswordTransformationMethod.getInstance()
        pass_confirm_checked = !pass_confirm_checked
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //reload()
        }
    }

    private fun addDataToDatabase() {
        val database = DatabaseManager()
        val user = auth.currentUser
        val name = signupfullname.text.toString().trim()
        val email = signupemail.text.toString().trim()
        val score = 0
        database.writeNewUser(user!!.uid, name, email,score)
    }

    fun updateUI(currentUser: FirebaseUser?){

    }
}