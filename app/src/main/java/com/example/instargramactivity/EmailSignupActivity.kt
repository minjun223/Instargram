package com.example.instargramactivity

import android.app.Activity
import android.app.Person
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.Response
import retrofit2.Callback

class EmailSignupActivity : AppCompatActivity() {

    lateinit var usernameView: EditText
    lateinit var userPassword1View: EditText
    lateinit var userPassword2View: EditText
    lateinit var registerBtn: TextView
    lateinit var loginBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if((application as MasterApplication)).checkIsLogin(){
            finish()
            startActivity(Intent(this, OutStargramPostListActivity::class.java))
        }else{
            setContentView(R.layout.activity_email_signup)
            initView(this@EmailSignupActivity)
            setUpListener(this)
        }

    }
    fun setUpListener(activity: Activity){
        registerBtn.setOnClickListener{
            register(this@EmailSignupActivity)
        }
        loginBtn.setOnClickListener {
          startActivity(
              Intent(this@EmailSignupActivity, LoginActivity::class.java)
          )
//            val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
//            val token = sp.getString("login_sp", "")
//            Log.d("abcc", "token : " + token)
        }
    }

    fun register(){
        val username = getUserName()
        val password1 = getUserPassword()
        val password2 = getUserPassword2()
        val register = Register(username, password1, password2)

        (application as MasterApplication).service.register(
            username, password1, password2
        ).enqueue(object :
            Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(activity, "가입에 실패 하였습니다.", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "가입에 실패 하였습니다.", Toast.LENGTH_LONG).show()
                    val user = response.body()
                    val token = user.token
                    saveUserToken(token, activity)
                }
            }
        })
    }
    fun saveUserToken(token: String, activity: Activity){
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }
    fun initView(activity: Activity){
        usernameView = activity.findViewById(R.id.username_inputbox)
        userPassword1View = activity.findViewById(R.id.password_inputbox)
        userPassword2View = activity.findViewById(R.id.password2_inputbox)
        registerBtn = activity.findViewById(R.id.register)
        loginBtn = activity.findViewById(R.id.login)
    }
    fun getUserName():String{
        return usernameView.text.toString()
    }
    fun getUserPassword(): String{
        return userPassword1View.text.toString()
    }
    fun getUserPassword2(): String{
        return userPassword2View.text.toString()
    }
}