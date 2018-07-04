package com.quang.googleio.hanoi.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.quang.googleio.hanoi.BuildConfig
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.app.AppController
import com.quang.googleio.hanoi.app.ForceUpdateChecker
import com.quang.googleio.hanoi.controller.MySQLiteController
import com.quang.googleio.hanoi.model.Topic
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        firebaseRemoteConfig.setConfigSettings(configSettings)

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = BuildConfig.VERSION_CODE
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)

        firebaseRemoteConfig.fetch(3600)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseConfig", "remote config is fetched.")
                        firebaseRemoteConfig.activateFetched()
                    } else Log.e("FirebaseConfig", task.exception.toString())
                }

        Glide.with(this).load(R.drawable.io).into(imvSplash)

        val controller = MySQLiteController(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id), false)
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) googleSignInClient.silentSignIn()

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)


        val req = JsonObjectRequest(
                "https://ioextended.org/api/topics",
                null,
                Response.Listener { response ->
                    controller.deleteAllTopic()
                    val topics = response.getJSONArray("topics")
                    for (i in 0 until topics.length()) {
                        val topic = topics.getJSONObject(i)
                        val speaker = topic.getString("speaker")
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

                        controller.insertTopic(Topic(id, level, duration, name, content, location, color, topictype, timestart, timeend, start, speaker, false))
                    }
                },
                Response.ErrorListener { e ->
                    e.printStackTrace()
                })
        AppController.instance!!.addToRequestQueue(req)
    }
}
