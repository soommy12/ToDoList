package pl.bgn.todolist.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import pl.bgn.todolist.databinding.RecyclerViewTodoItemBinding
import pl.bgn.todolist.data.TodoItem

class TodoItemViewHolder(
    private val binding: RecyclerViewTodoItemBinding,
    private val onItemClick: (TodoItem) -> Unit,
    private val onLongItemClick: (TodoItem) -> Boolean
): RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    private lateinit var todoItem: TodoItem

    init {
        itemView.apply {
            setOnClickListener(this@TodoItemViewHolder)
            setOnLongClickListener(this@TodoItemViewHolder)
        }
    }

    override fun onClick(p0: View?) = onItemClick(todoItem)

    override fun onLongClick(p0: View?) = onLongItemClick(todoItem)

    fun bind(todoItem: TodoItem) {
        this.todoItem = todoItem
        with(binding) {
            title.text = todoItem.title
            date.text = DateTime(todoItem.dateOfCreation.toDate()).toLocalDate().toString("dd.MM.yyyy")
        }
    }
}