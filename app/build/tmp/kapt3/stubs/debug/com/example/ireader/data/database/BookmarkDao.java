package com.example.ireader.data.database;

/**
 * 书签数据访问对象
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\n\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000eH\'J\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\f\u001a\u00020\tH\'J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u000f2\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0016\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u0018\u00a8\u0006\u0019"}, d2 = {"Lcom/example/ireader/data/database/BookmarkDao;", "", "deleteBookmark", "", "bookmark", "Lcom/example/ireader/data/model/Bookmark;", "(Lcom/example/ireader/data/model/Bookmark;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBookmarkById", "bookmarkId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBookmarksByBook", "bookId", "getAllBookmarks", "Lkotlinx/coroutines/flow/Flow;", "", "getAllBookmarksList", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookmarksByBook", "getBookmarksForBook", "insertBookmark", "updateBookmark", "updateBookmarkNote", "note", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface BookmarkDao {
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ireader.data.model.Bookmark>> getBookmarksByBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId);
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY createdAt DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBookmarksForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ireader.data.model.Bookmark>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ireader.data.model.Bookmark>> getAllBookmarks();
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks ORDER BY createdAt DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllBookmarksList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ireader.data.model.Bookmark>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBookmark(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Bookmark bookmark, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBookmark(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Bookmark bookmark, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBookmark(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Bookmark bookmark, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM bookmarks WHERE id = :bookmarkId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBookmarkById(@org.jetbrains.annotations.NotNull()
    java.lang.String bookmarkId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE bookmarks SET note = :note WHERE id = :bookmarkId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBookmarkNote(@org.jetbrains.annotations.NotNull()
    java.lang.String bookmarkId, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM bookmarks WHERE bookId = :bookId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBookmarksByBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}