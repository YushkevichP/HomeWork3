package com.example.hm3_retrofit.retrofit


import com.example.hm3_retrofit.model.PersonsListApi
import com.example.hm3_retrofit.model.PersonDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://youtu.be/IDVxFjLeecA?t=10822

interface RickMortyApi {

    @GET("character")
    fun getUsers(
        @Query("page") page: Int,

        ): Call<PersonsListApi>

    @GET("character/{id}")
    fun getUserDetails(
        @Path("id") id: Int // Path -подставление значения в какой-то запрос.
    //
    ): Call<PersonDetails>


}