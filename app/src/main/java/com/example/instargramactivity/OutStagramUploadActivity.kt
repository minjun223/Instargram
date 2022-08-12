package com.example.instargramactivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.Call
import android.util.Log
import com.google.android.gms.common.api.Response
import java.io.File

class OutStagramUploadActivity : AppCompatActivity() {
    lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_stagram_upload)

        view_pictures.setOnClickListener{
            getPicture()
        }
        upload_Post.setOnClickListener{
            uploadPost()
        }
        all_list.setOnClickListener{
            startActivity(Intent(this, OutStargramPostListActivity::class.java))
        }
        my_list.setOnClickListener{
            startActivity(Intent(this, OutstargramMyPostListActivity::class.java))
        }
        user_list.setOnClickListener{
            startActivity(Intent(this, OutStagramUserInfoActivity::class.java))
        }

    }

    fun getPicture(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            val uri: Uri = data!!.data!!
            val a = getImageFilePath(uri)
            Log.d("pathh", "path : "+a)
        }
    }

    fun getImageFilePath(contentUri: Uri): String{
        var columnIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }return cursor.getString(columnIndex)
    }
    fun uploadPost(filePath: String){
        val file = File(filePath)
        val fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("image", file.name,fileRequestBody)
        val content = RequestBody.create(MediaType.parse("text/pla"), getContent())
        (application as MasterApplication).service.uploadPost(
            part, content
        ).enqueue(object : Call.Callback<post>{
            override fun onFailure(call: Call<post>, t: Throwable){

            }
            override fun onResponse(call: Call<post>, response: Response<post>){
                if(response.isSuccessful){
                    val post = response.body()
                    Log.d("pathh", post!!.content)
                }
            }
        })
    }

    fun getContent():String{
        return content_input.text.toString()
    }
}