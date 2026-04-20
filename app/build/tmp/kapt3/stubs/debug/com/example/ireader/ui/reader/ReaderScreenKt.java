package com.example.ireader.ui.reader;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000v\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001ab\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00030\u0010H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012\u001aD\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\n2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e2\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00030\u0010H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a\u0018\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001cH\u0007\u001aP\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\"2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00030\u00102\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0003\u001a4\u0010&\u001a\u00020\u00032\u0006\u0010\'\u001a\u00020\u001f2\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00030\u00102\u0006\u0010)\u001a\u00020\u0001H\u0003\u001a@\u0010*\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b+\u0010,\u001ab\u0010-\u001a\u00020\u00032\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010/\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e2\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00030\u0010H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u0010\u0012\u001a\"\u00101\u001a\u0004\u0018\u0001022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0001H\u0002\u001a\u0018\u00108\u001a\u00020\u00012\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000206H\u0002\u001a\u001e\u00109\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000206H\u0002\u001a \u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\b2\u0006\u00103\u001a\u000204H\u0002\u001a\u001e\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010@\u001a\u00020\u00012\u0006\u0010A\u001a\u00020\bH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006B"}, d2 = {"TAG", "", "EpubViewer", "", "content", "", "Lcom/example/ireader/ui/reader/SpineItem;", "index", "", "bgColor", "Landroidx/compose/ui/graphics/Color;", "textColor", "fontSize", "onTapCenter", "Lkotlin/Function0;", "onIndexChange", "Lkotlin/Function1;", "EpubViewer-eaDK9VM", "(Ljava/util/List;IJJILkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "PdfViewer", "state", "Lcom/example/ireader/ui/reader/PdfRenderState;", "onPageChange", "PdfViewer-RPmYEkk", "(Lcom/example/ireader/ui/reader/PdfRenderState;JLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "ReaderScreen", "bookId", "navController", "Landroidx/navigation/NavController;", "SettingsPanelContent", "currentTheme", "Lcom/example/ireader/ui/reader/ReadingTheme;", "currentFontSize", "isPdf", "", "onThemeChanged", "onFontSizeIncrease", "onFontSizeDecrease", "ThemeButton", "theme", "onClick", "label", "TxtScrollViewer", "TxtScrollViewer-1wkBAMs", "(Ljava/lang/String;JJILkotlin/jvm/functions/Function0;)V", "TxtViewer", "pages", "currentPage", "TxtViewer-eaDK9VM", "copyToCache", "Ljava/io/File;", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "fileName", "loadText", "parseEpub", "renderPageSync", "Landroid/graphics/Bitmap;", "renderer", "Landroid/graphics/pdf/PdfRenderer;", "pageIndex", "splitTextToPages", "text", "charsPerPage", "app_debug"})
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
    private static final void SettingsPanelContent(com.example.ireader.ui.reader.ReadingTheme currentTheme, int currentFontSize, boolean isPdf, kotlin.jvm.functions.Function1<? super com.example.ireader.ui.reader.ReadingTheme, kotlin.Unit> onThemeChanged, kotlin.jvm.functions.Function0<kotlin.Unit> onFontSizeIncrease, kotlin.jvm.functions.Function0<kotlin.Unit> onFontSizeDecrease) {
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