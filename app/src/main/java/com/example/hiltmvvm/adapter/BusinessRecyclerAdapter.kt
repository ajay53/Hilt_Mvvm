package com.example.hiltmvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hiltmvvm.BR
import com.example.hiltmvvm.databinding.ListItemRestaurantBinding
import com.example.hiltmvvm.model.Business

class BusinessRecyclerAdapter(private val interaction: Interaction) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Business>() {

        override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val itemBinding: ListItemRestaurantBinding = ListItemRestaurantBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Business>, rv: RecyclerView) {
        //everytime list is refreshed, new list object is created in MainActivity before calling submitList in this class
        //if the above is not done, list is not refreshed
        differ.submitList(list) {
            //scroll to top once listDiffer is done
            rv.layoutManager?.scrollToPosition(0)
        }
    }

    fun removeAt(position: Int) {
        //in Activity: objectList.removeAt(position)
        //then call the code below
        this.notifyItemRemoved(position)
    }

    fun insertAdd(position: Int) {
        //in Activity: objectList.insertAt(position)
        //then call the code below
        this.notifyItemInserted(position)
    }

    inner class ViewHolder(binding: ListItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val binding: ListItemRestaurantBinding

        init {
            this.binding = binding
        }

        fun bind(item: Business) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()

            itemView.setOnClickListener {
                interaction.onItemSelected(adapterPosition, item)
            }
        }
    }

    fun interface Interaction {
        fun onItemSelected(position: Int, item: Business)
    }
}