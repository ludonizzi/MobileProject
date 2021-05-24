package com.example.mobileproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_second.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.signout).setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        playgamebut.setOnClickListener{
            findNavController().navigate(R.id.action_SecondFragment_to_androidLauncher)
        }

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users").child("users")

        view.findViewById<Button>(R.id.bestScoreProva).setOnClickListener {
            val edit = view.findViewById<EditText>(R.id.editBestScore).text.toString()
            val hashMap = HashMap<Any, Any>()

            hashMap.put("name", edit)

            //reference.child("-M__6ME2q_EpKPHgAsUx").updateChildren(hashMap)


        }
    }
}