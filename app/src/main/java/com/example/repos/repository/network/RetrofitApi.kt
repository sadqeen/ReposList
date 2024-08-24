package com.example.repos.repository.network


import com.example.repos.data.ResponseMain
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

@JvmSuppressWildcards
interface RetrofitApi {
    companion object {
        const val BASE_URL = "https://api.github.com/orgs/square/"

    }

    @GET("repos")
    suspend fun getAllRepos(
        @Query("page") page: Int
    ): List<ResponseMain>


}