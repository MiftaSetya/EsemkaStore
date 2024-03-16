package com.example.esemkastorekotlin.fragments

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkastorekotlin.Container
import com.example.esemkastorekotlin.DetailProduct
import com.example.esemkastorekotlin.Home
import com.example.esemkastorekotlin.R
import com.example.esemkastorekotlin.databinding.FragmentMenuBinding
import com.example.esemkastorekotlin.databinding.ListItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var binding = FragmentMenuBinding.inflate(inflater, container, false)

        arguments?.let {
            binding.welocmeTv.text = "Welcome ${it.getString("name")}"
        }

        GlobalScope.launch(Dispatchers.IO) {
            var productStr = URL("http://10.0.2.2:5000/api/Home/Item").openStream().bufferedReader().readText()
            var product = JSONArray(productStr)

            GlobalScope.launch(Dispatchers.Main) {
                var adapter = object : RecyclerView.Adapter<MenuFragment.ProductViewHolder>() {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): MenuFragment.ProductViewHolder {
                        var binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                        return MenuFragment.ProductViewHolder(binding)
                    }

                    override fun getItemCount(): Int = product.length()

                    override fun onBindViewHolder(holder: MenuFragment.ProductViewHolder, pos: Int) {
                        val item = product.getJSONObject(pos)
                        holder.binding.titleTv.text = item.getString("name")
                        holder.binding.descTv.text = item.getString("description")
                        holder.binding.priceTv.text = "Rp. ${item.getInt("price")}.00"

                        GlobalScope.launch(Dispatchers.IO) {

                            var con = URL("http://10.0.2.2:5000/api/Home/Item/Photo/${item.getInt("id")}").openConnection() as HttpURLConnection
                            con.requestMethod = "GET"
                            con.setRequestProperty("Content-Type", "image/png")

                            try {
                                val image = BitmapFactory.decodeStream(con.inputStream)

                                GlobalScope.launch(Dispatchers.Main) {
                                    holder.binding.image.setImageBitmap(image)
                                }
                            }
                            catch (e: Exception) {
                                GlobalScope.launch(Dispatchers.Main) {
                                    val image = ContextCompat.getDrawable(holder.itemView.context, R.drawable.default_image)
                                    holder.binding.image.setImageDrawable(image)
                                }
                            }
                        }

                        holder.itemView.setOnClickListener {
                            if (activity is Container) {
                               val fragment = DetailFragment().apply {
                                   arguments = Bundle().apply {
                                       putString("id", item.getInt("id").toString())
                                       putString("name", item.getString("name"))
                                       putString("description", item.getString("description"))
                                       putString("price", item.getInt("price").toString())
                                       putString("stock", item.getInt("stock").toString())
                                   }
                               }
                               (activity as Container).showFragment(fragment, true)
                           }
                        }
                    }
                }
                binding.menuRv.adapter = adapter
                binding.menuRv.layoutManager = GridLayoutManager(context, 2)
            }
        }
        return binding.root
    }
    class ProductViewHolder(val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root)
}