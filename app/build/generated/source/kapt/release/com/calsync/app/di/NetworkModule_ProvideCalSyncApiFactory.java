package com.calsync.app.di;

import com.calsync.app.data.remote.api.CalSyncApi;
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
public final class NetworkModule_ProvideCalSyncApiFactory implements Factory<CalSyncApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideCalSyncApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public CalSyncApi get() {
    return provideCalSyncApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideCalSyncApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideCalSyncApiFactory(retrofitProvider);
  }

  public static CalSyncApi provideCalSyncApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideCalSyncApi(retrofit));
  }
}
