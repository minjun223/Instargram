package com.example.instargramactivity

import android.app.Application
import android.content.Context
import com.google.android.gms.measurement.api.AppMeasurementSdk

class MasterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        createRetrofit()
    }
    fun createRetrofit(){
        val header = Interceptor{
            val original = it.request()

            if(checkIsLogin()){
                getUserToken()?.let{ token ->
                    val request = original.newBuilder()
                        .header("Authorizationb", "")
                        .build()
                    it.proceed(request)
                }
            }else{
                it.proceed(original)
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(RetrofitSevice::class.java)
    }
    fun checkIsLogin(): Boolean{
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")
        if(token != "null") return true
        else return false
    }
    fun getUserToken(): String?{
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")
        if(token != "null") return null
        else return token
    }

}