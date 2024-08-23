package com.example.repos.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.repos.R
import com.example.repos.data.ResponseMain
import com.example.repos.databinding.ItemHomeLayoutBinding


class RepositoriesAdapter(private val onItemClicked: (ResponseMain,Int) -> Unit):  PagingDataAdapter<ResponseMain, RepositoriesAdapter.ItemHomeHolder>(DiffUtils){
    object DiffUtils : DiffUtil.ItemCallback<ResponseMain>(){
        override fun areItemsTheSame(oldItem: ResponseMain, newItem: ResponseMain): Boolean {
            return oldItem.id == newItem.id || oldItem.isBookMark == newItem.isBookMark
        }

        override fun areContentsTheSame(oldItem: ResponseMain, newItem: ResponseMain): Boolean {
            return oldItem == newItem
        }

    }

    class ItemHomeHolder(var itemHomeLayoutBinding: ItemHomeLayoutBinding):RecyclerView.ViewHolder(itemHomeLayoutBinding.root)

    override fun onBindViewHolder(holder: ItemHomeHolder, position: Int) {
        setFadeAnimation(holder.itemHomeLayoutBinding.root)
        holder.itemHomeLayoutBinding.data=getItem(position)
        holder.itemHomeLayoutBinding.root.setOnClickListener {
            try {
                getItem(holder.bindingAdapterPosition)?.let { it1 -> onItemClicked(it1,holder.bindingAdapterPosition) }
            }catch (e:Exception)
            {
                e.printStackTrace()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHomeHolder {
       var itemHomeLayoutBinding=DataBindingUtil.inflate<ItemHomeLayoutBinding>(LayoutInflater.from(parent.context),
           R.layout.item_home_layout,parent,false)
        return ItemHomeHolder(itemHomeLayoutBinding)
    }
    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        view.startAnimation(anim)
    }


}