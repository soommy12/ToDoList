package pl.bgn.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.bgn.todolist.data.source.FirestoreRepository
import pl.bgn.todolist.data.TodoItem

class MainViewModel: ViewModel() {

    private val db = Firebase.firestore
    private var _todoItems: MutableLiveData<MutableList<TodoItem>> = MutableLiveData()
    private var _shouldRefresh: MutableLiveData<Boolean> = MutableLiveData()
    private var firebaseRepository = FirestoreRepository()

    init {
        listenToTodoItems()
    }

    private fun listenToTodoItems() {
        firebaseRepository.getAllTodoItems().addSnapshotListener { snapshot, e ->
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
        firebaseRepository.deleteTodoItem(item)
    }

    internal var shouldRefresh: MutableLiveData<Boolean>
        get() {return _shouldRefresh }
        set(value) { _shouldRefresh = value }

    companion object {
        private const val TAG = "MainVM"
    }
}