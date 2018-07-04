package com.quang.googleio.hanoi.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.adapter.ScheduleAdapter
import com.quang.googleio.hanoi.adapter.SpeakerAdapter
import com.quang.googleio.hanoi.app.AppController
import com.quang.googleio.hanoi.controller.MySQLiteController
import kotlinx.android.synthetic.main.activity_detail_topic.*
import org.json.JSONObject
import java.util.*

class DetailTopicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_topic)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        val controller = MySQLiteController(this)

        imvClose.setOnClickListener { finish() }

        val bundle = intent.getBundleExtra("topic")
        val title = bundle.getString("title")
        tvTitle.text = title
        val time = bundle.getString("time")
        tvTime.text = time
        val room = bundle.getString("room")
        tvRoom.text = room
        val content = bundle.getString("content")
        tvContent.text = content
        var isBooked = bundle.getBoolean("isBooked")
        val id = bundle.getString("id")
        val color = bundle.getString("color")
        val backgroundTintList = ColorStateList.valueOf(ScheduleAdapter.parse(color))
        layoutTitle.backgroundTintList = backgroundTintList

        updateUI(isBooked)

        rvSpeaker.layoutManager = LinearLayoutManager(this)
        rvSpeaker.isNestedScrollingEnabled = false
        val adapter = SpeakerAdapter(bundle.getString("speaker"))
        rvSpeaker.adapter = adapter

        imvStar.setOnClickListener {
            if (account == null) {
                showNoti(getString(R.string.noti_require_login))
                return@setOnClickListener
            }
            if (progressBar != null) progressBar.visibility = View.VISIBLE
            val url = if (isBooked) {
                "https://ioextended.org/api/reservation/cancel"
            } else {
                "https://ioextended.org/api/reservation"
            }
            val params = JSONObject()
            params.put("id", id)
            val req = object : JsonObjectRequest(
                    Method.POST,
                    url,
                    params,
                    Response.Listener {
                        isBooked = !isBooked
                        controller.updateStatusOfTopic(id, isBooked)
                        updateUI(isBooked)
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                    },
                    Response.ErrorListener { e ->
                        e.printStackTrace()
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                        val jo = JSONObject(String(e.networkResponse.data!!))
                        Log.d("Quang", jo.toString())
                        var message = getString(R.string.noti_error_try)
                        try {
                            message = jo.getString("message")
                        } catch (e: Exception) {
                            try {
                                message = jo.getString("error")
                            } catch (e: Exception) {

                            }
                        }
                        showNoti(message)
                    }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val paramsHeader = HashMap<String, String>()
                    paramsHeader["Authorization"] = account.idToken!!
                    return paramsHeader
                }
            }
            AppController.instance!!.addToRequestQueue(req)
        }
    }

    private fun updateUI(isBooked: Boolean) {
        if (isBooked) imvStar.setImageResource(R.drawable.ic_check)
        else imvStar.setImageResource(R.drawable.ic_playlist_plus)
    }

    fun showNoti(content: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(content)
        dialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.create()
        if (!isFinishing) dialog.show()
    }
}
