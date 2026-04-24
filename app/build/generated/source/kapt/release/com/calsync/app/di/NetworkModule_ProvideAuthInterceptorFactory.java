package com.calsync.app.di;

import com.calsync.app.data.remote.interceptor.AuthInterceptor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class NetworkModule_ProvideAuthInterceptorFactory implements Factory<AuthInterceptor> {
  @Override
  public AuthInterceptor get() {
    return provideAuthInterceptor();
  }

  public static NetworkModule_ProvideAuthInterceptorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AuthInterceptor provideAuthInterceptor() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideAuthInterceptor());
  }

  private static final class InstanceHolder {
    private static final NetworkModule_ProvideAuthInterceptorFactory INSTANCE = new NetworkModule_ProvideAuthInterceptorFactory();
  }
}
