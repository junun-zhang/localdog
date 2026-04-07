package com.example.ireader.ui.bookstore;

import com.example.ireader.data.repository.BookStoreRepository;
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
public final class BookStoreViewModel_Factory implements Factory<BookStoreViewModel> {
  private final Provider<BookStoreRepository> bookStoreRepositoryProvider;

  public BookStoreViewModel_Factory(Provider<BookStoreRepository> bookStoreRepositoryProvider) {
    this.bookStoreRepositoryProvider = bookStoreRepositoryProvider;
  }

  @Override
  public BookStoreViewModel get() {
    return newInstance(bookStoreRepositoryProvider.get());
  }

  public static BookStoreViewModel_Factory create(
      Provider<BookStoreRepository> bookStoreRepositoryProvider) {
    return new BookStoreViewModel_Factory(bookStoreRepositoryProvider);
  }

  public static BookStoreViewModel newInstance(BookStoreRepository bookStoreRepository) {
    return new BookStoreViewModel(bookStoreRepository);
  }
}
