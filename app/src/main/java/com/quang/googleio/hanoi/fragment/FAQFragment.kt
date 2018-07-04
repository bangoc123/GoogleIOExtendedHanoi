package com.quang.googleio.hanoi.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.adapter.FAQAdapter
import com.quang.googleio.hanoi.model.Question
import kotlinx.android.synthetic.main.fragment_faq.*

/**
 * A simple [Fragment] subclass.
 *
 */
class FAQFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFAQ.layoutManager = LinearLayoutManager(view.context)
        val animator = rvFAQ.itemAnimator
        if (animator is DefaultItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        val listQuestion = ArrayList<Question>()
        listQuestion.add(Question(getString(R.string.question_1),
                getString(R.string.answer_1)))
        listQuestion.add(Question(getString(R.string.question_2),
                getString(R.string.answer_2)))
        listQuestion.add(Question(getString(R.string.question_3),
                getString(R.string.answer_3)))
        val adapter = FAQAdapter(listQuestion)
        rvFAQ.adapter = adapter
    }

}
