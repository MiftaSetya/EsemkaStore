package com.example.esemkastorekotlin

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkastorekotlin.databinding.ActivityHomeBinding
import com.example.esemkastorekotlin.databinding.ListItemBinding
import com.example.esemkastorekotlin.fragments.MenuFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                                    val defaultImage = ContextCompat.getDrawable(holder.itemView.context, R.drawable.default_image)
                                    holder.binding.image.setImageDrawable(defaultImage)
                                }
                            }
                        }

                        val intent = Intent(this@Home, DetailProduct::class.java).apply {
                            putExtra("id", item.getInt("id"))
                            putExtra("name", item.getString("name"))
                            putExtra("description", item.getString("description"))
                            putExtra("price", item.getInt("price"))
                            putExtra("stock", item.getInt("stock"))
                        }
                        holder.itemView.setOnClickListener {
                            startActivity(intent)
                        }
                    }
                }
                binding.menuRv.adapter = adapter
                binding.menuRv.layoutManager = LinearLayoutManager(this@Home, RecyclerView.VERTICAL, false)
            }
        }
    }
}