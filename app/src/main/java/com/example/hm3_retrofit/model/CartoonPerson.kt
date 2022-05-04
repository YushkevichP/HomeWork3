//package com.example.hm3_retrofit.model
//
//import com.google.gson.annotations.SerializedName
//
//sealed class Item{
//    data class CartoonPerson (
//        // сюдп пишем данные которые будем тянуть  с апи
//        //  https://youtu.be/IDVxFjLeecA?t=10566
//        @SerializedName("id")
//        val idApi: Int,
//        @SerializedName("name")
//        val nameApi: String,
//        @SerializedName("image")// используется, чтоб переписать название из json в наше название.
//        val imageApi: String,
//    ) : Item()
//
//    object Loading : Item()
//}
//
//
//
//data class ResponseApi(
//    val results: List<Item.CartoonPerson>
//)