package com.quang.googleio.hanoi.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.adapter.MyTopicsAdapter
import com.quang.googleio.hanoi.app.AppController
import com.quang.googleio.hanoi.model.Topic
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val account = GoogleSignIn.getLastSignedInAccount(view.context)
        Glide.with(view.context).load(account!!.photoUrl).into(imvAvatar)
        tvName.text = account.displayName
        tvEmail.text = account.email

        rvTopics.layoutManager = LinearLayoutManager(view.context)
        val listTopic = ArrayList<Topic>()
        try {
            val topics = JSONArray(arguments!!.getString("topics"))
            for (i in 0 until topics.length()) {
                val topic = topics.getJSONObject(i)
                val speakers = topic.getJSONArray("speaker").toString()
                val id = topic.getString("_id")
                val level = topic.getString("level")
                val duration = topic.getString("duration")
                val name = topic.getString("name")
                val content = topic.getString("content")
                val location = topic.getString("location")
                val color = topic.getString("color")
                val topictype = topic.getString("topictype")
                val timestart = topic.getString("timestart")
                val timeend = topic.getString("timeend")
                val start = topic.getString("start")

                listTopic.add(Topic(id, level, duration, name, content, location, color, topictype, timestart, timeend, start, speakers, true))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val adapter = MyTopicsAdapter(listTopic)
        rvTopics.adapter = adapter

        adapter.setOnDeleteListener { _, position ->
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage(getString(R.string.alert_delete_topic))
            builder.setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                dialog.dismiss()
                if (progressBar != null) progressBar.visibility = View.VISIBLE
                val params = JSONObject()
                params.put("id", listTopic[position].id)
                val req = object : JsonObjectRequest(
                        Method.POST,
                        "https://ioextended.org/api/reservation/cancel",
                        params,
                        Response.Listener {
                            listTopic.remove(listTopic[position])
                            adapter.notifyDataSetChanged()
                            if (progressBar != null) progressBar.visibility = View.INVISIBLE
                        },
                        Response.ErrorListener { e ->
                            e.printStackTrace()
                            Toast.makeText(view.context, getString(R.string.noti_error_try), Toast.LENGTH_LONG).show()
                            if (progressBar != null) progressBar.visibility = View.INVISIBLE
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
            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            builder.create()
            if (!activity!!.isFinishing) builder.show()
        }


    }

}
