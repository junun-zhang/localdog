package com.calsync.app.data.repository;

import com.calsync.app.data.local.database.EventDao;
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
public final class EventRepository_Factory implements Factory<EventRepository> {
  private final Provider<EventDao> eventDaoProvider;

  private final Provider<CalSyncApi> apiProvider;

  public EventRepository_Factory(Provider<EventDao> eventDaoProvider,
      Provider<CalSyncApi> apiProvider) {
    this.eventDaoProvider = eventDaoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public EventRepository get() {
    return newInstance(eventDaoProvider.get(), apiProvider.get());
  }

  public static EventRepository_Factory create(Provider<EventDao> eventDaoProvider,
      Provider<CalSyncApi> apiProvider) {
    return new EventRepository_Factory(eventDaoProvider, apiProvider);
  }

  public static EventRepository newInstance(EventDao eventDao, CalSyncApi api) {
    return new EventRepository(eventDao, api);
  }
}
