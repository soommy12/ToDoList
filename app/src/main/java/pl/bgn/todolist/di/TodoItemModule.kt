package pl.bgn.todolist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.bgn.todolist.viewmodel.TodoItemViewModel

@Module
abstract class TodoItemModule {

    @Binds
    @IntoMap
    @ViewModelKey(TodoItemViewModel::class)
    abstract fun bindViewModel(viewModel: TodoItemViewModel): ViewModel
}