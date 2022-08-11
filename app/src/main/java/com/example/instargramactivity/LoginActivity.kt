package com.example.instargramactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register.setOnClickListener{
            val intent = Intent(this, EmailSignupActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener{
            val user = username_inpubox.text.toString()
            val password = password_inpubox.text.toString()
            (application as MasterApplication).service.login(
                username.password
            ).enqueue(object : Callback<User>){
                override fun onFailure(call: Call<User>, t: Throwable){

                }
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {


                        val user = response.body()
                        val token = user!!.token!!
                        saveUserToken(token, this@LoginActivity)
                        (application as MasterApplication).createRetrofit()
                        Toast.makeText(this@LoginActivity,
                            "로그인 하셨습니다.", Toast.LENGTH_LONG).show()
                        startActivity(
                            Intent(this@LoginActivitym OutStagramPostListActivity::class.java)
                        )

                    }
                }
            }
        }
    }

    fun saveUserToken(token:String, activity: Activity){
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }

}