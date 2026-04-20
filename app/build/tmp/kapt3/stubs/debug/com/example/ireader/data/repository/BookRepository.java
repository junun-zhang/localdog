package com.example.ireader.data.repository;

/**
 * 书籍仓库，负责管理书籍数据的获取、存储和更新
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u0000 .2\u00020\u0001:\u0002-.B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\bJ\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u0006\u0010\u001f\u001a\u00020\u0018J\u000e\u0010 \u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"J\u0018\u0010#\u001a\u0004\u0018\u00010\b2\u0006\u0010!\u001a\u00020\"H\u0086@\u00a2\u0006\u0002\u0010$J\b\u0010%\u001a\u00020\u0018H\u0002J\u0016\u0010&\u001a\u00020\u00182\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0006\u0010\'\u001a\u00020\u0018J\u000e\u0010(\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\bJ\u001e\u0010)\u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\u00020\u0010X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/example/ireader/data/repository/BookRepository;", "Lkotlinx/coroutines/CoroutineScope;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_books", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/example/ireader/data/model/Book;", "bookDao", "Lcom/example/ireader/data/database/BookDao;", "books", "Landroidx/lifecycle/LiveData;", "getBooks", "()Landroidx/lifecycle/LiveData;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "database", "Lcom/example/ireader/data/database/IReaderDatabase;", "job", "Lkotlinx/coroutines/CompletableJob;", "addBook", "", "book", "addBookFromUri", "Lcom/example/ireader/data/repository/BookRepository$AddBookResult;", "uri", "Landroid/net/Uri;", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clear", "deleteBook", "bookId", "", "getBookById", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadBooksFromDatabase", "saveBooks", "scanLocalFiles", "updateBook", "updateBookProgress", "progress", "", "lastReadPage", "AddBookResult", "Companion", "app_debug"})
public final class BookRepository implements kotlinx.coroutines.CoroutineScope {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob job = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.coroutines.CoroutineContext coroutineContext = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.database.IReaderDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.database.BookDao bookDao = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.ireader.data.model.Book>> _books = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Book>> books = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.ireader.data.repository.BookRepository INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ireader.data.repository.BookRepository.Companion Companion = null;
    
    private BookRepository(android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlin.coroutines.CoroutineContext getCoroutineContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Book>> getBooks() {
        return null;
    }
    
    /**
     * 扫描本地文件并添加到书架
     * 注意：需要通过 SAF 选择文件，调用 addBookFromUri 添加单本书籍
     */
    public final void scanLocalFiles() {
    }
    
    /**
     * 从数据库加载书籍（按书名排序）
     */
    private final void loadBooksFromDatabase() {
    }
    
    /**
     * 保存书籍到数据库
     */
    private final void saveBooks(java.util.List<com.example.ireader.data.model.Book> books) {
    }
    
    /**
     * 添加单本书籍
     */
    public final void addBook(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Book book) {
    }
    
    /**
     * 从 URI 添加书籍（SAF 模式）
     * @return 添加结果：success=true 表示成功，duplicate=true 表示已存在
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addBookFromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.repository.BookRepository.AddBookResult> $completion) {
        return null;
    }
    
    /**
     * 删除书籍
     */
    public final void deleteBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId) {
    }
    
    /**
     * 更新书籍阅读进度
     */
    public final void updateBookProgress(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int progress, int lastReadPage) {
    }
    
    /**
     * 根据 ID 获取书籍
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBookById(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Book> $completion) {
        return null;
    }
    
    /**
     * 更新书籍
     */
    public final void updateBook(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Book book) {
    }
    
    /**
     * 清理资源
     */
    public final void clear() {
    }
    
    /**
     * 添加书籍结果
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J)\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001J\u0013\u0010\u0011\u001a\u00020\u00032\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b\u00a8\u0006\u0017"}, d2 = {"Lcom/example/ireader/data/repository/BookRepository$AddBookResult;", "", "success", "", "duplicate", "book", "Lcom/example/ireader/data/model/Book;", "(ZZLcom/example/ireader/data/model/Book;)V", "getBook", "()Lcom/example/ireader/data/model/Book;", "getDuplicate", "()Z", "getSuccess", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class AddBookResult {
        private final boolean success = false;
        private final boolean duplicate = false;
        @org.jetbrains.annotations.Nullable()
        private final com.example.ireader.data.model.Book book = null;
        
        public AddBookResult(boolean success, boolean duplicate, @org.jetbrains.annotations.Nullable()
        com.example.ireader.data.model.Book book) {
            super();
        }
        
        public final boolean getSuccess() {
            return false;
        }
        
        public final boolean getDuplicate() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.ireader.data.model.Book getBook() {
            return null;
        }
        
        public final boolean component1() {
            return false;
        }
        
        public final boolean component2() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.ireader.data.model.Book component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.ireader.data.repository.BookRepository.AddBookResult copy(boolean success, boolean duplicate, @org.jetbrains.annotations.Nullable()
        com.example.ireader.data.model.Book book) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/ireader/data/repository/BookRepository$Companion;", "", "()V", "INSTANCE", "Lcom/example/ireader/data/repository/BookRepository;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.ireader.data.repository.BookRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}