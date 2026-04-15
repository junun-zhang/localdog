package com.example.ireader.data.database;

/**
 * Room DAO for Book entity operations
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\rH\'J\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\r2\u0006\u0010\u0013\u001a\u00020\u0005H\'J\u0016\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u001c\u0010\u0017\u001a\u00020\b2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0016J.\u0010\u001b\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020 H\u00a7@\u00a2\u0006\u0002\u0010!\u00a8\u0006\""}, d2 = {"Lcom/example/ireader/data/database/BookDao;", "", "bookExists", "", "filePath", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllBooks", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBook", "id", "getAllBooks", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/ireader/data/model/Book;", "getAllBooksOnce", "getBookById", "getBooksByFormat", "format", "insertBook", "book", "(Lcom/example/ireader/data/model/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertBooks", "books", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateBook", "updateBookProgress", "progress", "", "lastReadPage", "lastReadTime", "", "(Ljava/lang/String;IIJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface BookDao {
    
    /**
     * Get all books from the database
     * @return Flow of list of books
     */
    @androidx.room.Query(value = "SELECT * FROM books ORDER BY lastReadTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ireader.data.model.Book>> getAllBooks();
    
    /**
     * Get all books from the database (one-shot query)
     * @return List of books
     */
    @androidx.room.Query(value = "SELECT * FROM books ORDER BY lastReadTime DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllBooksOnce(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ireader.data.model.Book>> $completion);
    
    /**
     * Get a book by its ID
     * @param id Book ID
     * @return Book or null if not found
     */
    @androidx.room.Query(value = "SELECT * FROM books WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBookById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Book> $completion);
    
    /**
     * Insert a new book or update existing one
     * @param book Book to insert/update
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBook(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Book book, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Insert multiple books
     * @param books List of books to insert
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBooks(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.ireader.data.model.Book> books, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update a book
     * @param book Book to update
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBook(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Book book, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update book reading progress
     * @param id Book ID
     * @param progress Reading progress (0-100)
     * @param lastReadPage Last read page number
     * @param lastReadTime Last read timestamp
     */
    @androidx.room.Query(value = "UPDATE books SET progress = :progress, lastReadPage = :lastReadPage, lastReadTime = :lastReadTime WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBookProgress(@org.jetbrains.annotations.NotNull()
    java.lang.String id, int progress, int lastReadPage, long lastReadTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete a book by ID
     * @param id Book ID to delete
     */
    @androidx.room.Query(value = "DELETE FROM books WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBook(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete all books
     */
    @androidx.room.Query(value = "DELETE FROM books")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllBooks(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Check if a book exists by file path
     * @param filePath File path to check
     * @return true if exists, false otherwise
     */
    @androidx.room.Query(value = "SELECT EXISTS(SELECT 1 FROM books WHERE filePath = :filePath)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object bookExists(@org.jetbrains.annotations.NotNull()
    java.lang.String filePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Get books by format
     * @param format File format (epub, pdf, txt)
     * @return List of books with specified format
     */
    @androidx.room.Query(value = "SELECT * FROM books WHERE format = :format ORDER BY lastReadTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ireader.data.model.Book>> getBooksByFormat(@org.jetbrains.annotations.NotNull()
    java.lang.String format);
}