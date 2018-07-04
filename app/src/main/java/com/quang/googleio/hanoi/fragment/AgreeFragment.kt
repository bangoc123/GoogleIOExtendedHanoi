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
import com.quang.googleio.hanoi.app.AppController
import kotlinx.android.synthetic.main.fragment_agree.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AgreeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(view.context)

        btnAgree.setOnClickListener {
            if (progressBar != null) progressBar.visibility = View.VISIBLE
            val req = object : JsonObjectRequest(
                    Method.POST,
                    "https://ioextended.org/api/pending",
                    null,
                    Response.Listener { response ->
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                        val status = response.getString("status")
                        if (status == "pending") {
                            parentFragment!!.childFragmentManager.beginTransaction().replace(R.id.container, ResultRegisterFragment()).commit()
                        }
                    },
                    Response.ErrorListener { e ->
                        e.printStackTrace()
                        if (progressBar != null) progressBar.visibility = View.INVISIBLE
                    }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val paramsHeader = HashMap<String, String>()
                    paramsHeader["Authorization"] = account!!.idToken!!
                    return paramsHeader
                }
            }
            AppController.instance!!.addToRequestQueue(req)
        }
    }
}
