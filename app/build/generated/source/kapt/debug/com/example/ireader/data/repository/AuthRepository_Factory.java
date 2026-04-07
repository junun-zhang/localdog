package com.example.ireader.data.repository;

import android.content.Context;
import com.example.ireader.data.network.AuthApi;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<AuthApi> authApiProvider;

  private final Provider<Context> contextProvider;

  public AuthRepository_Factory(Provider<AuthApi> authApiProvider,
      Provider<Context> contextProvider) {
    this.authApiProvider = authApiProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(authApiProvider.get(), contextProvider.get());
  }

  public static AuthRepository_Factory create(Provider<AuthApi> authApiProvider,
      Provider<Context> contextProvider) {
    return new AuthRepository_Factory(authApiProvider, contextProvider);
  }

  public static AuthRepository newInstance(AuthApi authApi, Context context) {
    return new AuthRepository(authApi, context);
  }
}
