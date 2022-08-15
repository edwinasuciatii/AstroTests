package com.astro.test.edwinasuciati

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class Adapter(private var data: ArrayList<ModelUser>, private val activity: FragmentActivity):
    RecyclerView.Adapter<Adapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val getData: ModelUser = data[p1]
        p0.username.text = getData.name
        val url = getData.avatar
        Glide.with(activity)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(60)))
            .into(p0.avatar)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view : View = LayoutInflater.from(p0.context).inflate(R.layout.card_user, p0, false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var avatar: ImageView = itemView.findViewById(R.id.avatar)
        var username: TextView = itemView.findViewById(R.id.username)
    }
}