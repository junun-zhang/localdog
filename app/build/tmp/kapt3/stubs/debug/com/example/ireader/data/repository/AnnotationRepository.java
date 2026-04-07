package com.example.ireader.data.repository;

/**
 * 注释仓库，负责管理书签、笔记和高亮
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0006\u0018\u0000 %2\u00020\u0001:\u0001%B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u0019J\u000e\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u0019J\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100 0\u001f2\u0006\u0010!\u001a\u00020\u0019J\u001a\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130 0\u001f2\u0006\u0010!\u001a\u00020\u0019J\u001a\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160 0\u001f2\u0006\u0010!\u001a\u00020\u0019J\u000e\u0010$\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/example/ireader/data/repository/AnnotationRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "bookmarkDao", "Lcom/example/ireader/data/database/BookmarkDao;", "database", "Lcom/example/ireader/data/database/IReaderDatabase;", "highlightDao", "Lcom/example/ireader/data/database/HighlightDao;", "noteDao", "Lcom/example/ireader/data/database/NoteDao;", "addBookmark", "", "bookmark", "Lcom/example/ireader/data/model/Bookmark;", "addHighlight", "highlight", "Lcom/example/ireader/data/model/Highlight;", "addNote", "note", "Lcom/example/ireader/data/model/Note;", "deleteBookmark", "bookmarkId", "", "deleteHighlight", "highlightId", "deleteNote", "noteId", "getBookmarksForBook", "Landroidx/lifecycle/LiveData;", "", "bookId", "getHighlightsForBook", "getNotesForBook", "updateNote", "Companion", "app_debug"})
public final class AnnotationRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.database.IReaderDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.database.BookmarkDao bookmarkDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.database.NoteDao noteDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.database.HighlightDao highlightDao = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.ireader.data.repository.AnnotationRepository INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ireader.data.repository.AnnotationRepository.Companion Companion = null;
    
    private AnnotationRepository(android.content.Context context) {
        super();
    }
    
    /**
     * 获取书籍的所有书签
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Bookmark>> getBookmarksForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId) {
        return null;
    }
    
    /**
     * 添加书签
     */
    public final void addBookmark(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Bookmark bookmark) {
    }
    
    /**
     * 删除书签
     */
    public final void deleteBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String bookmarkId) {
    }
    
    /**
     * 获取书籍的所有笔记
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Note>> getNotesForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId) {
        return null;
    }
    
    /**
     * 添加笔记
     */
    public final void addNote(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Note note) {
    }
    
    /**
     * 更新笔记
     */
    public final void updateNote(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Note note) {
    }
    
    /**
     * 删除笔记
     */
    public final void deleteNote(@org.jetbrains.annotations.NotNull()
    java.lang.String noteId) {
    }
    
    /**
     * 获取书籍的所有高亮
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Highlight>> getHighlightsForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId) {
        return null;
    }
    
    /**
     * 添加高亮
     */
    public final void addHighlight(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Highlight highlight) {
    }
    
    /**
     * 删除高亮
     */
    public final void deleteHighlight(@org.jetbrains.annotations.NotNull()
    java.lang.String highlightId) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/ireader/data/repository/AnnotationRepository$Companion;", "", "()V", "INSTANCE", "Lcom/example/ireader/data/repository/AnnotationRepository;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.ireader.data.repository.AnnotationRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}