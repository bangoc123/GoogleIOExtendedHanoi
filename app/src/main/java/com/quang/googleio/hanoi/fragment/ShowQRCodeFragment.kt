package com.quang.googleio.hanoi.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.quang.googleio.hanoi.R
import kotlinx.android.synthetic.main.fragment_show_qrcode.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ShowQRCodeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_qrcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barcodeurl = arguments!!.getString("barcodeurl")
        try {
            Glide.with(view).load(barcodeurl).into(imvQRCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
