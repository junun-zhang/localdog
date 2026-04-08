package com.example.ireader.data.repository;

/**
 * 注释仓库，负责管理书签、笔记和高亮
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0005\u0018\u0000 )2\u00020\u0001:\u0001)B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u001e\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010#J\u001c\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00100%2\u0006\u0010\"\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00140%2\u0006\u0010\"\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00180%2\u0006\u0010\"\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010(\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/example/ireader/data/repository/AnnotationRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "bookmarkDao", "Lcom/example/ireader/data/database/BookmarkDao;", "database", "Lcom/example/ireader/data/database/IReaderDatabase;", "highlightDao", "Lcom/example/ireader/data/database/HighlightDao;", "noteDao", "Lcom/example/ireader/data/database/NoteDao;", "addBookmark", "", "bookmark", "Lcom/example/ireader/data/model/Bookmark;", "(Lcom/example/ireader/data/model/Bookmark;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addHighlight", "highlight", "Lcom/example/ireader/data/model/Highlight;", "(Lcom/example/ireader/data/model/Highlight;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addNote", "note", "Lcom/example/ireader/data/model/Note;", "(Lcom/example/ireader/data/model/Note;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBookmark", "bookmarkId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteHighlight", "highlightId", "deleteNote", "noteId", "bookId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookmarksForBook", "", "getHighlightsForBook", "getNotesForBook", "updateNote", "Companion", "app_debug"})
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBookmarksForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ireader.data.model.Bookmark>> $completion) {
        return null;
    }
    
    /**
     * 添加书签
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addBookmark(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Bookmark bookmark, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 删除书签
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String bookmarkId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 获取书籍的所有笔记
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getNotesForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ireader.data.model.Note>> $completion) {
        return null;
    }
    
    /**
     * 添加笔记
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addNote(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Note note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 更新笔记
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateNote(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Note note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 删除笔记 - 注意：当前 DAO 不支持按 ID 删除单个笔记
     * 需要先获取笔记对象，然后调用 deleteNote(note)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteNote(@org.jetbrains.annotations.NotNull()
    java.lang.String noteId, @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 获取书籍的所有高亮
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getHighlightsForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ireader.data.model.Highlight>> $completion) {
        return null;
    }
    
    /**
     * 添加高亮
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addHighlight(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.model.Highlight highlight, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 删除高亮
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteHighlight(@org.jetbrains.annotations.NotNull()
    java.lang.String highlightId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
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