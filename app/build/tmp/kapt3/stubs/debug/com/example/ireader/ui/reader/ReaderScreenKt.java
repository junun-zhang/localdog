package com.example.ireader.ui.reader;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001aj\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00030\u0012H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0013\u0010\u0014\u001a4\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000e2\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00030\u00122\u0006\u0010\u0019\u001a\u00020\u0001H\u0003\u001aD\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00030\u0012H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001f\u001a\u0018\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020#H\u0007\u001a\u0088\u0001\u0010$\u001a\u00020\u00032\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\b2\u0006\u0010(\u001a\u00020\u000e2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020&\u0012\u0004\u0012\u00020\u00030\u00122\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00030\u00122\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\u0012\u00101\u001a\u000e\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020\u00030\u0012H\u0003\u001a4\u00102\u001a\u00020\u00032\u0006\u00103\u001a\u00020&2\u0006\u0010%\u001a\u00020&2\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020&\u0012\u0004\u0012\u00020\u00030\u00122\u0006\u0010\u0019\u001a\u00020\u0001H\u0003\u001aH\u00104\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010!\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0010H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b5\u00106\u001ab\u00107\u001a\u00020\u00032\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u00109\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00030\u0012H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b:\u0010;\u001a\"\u0010<\u001a\u0004\u0018\u00010=2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u0001H\u0002\u001a\u0018\u0010C\u001a\u00020\u00012\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AH\u0002\u001a\u001e\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AH\u0002\u001a \u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\b2\u0006\u0010>\u001a\u00020?H\u0002\u001a\u001e\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010K\u001a\u00020\u00012\u0006\u0010L\u001a\u00020\bH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006M"}, d2 = {"TAG", "", "EpubViewer", "", "content", "", "Lcom/example/ireader/ui/reader/SpineItem;", "index", "", "bgColor", "Landroidx/compose/ui/graphics/Color;", "textColor", "fontSize", "readingMode", "Lcom/example/ireader/ui/reader/ReadingMode;", "onTapCenter", "Lkotlin/Function0;", "onIndexChange", "Lkotlin/Function1;", "EpubViewer-t6yy7ic", "(Ljava/util/List;IJJILcom/example/ireader/ui/reader/ReadingMode;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "ModeButton", "mode", "currentMode", "onClick", "label", "PdfViewer", "state", "Lcom/example/ireader/ui/reader/PdfRenderState;", "onPageChange", "PdfViewer-RPmYEkk", "(Lcom/example/ireader/ui/reader/PdfRenderState;JLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "ReaderScreen", "bookId", "navController", "Landroidx/navigation/NavController;", "SettingsPanelContent", "currentTheme", "Lcom/example/ireader/ui/reader/ReadingTheme;", "currentFontSize", "currentReadingMode", "currentZoom", "", "isPdf", "", "onThemeChanged", "onReadingModeChanged", "onFontSizeIncrease", "onFontSizeDecrease", "onZoomChanged", "ThemeButton", "theme", "TxtScrollViewer", "TxtScrollViewer-RIQooxk", "(Ljava/lang/String;JJILjava/lang/String;Lkotlin/jvm/functions/Function0;)V", "TxtViewer", "pages", "currentPage", "TxtViewer-eaDK9VM", "(Ljava/util/List;IJJILkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "copyToCache", "Ljava/io/File;", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "fileName", "loadText", "parseEpub", "renderPageSync", "Landroid/graphics/Bitmap;", "renderer", "Landroid/graphics/pdf/PdfRenderer;", "pageIndex", "splitTextToPages", "text", "charsPerPage", "app_debug"})
public final class ReaderScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ReaderScreen";
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ReaderScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController) {
    }
    
    private static final java.util.List<java.lang.String> splitTextToPages(java.lang.String text, int charsPerPage) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SettingsPanelContent(com.example.ireader.ui.reader.ReadingTheme currentTheme, int currentFontSize, com.example.ireader.ui.reader.ReadingMode currentReadingMode, float currentZoom, boolean isPdf, kotlin.jvm.functions.Function1<? super com.example.ireader.ui.reader.ReadingTheme, kotlin.Unit> onThemeChanged, kotlin.jvm.functions.Function1<? super com.example.ireader.ui.reader.ReadingMode, kotlin.Unit> onReadingModeChanged, kotlin.jvm.functions.Function0<kotlin.Unit> onFontSizeIncrease, kotlin.jvm.functions.Function0<kotlin.Unit> onFontSizeDecrease, kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onZoomChanged) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModeButton(com.example.ireader.ui.reader.ReadingMode mode, com.example.ireader.ui.reader.ReadingMode currentMode, kotlin.jvm.functions.Function1<? super com.example.ireader.ui.reader.ReadingMode, kotlin.Unit> onClick, java.lang.String label) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ThemeButton(com.example.ireader.ui.reader.ReadingTheme theme, com.example.ireader.ui.reader.ReadingTheme currentTheme, kotlin.jvm.functions.Function1<? super com.example.ireader.ui.reader.ReadingTheme, kotlin.Unit> onClick, java.lang.String label) {
    }
    
    private static final android.graphics.Bitmap renderPageSync(android.graphics.pdf.PdfRenderer renderer, int pageIndex, android.content.Context context) {
        return null;
    }
    
    private static final java.io.File copyToCache(android.content.Context context, android.net.Uri uri, java.lang.String fileName) {
        return null;
    }
    
    private static final java.util.List<com.example.ireader.ui.reader.SpineItem> parseEpub(android.content.Context context, android.net.Uri uri) {
        return null;
    }
    
    private static final java.lang.String loadText(android.content.Context context, android.net.Uri uri) {
        return null;
    }
}