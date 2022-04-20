package com.than.gamebinar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.than.gamebinar.databinding.ActivityMainBinding
import com.than.gamebinar.model.LoginRequest
import com.than.gamebinar.model.LoginResponse
import com.than.gamebinar.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object{
        const val SP_FILE = "kotlinsharedpreferences"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val sharedpreferences = this.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
        val token = sharedpreferences.getString("token", "default_token")
        if (token != "default_token"){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()){
                Toast.makeText(this, "Form tidak boleh Kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val data = LoginRequest(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
                ApiClient.instance.loginUser(data).enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val code = response.code()
                        val body = response.body()!!.data
                        if(code == 200){
                            val editor = sharedpreferences.edit()
                            editor.putString("token", body.token)
                            editor.apply()
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Success Login!")
                                .setMessage("""
                                    Id      : ${body.id}
                                    Username: ${body.username}
                                    Email   : ${body.email}
                                """.trimIndent())
                                .create()
                                .show()
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Gagal Login!")
                                .setMessage("Pastikan Akun sudah terdaftar!")
                                .create()
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }
}