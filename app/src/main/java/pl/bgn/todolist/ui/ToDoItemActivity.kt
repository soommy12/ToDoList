package pl.bgn.todolist.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.bgn.todolist.R
import pl.bgn.todolist.databinding.ActivityTodoItemBinding
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.utils.DescriptionValidator
import pl.bgn.todolist.utils.IconValidator
import pl.bgn.todolist.utils.TitleValidator
import pl.bgn.todolist.viewmodel.ToDoItemViewModel
import pl.bgn.todolist.viewmodel.ToDoItemViewModelFactory

class ToDoItemActivity : AppCompatActivity() {

    private lateinit var titleValidator: TitleValidator
    private lateinit var descriptionValidator: DescriptionValidator
    private lateinit var iconValidator: IconValidator

    private val todoItemUuid: String? by lazy {
        intent.getStringExtra(EXTRA_TODO_UUID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTodoItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_item)
        val viewModelFactory = ToDoItemViewModelFactory(todoItemUuid)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ToDoItemViewModel::class.java)
        binding.viewModel = viewModel

        titleValidator = TitleValidator(this, binding.labelTitle, binding.title)
        descriptionValidator = DescriptionValidator(this, binding.labelDesc, binding.desc)
        iconValidator = IconValidator(this, binding.labelIcon, binding.icon)

        binding.btnSave.setOnClickListener {
            it.isEnabled = false
            titleValidator.validate()
            descriptionValidator.validate()
            iconValidator.validate()
            if(checkValidation()) {
                val title = binding.title.text.toString()
                val icon: String? = binding.icon.text.toString()
                val description = binding.desc.text.toString()
                val todoItem = TodoItem()
                if(todoItemUuid != null) todoItem.uuid = todoItemUuid!!
                todoItem.icon = if(icon == "") null else icon
                todoItem.description = description
                todoItem.title = title
                viewModel.saveTodoItem(todoItem)
                finish()
            } else {
                Toast.makeText(this, R.string.error_validation, Toast.LENGTH_SHORT).show()
                it.isEnabled = true
            }
        }
    }

    private fun checkValidation() : Boolean = titleValidator.isValid && descriptionValidator.isValid && iconValidator.isValid

    companion object {

        private const val TAG  = "ToDo"
        private const val EXTRA_TODO_UUID = "pl.bgn.todolist.ui.extra.uuid"

        fun newIntent(context: Context, uuid: String): Intent {
            val intent = Intent(context, ToDoItemActivity::class.java)
            intent.putExtra(EXTRA_TODO_UUID, uuid)
            return intent
        }
    }
}