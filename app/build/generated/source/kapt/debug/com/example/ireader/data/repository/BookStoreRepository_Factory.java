package com.example.ireader.data.repository;

import com.example.ireader.data.network.BookStoreApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class BookStoreRepository_Factory implements Factory<BookStoreRepository> {
  private final Provider<BookStoreApi> bookStoreApiProvider;

  public BookStoreRepository_Factory(Provider<BookStoreApi> bookStoreApiProvider) {
    this.bookStoreApiProvider = bookStoreApiProvider;
  }

  @Override
  public BookStoreRepository get() {
    return newInstance(bookStoreApiProvider.get());
  }

  public static BookStoreRepository_Factory create(Provider<BookStoreApi> bookStoreApiProvider) {
    return new BookStoreRepository_Factory(bookStoreApiProvider);
  }

  public static BookStoreRepository newInstance(BookStoreApi bookStoreApi) {
    return new BookStoreRepository(bookStoreApi);
  }
}
