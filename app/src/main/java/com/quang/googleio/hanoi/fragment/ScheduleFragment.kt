package com.quang.googleio.hanoi.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_schedule.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ScheduleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager)

        val listRoom = arrayOf("Agenda", "Keynote", "Room 1", "Room 2", "Room 3")
        listRoom.forEach { room ->
            val bundle = Bundle()
            bundle.putString("name", room)
            val scheduleTabFragment = ScheduleTabFragment()
            scheduleTabFragment.arguments = bundle
            adapter.addFragment(scheduleTabFragment, room)
        }
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        tabLayout.setupWithViewPager(viewPager)
    }

}
