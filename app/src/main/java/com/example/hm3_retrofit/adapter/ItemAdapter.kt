package com.example.hm3_retrofit.adapter

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hm3_retrofit.databinding.ItemLoadingBinding
import com.example.hm3_retrofit.databinding.ItemPersonBinding
import com.example.hm3_retrofit.model.ItemType


class ItemAdapter(
    context: Context,
    private val onUserClicked: (ItemType.CartoonPerson) -> Unit,
) : ListAdapter<ItemType, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ItemType.CartoonPerson -> TYPE_PERSON
            ItemType.Loading -> TYPE_LOADING
        }
    }
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_PERSON -> {
                PersonViewHolder(
                    binding = ItemPersonBinding.inflate(layoutInflater, parent, false),
                    onUserClicked = onUserClicked
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                )
            }

            else -> error("Incorrect ViewType = $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val personVH = holder as? PersonViewHolder ?: return
        val item = getItem(position) as? ItemType.CartoonPerson ?: return
        personVH.bind(item)
    }

    companion object {

        private const val TYPE_PERSON = 0
        private const val TYPE_LOADING = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemType>() {
            override fun areItemsTheSame(oldItem: ItemType, newItem: ItemType): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemType,
                newItem: ItemType,
            ): Boolean {
                val oldPersonItem = oldItem as? ItemType.CartoonPerson ?: return false
                val newPersonItem = newItem as? ItemType.CartoonPerson ?: return false

                return (oldPersonItem.idApi == newPersonItem.idApi
                        && oldPersonItem.imageApi == newPersonItem.imageApi
                        && oldPersonItem.nameApi == newPersonItem.nameApi)
            }
        }
    }
}