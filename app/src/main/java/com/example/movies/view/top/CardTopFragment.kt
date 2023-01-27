package com.example.movies.view.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.movies.R
import com.example.movies.view.PremiereFragment
import kotlinx.android.synthetic.main.fragment_card_top.*

class CardTopFragment : Fragment(R.layout.fragment_card_top) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewTop.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.topPlaceholder, BestTopFragment.newInstance())
                commit()
            }
        }
    }


    companion object {
        fun newInstance() = CardTopFragment()
    }
}