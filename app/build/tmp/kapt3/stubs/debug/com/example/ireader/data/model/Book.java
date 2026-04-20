package com.example.ireader.data.model;

/**
 * 书籍数据模型
 * @property id 书籍唯一标识符
 * @property title 书籍标题
 * @property author 作者
 * @property coverUri 封面图片URI
 * @property filePath 文件路径
 * @property fileSize 文件大小
 * @property pageCount 总页数（用于PDF/TXT）
 * @property lastReadPage 最后阅读页码
 * @property lastReadChapter 最后阅读章节（用于EPUB）
 * @property lastReadMode 阅读模式
 * @property lastScrollPosition 滚动位置
 * @property lastFontSize 字体大小
 * @property lastZoom PDF缩放比例
 * @property lastReadTime 最后阅读时间
 * @property progress 阅读进度 (0-100)
 * @property format 文件格式 (epub, pdf, txt)
 * @property addedTime 添加时间
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b-\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0087\b\u0018\u00002\u00020\u0001B\u00a9\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\t\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u000b\u0012\u0006\u0010\u0015\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0016\u001a\u00020\t\u00a2\u0006\u0002\u0010\u0017J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u000bH\u00c6\u0003J\t\u00100\u001a\u00020\u000bH\u00c6\u0003J\t\u00101\u001a\u00020\u0012H\u00c6\u0003J\t\u00102\u001a\u00020\tH\u00c6\u0003J\t\u00103\u001a\u00020\u000bH\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\tH\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00108\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\tH\u00c6\u0003J\t\u0010;\u001a\u00020\u000bH\u00c6\u0003J\t\u0010<\u001a\u00020\u000bH\u00c6\u0003J\t\u0010=\u001a\u00020\u000bH\u00c6\u0003J\u00b5\u0001\u0010>\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000b2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\t2\b\b\u0002\u0010\u0014\u001a\u00020\u000b2\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\tH\u00c6\u0001J\u0013\u0010?\u001a\u00020@2\b\u0010A\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0006\u0010B\u001a\u00020\u0003J\u0006\u0010C\u001a\u00020\u0003J\u0006\u0010D\u001a\u00020\u0003J\t\u0010E\u001a\u00020\u000bH\u00d6\u0001J\t\u0010F\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0016\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001bR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001bR\u0011\u0010\u0010\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\r\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\"R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001bR\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\"R\u0011\u0010\u0013\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0019R\u0011\u0010\u000f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\"R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\"R\u0011\u0010\u0014\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\"R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001b\u00a8\u0006G"}, d2 = {"Lcom/example/ireader/data/model/Book;", "", "id", "", "title", "author", "coverUri", "filePath", "fileSize", "", "pageCount", "", "lastReadPage", "lastReadChapter", "lastReadMode", "lastScrollPosition", "lastFontSize", "lastZoom", "", "lastReadTime", "progress", "format", "addedTime", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JIIILjava/lang/String;IIFJILjava/lang/String;J)V", "getAddedTime", "()J", "getAuthor", "()Ljava/lang/String;", "getCoverUri", "getFilePath", "getFileSize", "getFormat", "getId", "getLastFontSize", "()I", "getLastReadChapter", "getLastReadMode", "getLastReadPage", "getLastReadTime", "getLastScrollPosition", "getLastZoom", "()F", "getPageCount", "getProgress", "getTitle", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "getFileSizeString", "getFormatDisplayName", "getLastReadTimeString", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "books")
public final class Book {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String author = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String coverUri = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String filePath = null;
    private final long fileSize = 0L;
    private final int pageCount = 0;
    private final int lastReadPage = 0;
    private final int lastReadChapter = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String lastReadMode = null;
    private final int lastScrollPosition = 0;
    private final int lastFontSize = 0;
    private final float lastZoom = 0.0F;
    private final long lastReadTime = 0L;
    private final int progress = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String format = null;
    private final long addedTime = 0L;
    
    public Book(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String author, @org.jetbrains.annotations.Nullable()
    java.lang.String coverUri, @org.jetbrains.annotations.NotNull()
    java.lang.String filePath, long fileSize, int pageCount, int lastReadPage, int lastReadChapter, @org.jetbrains.annotations.NotNull()
    java.lang.String lastReadMode, int lastScrollPosition, int lastFontSize, float lastZoom, long lastReadTime, int progress, @org.jetbrains.annotations.NotNull()
    java.lang.String format, long addedTime) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAuthor() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCoverUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFilePath() {
        return null;
    }
    
    public final long getFileSize() {
        return 0L;
    }
    
    public final int getPageCount() {
        return 0;
    }
    
    public final int getLastReadPage() {
        return 0;
    }
    
    public final int getLastReadChapter() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLastReadMode() {
        return null;
    }
    
    public final int getLastScrollPosition() {
        return 0;
    }
    
    public final int getLastFontSize() {
        return 0;
    }
    
    public final float getLastZoom() {
        return 0.0F;
    }
    
    public final long getLastReadTime() {
        return 0L;
    }
    
    public final int getProgress() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFormat() {
        return null;
    }
    
    public final long getAddedTime() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFormatDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLastReadTimeString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFileSizeString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final float component13() {
        return 0.0F;
    }
    
    public final long component14() {
        return 0L;
    }
    
    public final int component15() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    public final long component17() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.ireader.data.model.Book copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String author, @org.jetbrains.annotations.Nullable()
    java.lang.String coverUri, @org.jetbrains.annotations.NotNull()
    java.lang.String filePath, long fileSize, int pageCount, int lastReadPage, int lastReadChapter, @org.jetbrains.annotations.NotNull()
    java.lang.String lastReadMode, int lastScrollPosition, int lastFontSize, float lastZoom, long lastReadTime, int progress, @org.jetbrains.annotations.NotNull()
    java.lang.String format, long addedTime) {
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