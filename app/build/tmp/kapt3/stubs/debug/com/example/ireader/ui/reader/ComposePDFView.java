package com.example.ireader.ui.reader;

/**
 * 自定义 PDFView 用于更好的 Compose 集成
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u001aR\"\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR(\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006\u001b"}, d2 = {"Lcom/example/ireader/ui/reader/ComposePDFView;", "Lcom/github/barteksc/pdfviewer/PDFView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "onPdfLoadComplete", "Lkotlin/Function0;", "", "getOnPdfLoadComplete", "()Lkotlin/jvm/functions/Function0;", "setOnPdfLoadComplete", "(Lkotlin/jvm/functions/Function0;)V", "onPdfPageChanged", "Lkotlin/Function1;", "getOnPdfPageChanged", "()Lkotlin/jvm/functions/Function1;", "setOnPdfPageChanged", "(Lkotlin/jvm/functions/Function1;)V", "setOnLoadCompleteListener", "listener", "Lcom/github/barteksc/pdfviewer/listener/OnLoadCompleteListener;", "setOnPageChangeListener", "Lcom/github/barteksc/pdfviewer/listener/OnPageChangeListener;", "app_debug"})
public final class ComposePDFView extends com.github.barteksc.pdfviewer.PDFView {
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function0<kotlin.Unit> onPdfLoadComplete;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onPdfPageChanged;
    
    @kotlin.jvm.JvmOverloads()
    public ComposePDFView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null, null);
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnPdfLoadComplete() {
        return null;
    }
    
    public final void setOnPdfLoadComplete(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<java.lang.Integer, kotlin.Unit> getOnPdfPageChanged() {
        return null;
    }
    
    public final void setOnPdfPageChanged(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> p0) {
    }
    
    public final void setOnLoadCompleteListener(@org.jetbrains.annotations.NotNull()
    com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener listener) {
    }
    
    public final void setOnPageChangeListener(@org.jetbrains.annotations.NotNull()
    com.github.barteksc.pdfviewer.listener.OnPageChangeListener listener) {
    }
    
    @kotlin.jvm.JvmOverloads()
    public ComposePDFView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null, null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public ComposePDFView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null, null);
    }
}