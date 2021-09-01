package com.example.mobileproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Home : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.signout).setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            findNavController().navigate(R.id.action_HomeFragment_to_IntroFragment)
        }
        playgamebut.setOnClickListener{
            findNavController().navigate(R.id.action_HomeFragment_to_androidLauncher)
        }



        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users").child("users")

        view.findViewById<Button>(R.id.bestScoreProva).setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_profile2)
        }
    }
}