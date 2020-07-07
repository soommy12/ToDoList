package pl.bgn.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.bgn.todolist.R
import pl.bgn.todolist.TodoListApp
import pl.bgn.todolist.adapters.TodoItemsListAdapter
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.data.source.FirestoreRepository
import pl.bgn.todolist.databinding.ActivityMainBinding
import pl.bgn.todolist.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var firestoreRepository: FirestoreRepository

    private lateinit var mAdapter: TodoItemsListAdapter
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as TodoListApp).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        binding.swipeRefreshLayout.setOnRefreshListener { mAdapter.refresh() }

        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, AddEditItemActivity::class.java), REQUEST_TODO_ITEM)
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            isMotionEventSplittingEnabled = false
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.shouldRefresh.observe(this, Observer {
            if(it) {
                setupAdapter()
                viewModel.shouldRefresh.value = false
            }
        })

    }

    private fun setupAdapter() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(30)
            .build()

        val options = FirestorePagingOptions.Builder<TodoItem>()
            .setLifecycleOwner(this)
            .setQuery(firestoreRepository.getQuery(), config, TodoItem::class.java)
            .build()

        mAdapter = TodoItemsListAdapter(
            { item -> startActivityForResult(AddEditItemActivity.newIntent(this@MainActivity, item.uuid), REQUEST_TODO_ITEM) },
            { item ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.alert_delete_item_title))
                builder.setMessage(getString(R.string.alert_delete_item_message))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.deleteItem(item) }
                builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
                builder.create().show()
                true
            },
            options,
            binding.swipeRefreshLayout
        )
        binding.recyclerView.adapter = mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TODO_ITEM) {
            val toastMsg = when(resultCode) {
                RESULT_ADD -> R.string.toast_item_added
                RESULT_UPDATE -> R.string.toast_item_updated
                else -> null
            }
            if(toastMsg != null) Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_TODO_ITEM = 1
        const val RESULT_ADD = 2
        const val RESULT_UPDATE = 3
    }
}