package com.example.esemkastorekotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.esemkastorekotlin.databinding.ActivityLoginBinding
import com.example.esemkastorekotlin.databinding.ActivityMainBinding
import com.example.esemkastorekotlin.fragments.DetailFragment
import com.example.esemkastorekotlin.fragments.MenuFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                var url = URL("http://10.0.2.2:5000/api/login")
                var con = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("Content-Type", "application/json")

                var dataAuth = JSONObject().apply {
                    put("email", binding.emailEt.text)
                    put("password", binding.pwEt.text)
                }
                con.outputStream.write(dataAuth.toString().toByteArray())

                var code = con.responseCode

                if (code == 200) {
                    var jsonStr = con.inputStream.bufferedReader().readText()
                    var user = JSONObject(jsonStr)

                    runOnUiThread {
                        var editor = getSharedPreferences("random", Context.MODE_PRIVATE).edit()
                        editor.putString("user", jsonStr)
                        editor.apply()

                        val intent = Intent(this@Login, Container::class.java).apply {
                            putExtra("name", user.getString("name"))
                            putExtra("id", user.getInt("id"))
                        }

/*                        val bundle = Bundle().apply {
                            putString("name", user.getString("name"))
                        }

                        MenuFragment().arguments = bundle*/

                        startActivity(intent)
                        Toast.makeText(this@Login, "Selamat datang ${user.getString("name")}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                else {
                    var jsonStr = con.errorStream.bufferedReader().readText()
                    var error = JSONObject(jsonStr)

                    runOnUiThread {
                        Toast.makeText(this@Login, "Authentication error, reason : ${error.getString("message")}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}