package com.example.apicalling.retrofit

import com.example.apicalling.model.Cast
import com.example.apicalling.model.Episode
import com.example.apicalling.model.Searchedshow
import com.example.apicalling.model.Show
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetroAPI {
    @GET("shows")
    fun getAllShows() : Call<List<Show>>

    @GET("search/shows")
    fun searchShow(@Query("q") text: String) : Call<List<Searchedshow>>

    @GET("shows/{id}/cast")
    fun getCast(@Path("id") id: Int) : Call<List<Cast>>

    @GET("shows/{id}/episodes")
    fun getEpisodes(@Path("id") id: Int) : Call<List<Episode>>
}