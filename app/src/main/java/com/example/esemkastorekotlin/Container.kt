package com.example.esemkastorekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.esemkastorekotlin.databinding.ActivityContainerBinding
import com.example.esemkastorekotlin.fragments.CartFragment
import com.example.esemkastorekotlin.fragments.HistoryFragment
import com.example.esemkastorekotlin.fragments.MenuFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.CompletionStage

class Container : AppCompatActivity() {
    lateinit var binding: ActivityContainerBinding
    private var fragments = listOf(MenuFragment(), CartFragment(), HistoryFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")

        val bundle = Bundle().apply {
            putString("name", name)
        }

        val menuFragment = MenuFragment().apply {
            arguments = bundle
        }

        val menus = listOf("Home", "Cart", "History")

        for (menu in menus) {
            val tab = binding.menuTl.newTab()
            tab.text = menu
            tab.tag = fragments[menus.indexOf(menu)]
            binding.menuTl.addTab(tab)
        }

        showFragment(menuFragment)

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(binding.containerFl.id)
            val index = fragments.indexOf(fragment)
            if (index != -1) binding.menuTl.getTabAt(index)?.select()
        }

        binding.menuTl.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    showFragment(it.tag as Fragment, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    fun showFragment(fragment: Fragment, addToBackStage: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.containerFl.id, fragment)
        if (addToBackStage) transaction.addToBackStack(null)
        transaction.commit()
    }
}