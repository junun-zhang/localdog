package com.example.ireader.ui.reader;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0011J\u0006\u0010\u0017\u001a\u00020\u0011J\u000e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u001dR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/example/ireader/ui/reader/ReaderViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_readingPreferences", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/ireader/ui/reader/ReadingPreferences;", "bookRepository", "Lcom/example/ireader/data/repository/BookRepository;", "readingPreferences", "Landroidx/lifecycle/LiveData;", "getReadingPreferences", "()Landroidx/lifecycle/LiveData;", "settingsManager", "Lcom/example/ireader/ui/reader/ReadingSettingsManager;", "decreaseFontSize", "", "getBook", "Lcom/example/ireader/ui/reader/SimpleBook;", "bookId", "", "increaseFontSize", "loadReadingPreferences", "updateFontSize", "fontSize", "", "updateTheme", "theme", "Lcom/example/ireader/ui/reader/ReadingTheme;", "app_debug"})
public final class ReaderViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.repository.BookRepository bookRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.ireader.ui.reader.ReadingPreferences> _readingPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.ireader.ui.reader.ReadingPreferences> readingPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.ui.reader.ReadingSettingsManager settingsManager = null;
    
    public ReaderViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.ireader.ui.reader.ReadingPreferences> getReadingPreferences() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.example.ireader.ui.reader.SimpleBook getBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId) {
        return null;
    }
    
    public final void loadReadingPreferences() {
    }
    
    public final void updateTheme(@org.jetbrains.annotations.NotNull()
    com.example.ireader.ui.reader.ReadingTheme theme) {
    }
    
    public final void updateFontSize(int fontSize) {
    }
    
    public final void increaseFontSize() {
    }
    
    public final void decreaseFontSize() {
    }
}