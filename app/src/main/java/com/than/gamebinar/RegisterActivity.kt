package com.than.gamebinar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.than.gamebinar.databinding.ActivityRegisterBinding
import com.than.gamebinar.model.RegisterRequest
import com.than.gamebinar.model.RegisterResponse
import com.than.gamebinar.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            when {
                binding.etEmail.text.isEmpty() || binding.etUsername.text.isEmpty() || binding.etPassword.text.isEmpty() || binding.etConfirmPassword.text.isEmpty() -> {
                    Toast.makeText(this, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val data = RegisterRequest(
                        binding.etEmail.text.toString(),
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString()
                    )
                    ApiClient.instance.registerUser(data)
                        .enqueue(object : Callback<RegisterResponse> {
                            override fun onResponse(
                                call: Call<RegisterResponse>,
                                response: Response<RegisterResponse>
                            ) {
                                when (response.code()) {
                                    201 -> {
                                        AlertDialog.Builder(this@RegisterActivity)
                                            .setTitle("Success Register!")
                                            .setMessage(
                                                """
                                                            Id      : ${response.body()?.data?.id}
                                                            Username: ${response.body()?.data?.username}
                                                            Email   : ${response.body()?.data?.email}
                                                        """.trimIndent()
                                            )
                                            .create()
                                            .show()
                                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                    else -> {
                                        AlertDialog.Builder(this@RegisterActivity)
                                            .setTitle("Gagal Register!")
                                            .setMessage("Email atau Username sudah digunakan!")
                                            .create()
                                            .show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                AlertDialog.Builder(this@RegisterActivity)
                                    .setTitle("Gagal Register!")
                                    .setMessage("${t.message}")
                                    .create()
                                    .show()
                            }

                        })
                }
            }
        }

    }
}