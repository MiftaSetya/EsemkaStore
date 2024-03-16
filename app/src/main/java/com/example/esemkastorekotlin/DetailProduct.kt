package com.example.esemkastorekotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.esemkastorekotlin.databinding.ActivityDetailProductBinding

class DetailProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleToolbar.title = intent.getStringExtra("name")
        binding.titleTv.text = intent.getStringExtra("name")
        binding.descTv.text = intent.getStringExtra("description")
        binding.priceTv.text = "Rp. ${intent.getIntExtra("price", 0)}.00"
        binding.stockTv.text = "Stock : ${intent.getIntExtra("stock", 0)}"

        var itemCount = 1
        binding.itemCount.text.toString().toIntOrNull()
        val price = intent.getIntExtra("price", 0)
        var stock = intent.getIntExtra("stock", 0)

        fun updateTotalPrice(binding: ActivityDetailProductBinding ,count: Int, price: Int) {
            val totalPrice = count * price
            binding.totalPrice.text = "Total Price : Rp.${totalPrice}.00"
        }

        updateTotalPrice(binding, itemCount, price)

        binding.itemCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.isNotEmpty()) {
                    itemCount = input.toInt()
                }
            }
        })

        binding.btnMin.setOnClickListener {
            if (itemCount > 1) {
                itemCount --
                binding.itemCount.setText(itemCount.toString())
                updateTotalPrice(binding, itemCount, price)
            }
            else
            {
                Toast.makeText(this, "Jumlah barang tidak boleh kurang dari 1", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnPlus.setOnClickListener {
            if (itemCount < stock) {
                itemCount ++
                binding.itemCount.setText(itemCount.toString())
                updateTotalPrice(binding, itemCount, price)
            }
            else
            {
                Toast.makeText(this, "Jumlah barang melebihi stock, stock barang adalah ${stock}", Toast.LENGTH_LONG).show()
            }
        }
    }
}