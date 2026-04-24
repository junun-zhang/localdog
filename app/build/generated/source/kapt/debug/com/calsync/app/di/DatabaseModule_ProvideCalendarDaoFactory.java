package com.calsync.app.di;

import com.calsync.app.data.local.database.AppDatabase;
import com.calsync.app.data.local.database.CalendarDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCalendarDaoFactory implements Factory<CalendarDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideCalendarDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CalendarDao get() {
    return provideCalendarDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideCalendarDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCalendarDaoFactory(databaseProvider);
  }

  public static CalendarDao provideCalendarDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCalendarDao(database));
  }
}
