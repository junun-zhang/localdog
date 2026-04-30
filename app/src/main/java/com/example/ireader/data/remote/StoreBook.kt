package com.example.ireader.data.remote

import com.google.gson.annotations.SerializedName

data class StoreBook(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val author: String,
    val description: String = "",
    val coverUrl: String = "",
    val file: String = "",
    val format: String = "txt",
    val category: String = "",
    val fileSize: Long = 0,
    val downloadCount: Int = 0,
    val rating: Double = 0.0,
    val isFree: Boolean = true,
    val createdAt: String = ""
)
