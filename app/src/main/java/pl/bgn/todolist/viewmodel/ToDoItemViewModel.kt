package pl.bgn.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.data.source.FirestoreRepository
import javax.inject.Inject

class ToDoItemViewModel(uuid: String?) : ViewModel() {

    val firestoreRepository = FirestoreRepository()
    private val _todoItem = MutableLiveData<TodoItem>().apply { null }
    val todoItem: LiveData<TodoItem?> = _todoItem

    val title = MutableLiveData<String>()

    init {
        if(uuid != null) {
            firestoreRepository.getTodoItem(uuid).addSnapshotListener { documentSnapshot, e ->
                if(e != null) {
                    Log.w("TAG", "Error gettingsnapshot", e)
                    return@addSnapshotListener
                }
                _todoItem.value = documentSnapshot?.toObject<TodoItem>()
            }
        }
    }

    fun saveTodoItem(todoItem: TodoItem) {
        firestoreRepository.saveTodoItem(todoItem)
    }

}
