package com.example.movies.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movies.view.HomeFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val list: List<Fragment>): FragmentStateAdapter(fragmentActivity)  {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}