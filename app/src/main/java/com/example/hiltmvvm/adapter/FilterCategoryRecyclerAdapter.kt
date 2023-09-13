package com.example.hiltmvvm.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hiltmvvm.databinding.FilterCategoryListItemBinding
import com.example.hiltmvvm.model.FilterCategoryObject

class FilterCategoryRecyclerAdapter(private val interaction: Interaction) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<FilterCategoryObject> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val itemBinding: FilterCategoryListItemBinding = FilterCategoryListItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(list[position])
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun submitList(list: List<FilterCategoryObject>, rv: RecyclerView) {
        this.list = list
        rv.layoutManager?.scrollToPosition(0)
    }

    fun insertAt(position: Int) {
        //in Activity: objectList.insertAt(position)
        //then call the code below
        this.notifyItemInserted(position)
    }

    fun removeAt(position: Int) {
        //in Activity: objectList.removeAt(position)
        //then call the code below
        this.notifyItemRemoved(position)
    }

    inner class ViewHolder(binding: FilterCategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val binding: FilterCategoryListItemBinding

        init {
            this.binding = binding
        }

        fun bind(item: FilterCategoryObject) {
//            binding.setVariable(BR.item, item)
//            binding.executePendingBindings()

            binding.tvName.setOnClickListener{
                interaction.onItemSelected(adapterPosition, item)
            }

            itemView.setOnClickListener {
                interaction.onItemSelected(adapterPosition, item)
            }

        }
    }

    fun interface Interaction {
        fun onItemSelected(position: Int, item: FilterCategoryObject)
    }
}