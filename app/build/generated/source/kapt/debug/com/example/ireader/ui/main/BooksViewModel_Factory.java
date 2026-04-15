package com.example.ireader.ui.main;

import com.example.ireader.data.repository.BookRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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

  public BooksViewModel_Factory(Provider<BookRepository> bookRepositoryProvider) {
    this.bookRepositoryProvider = bookRepositoryProvider;
  }

  @Override
  public BooksViewModel get() {
    return newInstance(bookRepositoryProvider.get());
  }

  public static BooksViewModel_Factory create(Provider<BookRepository> bookRepositoryProvider) {
    return new BooksViewModel_Factory(bookRepositoryProvider);
  }

  public static BooksViewModel newInstance(BookRepository bookRepository) {
    return new BooksViewModel(bookRepository);
  }
}
