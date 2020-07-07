package pl.bgn.todolist.data

import com.google.firebase.Timestamp
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

data class TodoItem(
    var uuid: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    val dateOfCreation: Timestamp = Timestamp(DateTime.now(DateTimeZone.UTC).toDate()),
    var icon: String? = null)