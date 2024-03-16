package com.example.esemkastorekotlin

import android.content.Context
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.esemkastorekotlin.databinding.ActivityMainBinding
import com.example.esemkastorekotlin.databinding.ListItemBinding
import com.example.esemkastorekotlin.fragments.CartFragment
import com.example.esemkastorekotlin.fragments.HistoryFragment
import com.example.esemkastorekotlin.fragments.MenuFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*        var fragment = listOf(MenuFragment(), CartFragment(), HistoryFragment())
        var menuNames = listOf("Menu", "Cart", "History")

        binding.menuVp.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int = fragment.size
            override fun createFragment(pos: Int): Fragment = fragment[pos]
        }

        TabLayoutMediator(binding.menuTl, binding.menuVp) { tab, pos ->
            tab.text = menuNames[pos]
        }.attach()*/
    }
}