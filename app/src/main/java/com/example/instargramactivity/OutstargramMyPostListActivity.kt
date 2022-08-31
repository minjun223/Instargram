package com.example.instargramactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Response
import javax.security.auth.callback.Callback

class OutstargramMyPostListActivity : AppCompatActivity() {
    lateinit var myPostRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outstargram_my_post_list)

        myPostRecyclerView = mypost_recyclerview
        glide = Glide.with(this@OutstargramMyPostListActivity)
        createList()
        user_info.setOnClickListener{startActivity(Intent(this, OutStagramUserInfoActivity::class.java))}
        my_list.setOnClickListener{startActivity(Intent(this, OutstargramMyPostListActivity::class.java))}
        upload.setOnClickListener{startActivity(Intent(this, OutStargramPostListActivity::class.java))}
    }
    fun createList(recyclerView: RecyclerView){
        (application as MasterApplication).service.getUserPostList().enqueue(
            object : Callback<ArrayList<post>> {
                override fun onFailure(call: Call<ArrayList<Post>>, tL Throwable) {
                    Log.d("123", "error")
                }

                override fun onResponse(
                    call: Call<ArrayList<post>>,
                    response: Response<ArrayList<post>>
                ){
                    Log.d("123123", "error : "+response.body())
                    if (response.isSuccessful){
                        val myPostList = response.body()
                        val adapter = MyPostAdapter(
                            myPostList!!,
                            LayoutInflater.from(this@OutstargramMyPostListActivity),
                            glide
                        )
                        myPostRecyclerView.adapter = adapter
                        myPostRecyclerView.layoutManager = LinearLayoutManager(this@OutstargramMyPostListActivity)


                    }
                }
            }
        )
    }
}
class PostAdapter(
    var postList:ArrayList<post>,
    val inflater : LayoutInflater,
    val glide : RequestManager
): RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val postOwner : TextView
        val postImage : ImageView
        val postCountent : TextView
        init {
            postOwner = itemView.findViewById(R.id.post_owner)
            postImage = itemView.findViewById(R.id.post_img)
            postCountent = itemView.findViewById(R.id.post_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.outstargrame_item_view,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postOwner.setText(postList.get(position).owner)
        holder.postCountent.setText(postList.get(position).content)
        glide.load(postList.get(position).image).into(holder.postImage)
    }
}