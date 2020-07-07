package pl.bgn.todolist.data.source

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.data.TodoItemSource
import javax.inject.Inject

class FirestoreRepository @Inject constructor(): TodoItemSource {

   private val db = Firebase.firestore

    fun getAllTodoItems(): CollectionReference {
        return db.collection(COLLECTION_TODO_ITEMS)
    }

    override fun deleteTodoItem(item: TodoItem) {
        db.collection(COLLECTION_TODO_ITEMS).document(item.uuid).delete()
            .addOnSuccessListener { Log.d(TAG, "DocSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting doc", e) }
    }

    override fun saveTodoItem(item: TodoItem) {
        db.collection(COLLECTION_TODO_ITEMS).document(item.uuid).set(item)
    }

    fun getTodoItem(uuid: String): DocumentReference {
        return db.collection(COLLECTION_TODO_ITEMS).document(uuid)
    }

    fun getQuery(): Query {
        return db.collection(COLLECTION_TODO_ITEMS).orderBy(FIELD_CREATION_DATE, Query.Direction.DESCENDING)
    }

    companion object {
        private const val TAG = "Firestore"
        private const val COLLECTION_TODO_ITEMS = "todoitems"
        private const val FIELD_CREATION_DATE = "dateOfCreation"
    }
}