package com.quang.googleio.hanoi.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quang.googleio.hanoi.R

/**
 * A simple [Fragment] subclass.
 *
 */
class QRCodeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val status = arguments!!.getString("status")
        when (status) {
            "pending" -> {
                childFragmentManager.beginTransaction().replace(R.id.container, ResultRegisterFragment()).commit()
            }
            "approved" -> {
                val barcodeurl = arguments!!.getString("barcodeurl")
                val showQRCodeFragment = ShowQRCodeFragment()
                val bundle = Bundle()
                bundle.putString("barcodeurl", barcodeurl)
                showQRCodeFragment.arguments = bundle
                childFragmentManager.beginTransaction().replace(R.id.container, showQRCodeFragment).commit()
            }
            else -> childFragmentManager.beginTransaction().replace(R.id.container, NotificationRegisterFragment()).commit()
        }
    }
}
