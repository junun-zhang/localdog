package com.calsync.app.data.repository;

import com.calsync.app.data.local.database.CalendarDao;
import com.calsync.app.data.remote.api.CalSyncApi;
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
public final class CalendarRepository_Factory implements Factory<CalendarRepository> {
  private final Provider<CalendarDao> calendarDaoProvider;

  private final Provider<CalSyncApi> apiProvider;

  public CalendarRepository_Factory(Provider<CalendarDao> calendarDaoProvider,
      Provider<CalSyncApi> apiProvider) {
    this.calendarDaoProvider = calendarDaoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public CalendarRepository get() {
    return newInstance(calendarDaoProvider.get(), apiProvider.get());
  }

  public static CalendarRepository_Factory create(Provider<CalendarDao> calendarDaoProvider,
      Provider<CalSyncApi> apiProvider) {
    return new CalendarRepository_Factory(calendarDaoProvider, apiProvider);
  }

  public static CalendarRepository newInstance(CalendarDao calendarDao, CalSyncApi api) {
    return new CalendarRepository(calendarDao, api);
  }
}
