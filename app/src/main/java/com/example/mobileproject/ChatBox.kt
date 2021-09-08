package com.example.mobileproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_chat_box.*
import androidx.recyclerview.widget.LinearLayoutManager


//import com.github.nkzawa.socketio.client.IO
//import com.github.nkzawa.socketio.client.Socket
//import com.github.nkzawa.emitter.Emitter

import java.net.URISyntaxException
import android.widget.Toast


import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

import org.json.JSONException

import org.json.JSONObject





class ChatBox : AppCompatActivity() {
    private lateinit var socket : Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_box)


        var Nickname = intent.getStringExtra("nickname")
        val MessageList : MutableList<Message> = mutableListOf()


        //connect client to server

        try {
            socket = IO.socket("http://127.0.0.1:3000")
            //Log.d("success", socket.id())
            socket.connect()
            socket.emit("join", Nickname!!.trim())
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        //MessageList.add(Message("Paolooo", "Buongiorno"))

        val chatBoxAdapter = ChatBoxAdapter(MessageList)
        //var myDataset = listUser
        messagelist.layoutManager = LinearLayoutManager(applicationContext)
        messagelist.adapter = chatBoxAdapter


        send.setOnClickListener{
            if(message_edit_box.text.toString().isNotEmpty()){
                socket.emit("messagedetection",Nickname,message_edit_box.text.toString())
                message_edit_box.text.clear()
            }

        }

        //implementing socket listeners
        //implementing socket listeners
        socket.on("userjoinedthechat", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                runOnUiThread {
                    val data = args[0] as String?
                    Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
                }
            }
        })

        socket.on("userdisconnect") { args ->
            runOnUiThread {
                val data = args[0] as String
                Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
            }
        }

        socket.on("message") { args ->
            runOnUiThread {
                val data = args[0] as JSONObject
                try {
                    //extract data from fired event
                    val nickname = data.getString("senderNickname")
                    val message = data.getString("message")

                    // make instance of message
                    val m = Message(nickname, message)


                    //add the message to the messageList
                    MessageList.add(m)

                    // add the new updated list to the adapter


                    // notify the adapter to update the recycler view
                    chatBoxAdapter.notifyDataSetChanged()

                    //set the adapter for the recycler view

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }

}