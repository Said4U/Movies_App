package com.example.movies.view.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.movies.R
import com.example.movies.view.FavoritesFragment
import com.example.movies.view.PremiereFragment
import kotlinx.android.synthetic.main.fragment_card_top.*

class CardTopFragment : Fragment(R.layout.fragment_card_top) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewTop.setOnClickListener {
            setCurrentFragment("TOP_250_BEST_FILMS")
        }

        cardViewPopular.setOnClickListener {
            setCurrentFragment("TOP_100_POPULAR_FILMS")
        }

        cardViewAwait.setOnClickListener {
            setCurrentFragment("TOP_AWAIT_FILMS")
        }
    }

    private fun setCurrentFragment(topName: String) {
        val bundle = Bundle()
        bundle.putString("top", topName)
        val bestTopFragment = BestTopFragment.newInstance(bundle)

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.topPlaceholder, bestTopFragment)
            commit()
        }
    }

    companion object {
        fun newInstance() = CardTopFragment()
    }
}