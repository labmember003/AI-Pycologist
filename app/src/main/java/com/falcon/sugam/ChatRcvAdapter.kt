package com.falcon.sugam

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

class ChatRcvAdapter(private val arrayListData: ArrayList<String>) : RecyclerView.Adapter<ChatRcvAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessageUser: TextView = itemView.findViewById(R.id.usertext)
        val ll: LinearLayout = itemView.findViewById(R.id.ll)
        val animation = itemView.findViewById<LottieAnimationView>(R.id.animation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textMessageUser.text = arrayListData[position]
        if (position % 2 == 0) {
            holder.animation.setAnimation("girl.json")
            Log.i("boyyyyyy","girl")
        } else {
            holder.animation.setAnimation("boy.json")
            Log.i("boyyyyyy", "boy")
        }
        holder.animation.playAnimation()

    }


}
