package com.example.hm3_retrofit.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hm3_retrofit.databinding.ItemPersonBinding

import com.example.hm3_retrofit.model.CartoonPerson

class PersonAdapter(
   // context: Context,
    //private val onUserClicked: (CartoonPerson) -> Unit
) : ListAdapter<CartoonPerson, PersonViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return PersonViewHolder(
            binding = ItemPersonBinding.inflate(layoutInflater, parent, false)
            //onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartoonPerson>() {
            override fun areItemsTheSame(oldItem: CartoonPerson, newItem: CartoonPerson): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CartoonPerson,
                newItem: CartoonPerson,
            ): Boolean {

                return (oldItem.idApi == newItem.idApi
                        && oldItem.imageApi == newItem.imageApi
                        && oldItem.nameApi == newItem.nameApi)
            }

        }

    }


}