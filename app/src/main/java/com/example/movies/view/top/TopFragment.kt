package com.example.movies.view.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.movies.R

class TopFragment : Fragment(R.layout.fragment_top) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().apply {
            replace(R.id.topPlaceholder, CardTopFragment.newInstance())
            commit()


        }
    }

    companion object {
        fun newInstance() = TopFragment()
    }
}