package com.example.hm3_retrofit.adapter

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.hm3_retrofit.databinding.ItemPersonBinding
import com.example.hm3_retrofit.model.CartoonPerson


// сюда нужно передать вторым параметром еще инфу по фото или АПИ  https://youtu.be/IDVxFjLeecA?t=10544
class PersonViewHolder(
    private val binding: ItemPersonBinding,
    private val onUserClicked: (CartoonPerson) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(person: CartoonPerson) {

        with(binding) {

            imageView.load(person.imageApi){
                scale(Scale.FILL)
                size(ViewSizeResolver(root))
            }

            textNameView.text = person.nameApi

            root.setOnClickListener{

                onUserClicked(person)
            }
        }

    }


}