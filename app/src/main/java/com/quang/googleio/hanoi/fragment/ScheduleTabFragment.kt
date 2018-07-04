package com.quang.googleio.hanoi.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.activity.DetailTopicActivity
import com.quang.googleio.hanoi.adapter.ScheduleAdapter
import com.quang.googleio.hanoi.app.AppController
import com.quang.googleio.hanoi.controller.MySQLiteController
import com.quang.googleio.hanoi.model.Topic
import kotlinx.android.synthetic.main.fragment_schedule_tab.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ScheduleTabFragment : Fragment() {

    private lateinit var controller: MySQLiteController
    private lateinit var adapter: ScheduleAdapter
    private lateinit var listTopic: ArrayList<Topic>
    private lateinit var roomName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = MySQLiteController(view.context)

        rvSchedule.layoutManager = LinearLayoutManager(view.context)

        val account = GoogleSignIn.getLastSignedInAccount(view.context)
        roomName = arguments!!.getString("name")
        listTopic = when (roomName) {
            "Agenda" -> controller.allTopics
            "Keynote" -> controller.getAllTopicsByRoom("Keynote Room")
            else -> controller.getAllTopicsByRoom(roomName)
        }
        adapter = ScheduleAdapter(listTopic)
        rvSchedule.adapter = adapter

        adapter.setOnItemListener { _, position ->
            val intent = Intent(activity!!, DetailTopicActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", listTopic[position].name)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.UK)
            val sdf2 = SimpleDateFormat("HH:mm", Locale.UK)
            bundle.putString("time", sdf2.format(Date(sdf.parse(listTopic[position].timestart).time + 7 * (60 * 60 * 1000)))
                    + " - " + sdf2.format(Date(sdf.parse(listTopic[position].timeend).time + 7 * (60 * 60 * 1000))))
            bundle.putString("room", listTopic[position].location)
            bundle.putString("content", listTopic[position].content)
            bundle.putString("speaker", listTopic[position].speaker)
            bundle.putBoolean("isBooked", listTopic[position].isBooked)
            bundle.putString("id", listTopic[position].id)
            bundle.putString("color", listTopic[position].color)

            intent.putExtra("topic", bundle)
            startActivity(intent)
        }

        adapter.setOnStarListener { _, position ->
            if (GoogleSignIn.getLastSignedInAccount(view.context) == null) {
                showNoti(getString(R.string.notifi_you_must_login))
                return@setOnStarListener
            }
            if (progressBar != null) progressBar.visibility = View.VISIBLE
            val url = if (listTopic[position].isBooked) {
                "https://ioextended.org/api/reservation/cancel"
            } else {
                "https://ioextended.org/api/reservation"
            }
            val params = JSONObject()
            params.put("id", listTopic[position].id)
            val req = object : JsonObjectRequest(
                    Method.POST,
                    url,
                    params,
                    Response.Listener {
                        listTopic[position].isBooked = !listTopic[position].isBooked
                        controller.updateStatusOfTopic(listTopic[position].id, listTopic[position].isBooked)
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                        adapter.notifyDataSetChanged()
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
                    if (account != null)
                        paramsHeader["Authorization"] = account.idToken!!
                    return paramsHeader
                }
            }
            AppController.instance!!.addToRequestQueue(req)
        }

        if (account != null) {
            val meReq = object : JsonObjectRequest(
                    "https://ioextended.org/api/me",
                    null,
                    Response.Listener { response ->
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                        try {
                            val me = response.getJSONObject("me")
                            val status = me.getString("status")
                            when (status) {
                                "approved" -> {
                                    val topics = me.getJSONArray("topics")
                                    for (i in 0 until topics.length()) {
                                        val topic = topics.getJSONObject(i)
                                        val id = topic.getString("_id")
                                        controller.updateStatusOfTopic(id, true)
                                        val tp = Topic(id)
                                        val index = listTopic.indexOf(tp)
                                        if (index >= 0) listTopic[index].isBooked = true
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        adapter.notifyDataSetChanged()
                    },
                    Response.ErrorListener { e ->
                        e.printStackTrace()
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                    }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Authorization"] = account.idToken!!
                    return params
                }
            }
            AppController.instance!!.addToRequestQueue(meReq)
        } else if (progressBar != null) progressBar.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        listTopic.clear()
        adapter.notifyDataSetChanged()
        val list = when (roomName) {
            "Agenda" -> controller.allTopics
            "Keynote" -> controller.getAllTopicsByRoom("Keynote Room")
            else -> controller.getAllTopicsByRoom(roomName)
        }
        for (topic in list) {
            listTopic.add(topic)
        }
        adapter.notifyDataSetChanged()
    }

    fun showNoti(content: String) {
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setMessage(content)
        dialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.create()
        if (!activity!!.isFinishing) dialog.show()
    }
}
