package pl.bgn.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.data.source.FirestoreRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository): ViewModel() {

    private var _shouldRefresh: MutableLiveData<Boolean> = MutableLiveData()

    init {
        listenToTodoItems()
    }

    private fun listenToTodoItems() {
        firestoreRepository.getAllTodoItems().addSnapshotListener { snapshot, e ->
            if(e != null) {
                Log.w(TAG, "Listening failed...", e)
                return@addSnapshotListener
            }
            if(snapshot != null) {
                _shouldRefresh.value = true
            }
        }
    }

    fun deleteItem(item: TodoItem) {
        firestoreRepository.deleteTodoItem(item)
    }

    internal var shouldRefresh: MutableLiveData<Boolean>
        get() {return _shouldRefresh }
        set(value) { _shouldRefresh = value }

    companion object {
        private const val TAG = "MainVM"
    }
}