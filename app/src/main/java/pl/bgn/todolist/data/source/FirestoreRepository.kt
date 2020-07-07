package pl.bgn.todolist.data.source

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.bgn.todolist.data.TodoItem

class FirestoreRepository {

   private val db = Firebase.firestore

    fun getAllTodoItems(): CollectionReference {
        return db.collection(COLLECTION_TODO_ITEMS)
    }

    fun deleteTodoItem(item: TodoItem) {
        db.collection(COLLECTION_TODO_ITEMS).document(item.uuid).delete()
            .addOnSuccessListener { Log.d(TAG, "DocSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting doc", e) }
    }

    fun saveTodoItem(todoItem: TodoItem) {
        db.collection(COLLECTION_TODO_ITEMS).document(todoItem.uuid).set(todoItem)
    }

    fun getTodoItem(uuid: String): DocumentReference {
        return db.collection("todoitems").document(uuid)
    }

    companion object {
        private const val TAG = "Firestore"
        private const val COLLECTION_TODO_ITEMS = "todoitems"
    }
}