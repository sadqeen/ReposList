package com.example.repos.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("Repositories")
data class ResponseMain(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("node_id") var nodeId: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("full_name") var fullName: String? = "",
    @Embedded(prefix = "owner_")
    @SerializedName("owner") var owner: Owner? = Owner(),
    @SerializedName("stargazers_count") var stargazersCount: Int? = 0,
    @SerializedName("description") var description: String? = "",
    var page: Int = 0,
    var isBookMark: Boolean = false,
    @SerializedName("forks") var forks:Int?=0,
    @SerializedName("language") var language: String? = "",


)