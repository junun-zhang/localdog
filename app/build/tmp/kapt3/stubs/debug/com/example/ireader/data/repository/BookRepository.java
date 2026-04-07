package com.example.ireader.data.repository;

/**
 * 书籍仓库，负责管理书籍数据的获取、存储和更新
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bJ\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\u0017\u001a\u00020\u0012H\u0002J\u0016\u0010\u0018\u001a\u00020\u00122\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0006\u0010\u0019\u001a\u00020\u0012J\u001e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/ireader/data/repository/BookRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_books", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/example/ireader/data/model/Book;", "bookDao", "Lcom/example/ireader/data/database/BookDao;", "books", "Landroidx/lifecycle/LiveData;", "getBooks", "()Landroidx/lifecycle/LiveData;", "database", "Lcom/example/ireader/data/database/IReaderDatabase;", "addBook", "", "book", "deleteBook", "bookId", "", "loadBooksFromDatabase", "saveBooks", "scanLocalFiles", "updateBookProgress", "progress", "", "lastReadPage", "Companion", "app_debug"})
public final class BookRepository {
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
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Book>> getBooks() {
        return null;
    }
    
    /**
     * 扫描本地文件并添加到书架
     */
    public final void scanLocalFiles() {
    }
    
    /**
     * 从数据库加载书籍
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