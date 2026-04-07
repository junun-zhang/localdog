package com.example.ireader.ui.main;

import android.content.Context;
import com.example.ireader.data.repository.BookRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class BooksViewModel_Factory implements Factory<BooksViewModel> {
  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<Context> contextProvider;

  public BooksViewModel_Factory(Provider<BookRepository> bookRepositoryProvider,
      Provider<Context> contextProvider) {
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public BooksViewModel get() {
    return newInstance(bookRepositoryProvider.get(), contextProvider.get());
  }

  public static BooksViewModel_Factory create(Provider<BookRepository> bookRepositoryProvider,
      Provider<Context> contextProvider) {
    return new BooksViewModel_Factory(bookRepositoryProvider, contextProvider);
  }

  public static BooksViewModel newInstance(BookRepository bookRepository, Context context) {
    return new BooksViewModel(bookRepository, context);
  }
}
