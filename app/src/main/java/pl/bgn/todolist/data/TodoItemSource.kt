package pl.bgn.todolist.data

interface TodoItemSource {

    fun deleteTodoItem(item: TodoItem)
    fun saveTodoItem(item: TodoItem)
}