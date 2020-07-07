package pl.bgn.todolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.squareup.picasso.Picasso
import org.joda.time.DateTime
import pl.bgn.todolist.R
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.databinding.RecyclerViewTodoItemBinding


class TodoItemsListAdapter internal constructor(
    private val onItemClick: (TodoItem) -> Unit,
    private val onLongItemClick: (TodoItem) -> Boolean,
    options: FirestorePagingOptions<TodoItem>,
    private val swipeRefreshLayout: SwipeRefreshLayout
): FirestorePagingAdapter<TodoItem, TodoItemsListAdapter.ToDoItemViewHolder>(options) {

    companion object {
        private const val CLICK_TIME_INTERVAL = 300
    }

    inner class ToDoItemViewHolder(
        private val binding: RecyclerViewTodoItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        private lateinit var todoItem: TodoItem
        private var mLastClickTime = System.currentTimeMillis()

        init {
            itemView.apply {
                setOnClickListener(this@ToDoItemViewHolder)
                setOnLongClickListener(this@ToDoItemViewHolder)
            }
        }

        override fun onClick(p0: View?) {
            val now = System.currentTimeMillis()
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return
            }
            mLastClickTime = now
            onItemClick(todoItem)
        }

        override fun onLongClick(p0: View?) = onLongItemClick(todoItem)

        fun bind(todoItem: TodoItem) {
            this.todoItem = todoItem
            with(binding) {
                title.text = todoItem.title
                date.text = DateTime(todoItem.dateOfCreation.toDate()).toLocalDateTime().toString("dd MMM yyyy HH:mm")
                Picasso.get().load(todoItem.icon)
                    .fit()
                    .placeholder(R.drawable.ic_baseline_note_48)
                    .error(R.drawable.ic_baseline_note_48)
                    .into(icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val binding: RecyclerViewTodoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_todo_item, parent, false)
        return ToDoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int, item: TodoItem) {
        holder.bind(item)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        when (state) {
            LoadingState.LOADING_INITIAL -> swipeRefreshLayout.isRefreshing = true

            LoadingState.LOADING_MORE -> swipeRefreshLayout.isRefreshing = true

            LoadingState.LOADED -> swipeRefreshLayout.isRefreshing = false

            LoadingState.ERROR -> swipeRefreshLayout.isRefreshing = false

            LoadingState.FINISHED -> swipeRefreshLayout.isRefreshing = false
        }
    }
}