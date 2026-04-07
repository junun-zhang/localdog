package com.example.ireader.ui.reader;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nJ \u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/example/ireader/ui/reader/ReaderViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "bookRepository", "Lcom/example/ireader/data/repository/BookRepository;", "(Landroid/app/Application;Lcom/example/ireader/data/repository/BookRepository;)V", "getBookById", "Lcom/example/ireader/data/model/Book;", "bookId", "", "updateBookProgress", "", "progress", "", "lastReadPage", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReaderViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.repository.BookRepository bookRepository = null;
    
    @javax.inject.Inject()
    public ReaderViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application, @org.jetbrains.annotations.NotNull()
    com.example.ireader.data.repository.BookRepository bookRepository) {
        super(null);
    }
    
    public final void updateBookProgress(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int progress, int lastReadPage) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.example.ireader.data.model.Book getBookById(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId) {
        return null;
    }
}