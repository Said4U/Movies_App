package com.example.movies.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.movies.R
import com.example.movies.view.adapter.ViewPagerAdapter
import com.example.movies.view.top.TopFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val fragList = listOf(
        RecommendationFragment.newInstance(),
        TopFragment.newInstance(),
        PremiereFragment.newInstance()
    )



    companion object{
        fun getNewInstance(userId: Bundle): HomeFragment {
            val homeFragment = HomeFragment()
            homeFragment.arguments = userId
            return homeFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragListTitle = listOf(
            getString(R.string.recommendation),
            getString(R.string.top),
            getString(R.string.premiere)
        )

        placeHolder.adapter = ViewPagerAdapter(requireActivity(), fragList)

        TabLayoutMediator(tabLayout, placeHolder){
            tab, pos -> tab.text = fragListTitle[pos]
        }.attach()
    }

}