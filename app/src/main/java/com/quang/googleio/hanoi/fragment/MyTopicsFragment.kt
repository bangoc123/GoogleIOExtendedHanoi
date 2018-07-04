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

/**
 * A simple [Fragment] subclass.
 *
 */
class MyTopicsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val status = arguments!!.getString("status")
        when (status) {
            "step_1" -> {
                childFragmentManager.beginTransaction().replace(R.id.container, RegisterNowFragment()).commit()
                startActivityForResult(Intent(context, RegisterActivity::class.java), 1000)
            }
            "pending" -> {
                childFragmentManager.beginTransaction().replace(R.id.container, ResultRegisterFragment()).commit()
            }
            "step_3" -> {
                childFragmentManager.beginTransaction().replace(R.id.container, AgreeFragment()).commit()
            }
            "approved" -> {
                val profileFragment = ProfileFragment()
                val topics = arguments!!.getString("topics")
                val bundle = Bundle()
                bundle.putString("topics", topics)
                profileFragment.arguments = bundle
                childFragmentManager.beginTransaction().replace(R.id.container, profileFragment).commit()
            }
            else -> childFragmentManager.beginTransaction().replace(R.id.container, LoginFragment()).commit()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            childFragmentManager.beginTransaction().replace(R.id.container, AgreeFragment()).commit()
        }
    }
}
