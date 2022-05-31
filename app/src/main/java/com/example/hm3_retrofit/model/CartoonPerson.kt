package com.example.hm3_retrofit.model

import com.google.gson.annotations.SerializedName

data class CartoonPerson (
    //  https://youtu.be/IDVxFjLeecA?t=10566
    @SerializedName("id")
    val idApi: Int,
    @SerializedName("name")
    val nameApi: String,
    @SerializedName("image")// используется, чтоб переписать название из json в наше название.
    val imageApi: String,
)