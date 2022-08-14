package com.example.instargramactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OutStagramUserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_stagram_user_info)

        all_list.setOnClickListener{startActivity(Intent(this, OutStargramPostListActivity::class.java))}
        my_list.setOnClickListener{startActivity(Intent(this, OutstargramMyPostListActivity::class.java))}
        upload.setOnClickListener{startActivity(Intent(this, OutStagramUploadActivity::class.java))}

        logout.setOnClickListener{
            val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("login_sp", "null")
            (application as MasterApplication).createRetrofit()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}