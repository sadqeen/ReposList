package com.example.repos.repository.network


import com.example.repos.data.ResponseMain
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

@JvmSuppressWildcards
interface RetrofitApi {
    companion object {
        const val BASE_URL = "https://api.github.com/orgs/square/"
        const val TOKEN="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"

    }

    @GET("repos")
    suspend fun getAllRepos(
        @Header("Authroziation") token:String,
        @Query("page") page: Int
    ): List<ResponseMain>


}