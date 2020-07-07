package pl.bgn.todolist

import android.app.Application
import pl.bgn.todolist.di.AppComponent
import pl.bgn.todolist.di.DaggerAppComponent

class TodoListApp: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}