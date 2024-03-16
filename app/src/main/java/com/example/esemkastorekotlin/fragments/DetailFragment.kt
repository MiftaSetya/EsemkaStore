package com.example.esemkastorekotlin.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.esemkastorekotlin.R
import com.example.esemkastorekotlin.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            var imageID = it.getString("id")
            binding.titleTv.text = it.getString("name")
            binding.descTv.text = it.getString("description")
            binding.priceTv.text= "Rp ${it.getString("price")}"
            binding.stockTv.text = "Stock : ${it.getString("stock")}"

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    var con = URL("http://10.0.2.2:5000/api/Home/Item/Photo/${imageID}").openConnection() as HttpURLConnection
                    con.requestMethod = "GET"
                    con.setRequestProperty("Content-Type", "image/png")

                    val image = BitmapFactory.decodeStream(con.inputStream)

                    GlobalScope.launch(Dispatchers.Main) {
                        binding.image.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    var defaulImage = BitmapFactory.decodeResource(resources, R.drawable.default_image)

                    GlobalScope.launch(Dispatchers.Main) {
                        binding.image.setImageBitmap(defaulImage)
                    }
                }

            }

            var itemCount = 1
            binding.itemCount.text.toString().toInt()
            val price = it.getString("price")?.toInt()
            val stock = it.getString("stock")?.toInt()

            fun updatePrice(binding: FragmentDetailBinding, count: Int, price: Int) {
                var totalPrice = price * count
                binding.totalPrice.text = "Rp. ${totalPrice},00"
            }

            if (price != null) {
                updatePrice(binding, itemCount, price)
            }

            binding.itemCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val input = s.toString()
                    if (input.isNotEmpty()) {
                        itemCount = input.toInt()
                    }
                }
            })

            binding.btnMin.setOnClickListener {
                if(itemCount > 1) {
                    itemCount--
                    binding.itemCount.setText(itemCount.toString())
                    if (price != null) {
                        updatePrice(binding, itemCount, price)
                    }
                }
                else {
                    Toast.makeText(context, "Jumlah barang tidak boleh kurang dari 1", Toast.LENGTH_LONG).show()
                }
            }

            binding.btnPlus.setOnClickListener {
                if (stock != null) {
                    if(itemCount < stock.toInt()) {
                        itemCount++
                        binding.itemCount.setText(itemCount.toString())
                        if (price != null) {
                            updatePrice(binding, itemCount, price)
                        }
                    }
                    else {
                        Toast.makeText(context, "Julah barang tidak boleh lebih dari stock", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return binding.root
    }
}