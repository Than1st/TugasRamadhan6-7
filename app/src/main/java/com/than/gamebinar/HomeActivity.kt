package com.than.gamebinar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.than.gamebinar.MainActivity.Companion.SP_FILE
import com.than.gamebinar.databinding.ActivityHomeBinding
import com.than.gamebinar.databinding.LayoutEditBinding
import com.than.gamebinar.model.EditRequest
import com.than.gamebinar.model.RegisterResponse
import com.than.gamebinar.service.ApiClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = this.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "default_token")
        binding.btnEdit.setOnClickListener {
            showEdit(token)
        }
        when {
            token != "default_token" -> {
                getAuth(token)
            }
        }
    }

    private fun showEdit(token: String?) {
        val dialogBinding = LayoutEditBinding.inflate(LayoutInflater.from(this))
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogBinding.root)
        val dialog = dialogBuilder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        ApiClient.instance.getUser("Bearer $token").enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                when{
                    response.code() == 200 -> {
                        val data = response.body()!!.data
                        binding.apply {
                            dialogBinding.etEmail.setText(data.email)
                            dialogBinding.etUsername.setText(data.username)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
        //btn edit
        dialogBinding.btnEdit.setOnClickListener {
            val username = dialogBinding.etUsername.text.toString()
            val email = dialogBinding.etEmail.text.toString()
            ApiClient.instance.updateUser(
                "Bearer $token",
                username.toRequestBody("text/plain".toMediaType()),
                email.toRequestBody("text/plain".toMediaType())
            ).enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    when{
                        response.code() == 200 -> {
                            Toast.makeText(this@HomeActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
                            getAuth(token)
                            dialog.dismiss()
                        }
                        else -> {
                            Toast.makeText(this@HomeActivity, "Update Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getAuth(token: String?) {
        ApiClient.instance.getUser("Bearer $token").enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                when{
                    response.code() == 200 -> {
                        val data = response.body()!!.data
                        binding.apply {
                            tvId.text = data.id
                            tvEmail.text = data.email
                            tvUsername.text = data.username
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}