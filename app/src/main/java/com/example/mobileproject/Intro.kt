package com.example.mobileproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_intro.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Intro : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        loginbut.setOnClickListener {
            findNavController().navigate(R.id.action_IntroFragment_to_Login)
        }
        signupbut.setOnClickListener {
            findNavController().navigate(R.id.action_IntroFragment_to_Register)
        }

        //button animations
        bird_gif.alpha = 0f
        bird_gif.translationX = -300f
        bird_gif.animate().alpha(1f).translationXBy(300f).setStartDelay(1000).duration = 1000

        loginbut.alpha = 0f
        loginbut.translationY = 300f
        loginbut.animate().alpha(1f).translationYBy(-300f).duration = 1000

        signupbut.alpha = 0f
        signupbut.translationY = 300f
        signupbut.animate().alpha(1f).translationYBy(-300f).setStartDelay(200).duration = 1000
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser!=null){
            findNavController().navigate(R.id.action_IntroFragment_to_HomeFragment)
        }
    }
}