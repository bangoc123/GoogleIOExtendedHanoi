package com.quang.googleio.hanoi.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_io.*


/**
 * A simple [Fragment] subclass.
 *
 */
class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(EventFragment(), getString(R.string.menu_event))
        adapter.addFragment(FAQFragment(), getString(R.string.menu_faq))
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

}
