package com.example.mobileproject

import android.content.Context
import android.content.Intent
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_profile.*
import java.lang.Math.acos
import java.lang.Math.asin

import java.util.*


class Profile : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private var mSensors: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensors = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY)

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
                DatabaseManager().removeAccount()
                val user = Firebase.auth.currentUser!!
                LoginManager.getInstance().logOut();

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

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(
            this,
            mSensors,
            SensorManager.SENSOR_STATUS_ACCURACY_LOW
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x = event!!.values[0]
        val y = event.values[1]
        val z = event.values[2]
        profilepicture.rotation = Math.toDegrees(acos(y / 9.8)).toFloat()

        if (x < 0) {
            profilepicture.rotation = profilepicture.rotation * -1
        }


    }

}