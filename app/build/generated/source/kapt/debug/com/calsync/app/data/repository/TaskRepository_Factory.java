package com.calsync.app.data.repository;

import com.calsync.app.data.local.database.TaskDao;
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
public final class TaskRepository_Factory implements Factory<TaskRepository> {
  private final Provider<TaskDao> taskDaoProvider;

  private final Provider<CalSyncApi> apiProvider;

  public TaskRepository_Factory(Provider<TaskDao> taskDaoProvider,
      Provider<CalSyncApi> apiProvider) {
    this.taskDaoProvider = taskDaoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public TaskRepository get() {
    return newInstance(taskDaoProvider.get(), apiProvider.get());
  }

  public static TaskRepository_Factory create(Provider<TaskDao> taskDaoProvider,
      Provider<CalSyncApi> apiProvider) {
    return new TaskRepository_Factory(taskDaoProvider, apiProvider);
  }

  public static TaskRepository newInstance(TaskDao taskDao, CalSyncApi api) {
    return new TaskRepository(taskDao, api);
  }
}
