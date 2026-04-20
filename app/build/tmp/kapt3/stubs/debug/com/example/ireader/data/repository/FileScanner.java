package com.example.ireader.data.repository;

/**
 * 文件扫描器 - 支持 Storage Access Framework (SAF)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002J\u0010\u0010\r\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002J\u000e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/example/ireader/data/repository/FileScanner;", "", "()V", "createBookFromUri", "Lcom/example/ireader/data/model/Book;", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "(Landroid/content/Context;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "extractAuthorFromFileName", "", "fileName", "getFileFormat", "getFileSizeString", "fileSize", "", "app_debug"})
public final class FileScanner {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ireader.data.repository.FileScanner INSTANCE = null;
    
    private FileScanner() {
        super();
    }
    
    /**
     * 从 SAF URI 创建 Book 对象
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createBookFromUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Book> $completion) {
        return null;
    }
    
    /**
     * 从文件名提取格式
     */
    private final java.lang.String getFileFormat(java.lang.String fileName) {
        return null;
    }
    
    /**
     * 从文件名提取作者信息
     * 支持格式: "书名-作者.epub" 或 "书名_作者.epub"
     */
    private final java.lang.String extractAuthorFromFileName(java.lang.String fileName) {
        return null;
    }
    
    /**
     * 获取文件大小字符串
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFileSizeString(long fileSize) {
        return null;
    }
}