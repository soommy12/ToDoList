package pl.bgn.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.data.source.FirestoreRepository
import javax.inject.Inject

class TodoItemViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository) : ViewModel() {

    private var taskUuid: String? = null

    private var isNew: Boolean = false

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val todoItem = MutableLiveData<TodoItem>()

    val title = MutableLiveData<String>()

    fun start(taskUuid: String?) {

        if (_dataLoading.value == true) {
            return
        }

        this.taskUuid = taskUuid
        if(taskUuid == null) {
            isNew = true
            return
        }

        isNew = true
        _dataLoading.value = true

        firestoreRepository.getTodoItem(taskUuid).addSnapshotListener { documentSnapshot, e ->
            if(e != null) {
                Log.w("TAG", "Error gettingsnapshot", e)
                _dataLoading.value = false
                return@addSnapshotListener
            }
            todoItem.value = documentSnapshot?.toObject<TodoItem>()
            _dataLoading.value = false
        }

    }

    fun saveTodoItem(todoItem: TodoItem) {
        firestoreRepository.saveTodoItem(todoItem)
    }

}
