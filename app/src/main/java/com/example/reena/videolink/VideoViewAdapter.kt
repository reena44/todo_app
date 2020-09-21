package com.example.reena.videolink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reena.R
import com.example.reena.VideoViewModel
import com.example.reena.homescreen.MainActivity
import kotlinx.android.synthetic.main.videos_item.view.*

class VideoViewAdapter(var listener: OnItemClick , var videosList: ArrayList<VideoViewModel>) :RecyclerView.Adapter<VideoViewAdapter.ViewHolder>(){
    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.videos_item,parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_video_title.text = videosList[position].title
        holder.itemView.img_video.setImageDrawable(videosList[position].image)
        holder.itemView.cv_image.setOnClickListener {
            listener.onClickListener(videosList[position].videoLink)

        }
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
    interface OnItemClick{
        fun onClickListener(link:String)
    }
}