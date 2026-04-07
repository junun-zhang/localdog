package com.example.ireader.ui.reader;

import android.app.Application;
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
public final class ReaderViewModel_Factory implements Factory<ReaderViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  public ReaderViewModel_Factory(Provider<Application> applicationProvider,
      Provider<BookRepository> bookRepositoryProvider) {
    this.applicationProvider = applicationProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
  }

  @Override
  public ReaderViewModel get() {
    return newInstance(applicationProvider.get(), bookRepositoryProvider.get());
  }

  public static ReaderViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<BookRepository> bookRepositoryProvider) {
    return new ReaderViewModel_Factory(applicationProvider, bookRepositoryProvider);
  }

  public static ReaderViewModel newInstance(Application application,
      BookRepository bookRepository) {
    return new ReaderViewModel(application, bookRepository);
  }
}
