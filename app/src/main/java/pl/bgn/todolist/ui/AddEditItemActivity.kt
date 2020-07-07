package pl.bgn.todolist.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import pl.bgn.todolist.R
import pl.bgn.todolist.TodoListApp
import pl.bgn.todolist.data.TodoItem
import pl.bgn.todolist.databinding.ActivityTodoItemBinding
import pl.bgn.todolist.utils.DescriptionValidator
import pl.bgn.todolist.utils.IconValidator
import pl.bgn.todolist.utils.TitleValidator
import pl.bgn.todolist.viewmodel.TodoItemViewModel
import javax.inject.Inject

class AddEditItemActivity : AppCompatActivity() {

    private lateinit var titleValidator: TitleValidator
    private lateinit var descriptionValidator: DescriptionValidator
    private lateinit var iconValidator: IconValidator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TodoItemViewModel> { viewModelFactory }

    private val todoItemUuid: String? by lazy {
        intent.getStringExtra(EXTRA_TODO_UUID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as TodoListApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        val binding: ActivityTodoItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_item)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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
                setResult(MainActivity.RESULT_ADD)
                if(todoItemUuid != null) {
                    todoItem.uuid = todoItemUuid!!
                    setResult(MainActivity.RESULT_UPDATE)
                }
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

        viewModel.start(todoItemUuid)
    }

    private fun checkValidation() : Boolean = titleValidator.isValid && descriptionValidator.isValid && iconValidator.isValid

    companion object {

        private const val TAG  = "ToDo"
        private const val EXTRA_TODO_UUID = "pl.bgn.todolist.ui.extra.uuid"

        fun newIntent(context: Context, uuid: String): Intent {
            val intent = Intent(context, AddEditItemActivity::class.java)
            intent.putExtra(EXTRA_TODO_UUID, uuid)
            return intent
        }
    }
}