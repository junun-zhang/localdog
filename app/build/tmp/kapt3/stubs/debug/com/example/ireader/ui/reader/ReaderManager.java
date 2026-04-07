package com.example.ireader.ui.reader;

/**
 * 阅读器管理器 - 根据文件格式提供不同的阅读器实现
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J<\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00040\b2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00040\bH\u0007J\u001a\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007J\u0016\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0006\u00a8\u0006\u0013"}, d2 = {"Lcom/example/ireader/ui/reader/ReaderManager;", "", "()V", "PdfReaderView", "", "pdfPath", "", "onPageChanged", "Lkotlin/Function1;", "", "onLoadComplete", "TxtReaderView", "txtContent", "modifier", "Landroidx/compose/ui/Modifier;", "openEpubReader", "context", "Landroid/content/Context;", "epubPath", "app_debug"})
public final class ReaderManager {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ireader.ui.reader.ReaderManager INSTANCE = null;
    
    private ReaderManager() {
        super();
    }
    
    public final void openEpubReader(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String epubPath) {
    }
    
    @androidx.compose.runtime.Composable()
    public final void PdfReaderView(@org.jetbrains.annotations.NotNull()
    java.lang.String pdfPath, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onPageChanged, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onLoadComplete) {
    }
    
    @androidx.compose.runtime.Composable()
    public final void TxtReaderView(@org.jetbrains.annotations.NotNull()
    java.lang.String txtContent, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}