package pl.bgn.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToDoItemViewModelFactory(private val uuid: String?): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ToDoItemViewModel(uuid) as T
    }
}