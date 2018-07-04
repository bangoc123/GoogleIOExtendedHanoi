package com.quang.googleio.hanoi.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.activity.RegisterActivity
import kotlinx.android.synthetic.main.fragment_register_now.*

/**
 * A simple [Fragment] subclass.
 *
 */
class RegisterNowFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_now, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegister.setOnClickListener { startActivityForResult(Intent(view.context, RegisterActivity::class.java), 1000) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            try {
                parentFragment!!.childFragmentManager.beginTransaction().replace(R.id.container, AgreeFragment()).commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
