package com.example.ireader.data.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideBookStoreApiFactory implements Factory<BookStoreApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideBookStoreApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public BookStoreApi get() {
    return provideBookStoreApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideBookStoreApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideBookStoreApiFactory(retrofitProvider);
  }

  public static BookStoreApi provideBookStoreApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideBookStoreApi(retrofit));
  }
}
