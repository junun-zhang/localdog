package com.example.ireader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * 图书商城书籍项数据模型
 */
@Entity(tableName = "bookstore_items")
data class BookStoreItem(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("author")
    val author: String,
    
    @SerializedName("cover_url")
    val coverUrl: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("file_url")
    val fileUrl: String,
    
    @SerializedName("file_size")
    val fileSize: Long,
    
    @SerializedName("format")
    val format: String,
    
    @SerializedName("price")
    val price: Double, // 0.0 表示免费
    
    @SerializedName("rating")
    val rating: Float,
    
    @SerializedName("download_count")
    val downloadCount: Int,
    
    @SerializedName("created_at")
    val createdAt: Long,
    
    @SerializedName("updated_at")
    val updatedAt: Long,
    
    // 本地状态
    var isDownloaded: Boolean = false,
    var localFilePath: String? = null,
    var downloadProgress: Int = 0
) {
    fun isFree(): Boolean = price <= 0.0
    
    fun getPriceDisplay(): String {
        return if (isFree()) {
            "免费"
        } else {
            "¥${String.format("%.2f", price)}"
        }
    }
    
    fun getCategoryDisplayName(): String {
        return when (category.lowercase()) {
            "fiction" -> "小说"
            "nonfiction" -> "非虚构"
            "science" -> "科学"
            "history" -> "历史"
            "biography" -> "传记"
            "poetry" -> "诗歌"
            "drama" -> "戏剧"
            "children" -> "儿童"
            "mystery" -> "悬疑"
            "romance" -> "言情"
            "fantasy" -> "奇幻"
            "scifi" -> "科幻"
            else -> category
        }
    }
}