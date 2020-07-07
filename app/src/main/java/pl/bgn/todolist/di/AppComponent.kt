package pl.bgn.todolist.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pl.bgn.todolist.ui.AddEditItemActivity
import pl.bgn.todolist.ui.MainActivity

@Component(modules = [ViewModelBuilder::class, MainModule::class, TodoItemModule::class,  StorageModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(activity: AddEditItemActivity)
}