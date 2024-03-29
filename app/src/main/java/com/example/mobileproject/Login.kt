package com.example.mobileproject

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*


class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    //facebook
    private lateinit var callbackManager: CallbackManager

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //Show password checkbox listener
        loginpassword_check.setOnClickListener{
            showPassword()
        }


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")




        back_button_login.setOnClickListener{
            onBackPressed()
        }


        custom_login_button.setOnClickListener{
            onLogin()
        }


        googlebutton.setSize(SignInButton.SIZE_ICON_ONLY)
        googlebutton.setOnClickListener{
            signIn()
        }

        signuptext.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }
        //facebook
        // [START initialize_fblogin]
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        login_button.setReadPermissions("email", "public_profile")
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        })
        // [END initialize_fblogin]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
        //facebook
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        addUserToDatabase()

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        updateUI(null)
                    }

                }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //facebook
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        if(task.result!!.additionalUserInfo!!.isNewUser){
                            addUserToDatabase()
                        }

                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                }
    }
    private fun onLogin(){
        if(loginemail.text.toString().isEmpty()){
            loginemail.error = resources.getString(R.string.email_blank)
            loginemail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(loginemail.text.toString()).matches()){
            loginemail.error = resources.getString(R.string.email_miss_error)
            loginemail.requestFocus()
            return
        }
        if(loginpassword.text.toString().isEmpty()){
            loginpassword.error = resources.getString(R.string.password_blank)
            loginpassword.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(loginemail.text.toString(), loginpassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user:FirebaseUser? = auth.currentUser
                        updateUI(user)
                    } else {
                        updateUI(null)
                    }
                }
    }


    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUserToDatabase () {
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val email = user?.email
        var score = 0

        val database = DatabaseManager()
        database.writeNewUser(name, email,score)

        //Here we can send data to database
        //reference.child(id!!).setValue(model)
    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    // Show Password function, used for login form
    private var passChecked : Boolean = true
    private fun showPassword(){

        if (passChecked){
            loginpassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passChecked = !passChecked
            return
        }


        loginpassword.transformationMethod = PasswordTransformationMethod.getInstance()
        passChecked = !passChecked
    }

}