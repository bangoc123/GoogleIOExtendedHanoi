package com.quang.googleio.hanoi.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.adapter.ViewPagerAdapter
import com.quang.googleio.hanoi.app.AppController
import kotlinx.android.synthetic.main.fragment_my_io.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MyIOFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_io, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myTopicsFragment = MyTopicsFragment()
        val qrCodeFragment = QRCodeFragment()

        val bundle = Bundle()
        bundle.putString("status", "")
        myTopicsFragment.arguments = bundle
        qrCodeFragment.arguments = bundle

        val account = GoogleSignIn.getLastSignedInAccount(view.context)
        if (account == null) {
            try {
                val adapter = ViewPagerAdapter(childFragmentManager)
                adapter.addFragment(myTopicsFragment, getString(R.string.menu_my_topics))
                adapter.addFragment(qrCodeFragment, getString(R.string.menu_qrcode))
                viewPager.adapter = adapter
                tabLayout.setupWithViewPager(viewPager)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            if (progressBar != null) progressBar.visibility = View.VISIBLE
            val req = object : JsonObjectRequest(
                    "https://ioextended.org/api/me",
                    null,
                    Response.Listener { response ->
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                        try {
                            val me = response.getJSONObject("me")
                            val status = me.getString("status")
                            bundle.putString("status", status)
                            when (status) {
                                "approved" -> {
                                    val topics = me.getJSONArray("topics")
                                    bundle.putString("topics", topics.toString())
                                    bundle.putString("barcodeurl", me.getString("barcodeurl"))
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        try {
                            val adapter = ViewPagerAdapter(childFragmentManager)
                            adapter.addFragment(myTopicsFragment, getString(R.string.menu_my_topics))
                            adapter.addFragment(qrCodeFragment, getString(R.string.menu_qrcode))
                            viewPager.adapter = adapter
                            tabLayout.setupWithViewPager(viewPager)
                        } catch (e: Exception) {
                        }
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                    }
            ) {
                override fun getHeaders(): HashMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Authorization"] = account.idToken!!
                    return params
                }
            }
            AppController.instance!!.addToRequestQueue(req)
        }
    }
}
