package pl.bgn.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.bgn.todolist.R
import pl.bgn.todolist.adapters.TodoItemsListAdapter
import pl.bgn.todolist.databinding.ActivityMainBinding
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: TodoItemsListAdapter
    private val mQuery = Firebase.firestore.collection("todoitems").orderBy("dateOfCreation", Query.Direction.DESCENDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.swipeRefreshLayout.setOnRefreshListener { mAdapter.refresh() }

        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, ToDoItemActivity::class.java), REQUEST_TODO_ITEM)
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.shouldRefresh.observe(this, Observer {
            if(it) {
                setupAdapter()
                viewModel.shouldRefresh.value = false
            }
        })

//        addRandomPosts()
    }

    private fun addRandomPosts() {
        createPosts().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Posts Added!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createPosts(): Task<Void> {
        val writeBatch = Firebase.firestore.batch();

        for (i in 0..300) {
            val title = "Title $i"
            val desc = "Desc for is message $i."
            val icon = "http://i.imgur.com/DvpvklR.png"
            val todoitem = TodoItem(title = title, description = desc/*, icon = icon*/)

            writeBatch.set(Firebase.firestore.collection("todoitems").document(todoitem.uuid), todoitem)
        }

        return writeBatch.commit()
    }

    private fun setupAdapter() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(30)
            .build()

        val options = FirestorePagingOptions.Builder<TodoItem>()
            .setLifecycleOwner(this)
            .setQuery(mQuery, config, TodoItem::class.java)
            .build()

        mAdapter = TodoItemsListAdapter(
            { item -> startActivityForResult(ToDoItemActivity.newIntent(this@MainActivity, item.uuid), REQUEST_TODO_ITEM) },
            { item ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete")
                builder.setMessage("Do you want to delete this item?")
                builder.setPositiveButton("Yes") { _, _ -> viewModel.deleteItem(item) }
                builder.setNegativeButton("No") { _, _ -> }
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
//            Toast.makeText(this, "Back from details", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_TODO_ITEM = 123
    }
}