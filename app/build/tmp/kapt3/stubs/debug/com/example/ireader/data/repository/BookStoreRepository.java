package com.example.ireader.data.repository;

/**
 * 图书商城仓库，负责与服务端交互获取书籍信息
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00072\u0006\u0010\u0010\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u00072\u0006\u0010\u0010\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0011J&\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u000f2\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0017R \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\n\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0018"}, d2 = {"Lcom/example/ireader/data/repository/BookStoreRepository;", "", "bookStoreApi", "Lcom/example/ireader/data/network/BookStoreApi;", "(Lcom/example/ireader/data/network/BookStoreApi;)V", "_books", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/ireader/data/model/Resource;", "", "Lcom/example/ireader/data/model/BookStoreItem;", "books", "Landroidx/lifecycle/LiveData;", "getBooks", "()Landroidx/lifecycle/LiveData;", "downloadBook", "", "bookId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchBookDetails", "fetchBooks", "", "category", "query", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class BookStoreRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.network.BookStoreApi bookStoreApi = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.ireader.data.model.Resource<java.util.List<com.example.ireader.data.model.BookStoreItem>>> _books = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.ireader.data.model.Resource<java.util.List<com.example.ireader.data.model.BookStoreItem>>> books = null;
    
    @javax.inject.Inject()
    public BookStoreRepository(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.network.BookStoreApi bookStoreApi) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.ireader.data.model.Resource<java.util.List<com.example.ireader.data.model.BookStoreItem>>> getBooks() {
        return null;
    }
    
    /**
     * 从服务端获取书籍列表
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchBooks(@org.jetbrains.annotations.Nullable()
    java.lang.String category, @org.jetbrains.annotations.Nullable()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 获取书籍详情
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchBookDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Resource<com.example.ireader.data.model.BookStoreItem>> $completion) {
        return null;
    }
    
    /**
     * 下载书籍文件
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object downloadBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Resource<java.lang.String>> $completion) {
        return null;
    }
}