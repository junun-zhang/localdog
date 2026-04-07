package com.example.ireader.data.network;

/**
 * 图书商城 API 接口
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J:\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\t0\u00032\n\b\u0003\u0010\n\u001a\u0004\u0018\u00010\u00062\b\b\u0003\u0010\u000b\u001a\u00020\f2\b\b\u0003\u0010\r\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\t0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0010J8\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\t0\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00062\b\b\u0003\u0010\u000b\u001a\u00020\f2\b\b\u0003\u0010\r\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/example/ireader/data/network/BookStoreApi;", "", "getBookDetail", "Lretrofit2/Response;", "Lcom/example/ireader/data/model/BookStoreItem;", "bookId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBooks", "", "category", "page", "", "pageSize", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCategories", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchBooks", "query", "app_debug"})
public abstract interface BookStoreApi {
    
    /**
     * 获取书籍列表
     * @param category 分类（可选）
     * @param page 页码
     * @param pageSize 每页数量
     */
    @retrofit2.http.GET(value = "api/books")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBooks(@retrofit2.http.Query(value = "category")
    @org.jetbrains.annotations.Nullable()
    java.lang.String category, @retrofit2.http.Query(value = "page")
    int page, @retrofit2.http.Query(value = "page_size")
    int pageSize, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.ireader.data.model.BookStoreItem>>> $completion);
    
    /**
     * 搜索书籍
     * @param query 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     */
    @retrofit2.http.GET(value = "api/books/search")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchBooks(@retrofit2.http.Query(value = "q")
    @org.jetbrains.annotations.NotNull()
    java.lang.String query, @retrofit2.http.Query(value = "page")
    int page, @retrofit2.http.Query(value = "page_size")
    int pageSize, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.ireader.data.model.BookStoreItem>>> $completion);
    
    /**
     * 获取书籍详情
     * @param bookId 书籍ID
     */
    @retrofit2.http.GET(value = "api/books/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBookDetail(@retrofit2.http.Query(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ireader.data.model.BookStoreItem>> $completion);
    
    /**
     * 获取书籍分类
     */
    @retrofit2.http.GET(value = "api/categories")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCategories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<java.lang.String>>> $completion);
    
    /**
     * 图书商城 API 接口
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}