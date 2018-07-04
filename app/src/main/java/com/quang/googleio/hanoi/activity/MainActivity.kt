package com.quang.googleio.hanoi.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.app.ForceUpdateChecker
import com.quang.googleio.hanoi.controller.MySQLiteController
import com.quang.googleio.hanoi.fragment.InfoFragment
import com.quang.googleio.hanoi.fragment.MapFragment
import com.quang.googleio.hanoi.fragment.MyIOFragment
import com.quang.googleio.hanoi.fragment.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class MainActivity : AppCompatActivity(), ForceUpdateChecker.OnUpdateNeededListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check()

        val controller = MySQLiteController(this)

        bottomBar.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.tab_schedule -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    tvTitle.text = getString(R.string.menu_schedule)
                    fragmentTransaction.replace(R.id.contentContainer, ScheduleFragment())
                    fragmentTransaction.commit()
                }
                R.id.tab_map -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.contentContainer, MapFragment())
                    tvTitle.text = getString(R.string.menu_map)
                    fragmentTransaction.commit()
                }
                R.id.tab_my_io -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.contentContainer, MyIOFragment())
                    tvTitle.text = getString(R.string.menu_my_io)
                    fragmentTransaction.commit()
                }
                R.id.tab_info -> {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.contentContainer, InfoFragment())
                    tvTitle.text = getString(R.string.menu_info)
                    fragmentTransaction.commit()
                }
            }
        }

        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) {
            tvSignOut.visibility = View.VISIBLE
            Log.d("idToken", account.idToken)
        } else tvSignOut.visibility = View.INVISIBLE

        tvSignOut.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.server_client_id))
                    .requestEmail()
                    .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(applicationContext, gso)
            mGoogleSignInClient.signOut().addOnCompleteListener {
                tvSignOut.visibility = View.INVISIBLE
                controller.resetStatusAllTopic()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                tvTitle.text = getString(R.string.menu_schedule)
                fragmentTransaction.replace(R.id.contentContainer, ScheduleFragment())
                fragmentTransaction.commit()
                bottomBar.selectTabAtPosition(0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id), false)
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) googleSignInClient.silentSignIn()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val dialog = AlertDialog.Builder(this)
            dialog.setMessage(getString(R.string.alert_exit_app))
            dialog.setPositiveButton(getString(R.string.yes)) { _, _ -> finish() }
            dialog.setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            dialog.create()
            if (!isFinishing) dialog.show()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onUpdateNeeded(updateUrl: String) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_update_app))
                .setCancelable(false)
                .setMessage(getString(R.string.message_update_app))
                .setPositiveButton(getString(R.string.update)
                ) { _, _ -> redirectStore(updateUrl) }.setNegativeButton(getString(R.string.no_thanks)
                ) { _, _ -> finish() }.create()
        if (!isFinishing) dialog.show()
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
