package com.example.ireader.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lcom/example/ireader/ui/main/BooksViewModel;", "Landroidx/lifecycle/ViewModel;", "bookRepository", "Lcom/example/ireader/data/repository/BookRepository;", "(Lcom/example/ireader/data/repository/BookRepository;)V", "books", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/ireader/data/model/Book;", "getBooks", "()Landroidx/lifecycle/LiveData;", "addBookFromUri", "", "uri", "Landroid/net/Uri;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BooksViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.repository.BookRepository bookRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Book>> books = null;
    
    @javax.inject.Inject()
    public BooksViewModel(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.repository.BookRepository bookRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.ireader.data.model.Book>> getBooks() {
        return null;
    }
    
    public final void addBookFromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
}