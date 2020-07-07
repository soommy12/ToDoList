package pl.bgn.todolist.di

import dagger.Binds
import dagger.Module
import pl.bgn.todolist.data.TodoItemSource
import pl.bgn.todolist.data.source.FirestoreRepository


@Module
abstract class StorageModule {

    @Binds
    abstract fun provideStorage(storage: FirestoreRepository): TodoItemSource
}