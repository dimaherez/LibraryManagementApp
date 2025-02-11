package com.example.librarymanagementapp.home

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.librarymanagementapp.databinding.FragmentHomeBinding
import com.example.librarymanagementapp.home.all_books.AllBooksFragment
import com.example.librarymanagementapp.home.favorite_books.FavoriteBooksFragment
import com.example.librarymanagementapp.home.trending.TrendingBooksFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        viewPager.adapter = ViewPagerAdapter(
            fragments = listOf(
                AllBooksFragment(),
                TrendingBooksFragment(),
                FavoriteBooksFragment()
            ),
            fragmentActivity = requireActivity()
        )

        viewPager.offscreenPageLimit = 3

        TabLayoutMediator( tabLayout, viewPager) { tab, index ->
            tab.text = when(index) {
                0 -> "All Books📖"
                1 -> "Trending🔥"
                2 -> "Favorite🌟"
                else -> throw NotFoundException("Position not found")
            }
        }.attach()
    }


}