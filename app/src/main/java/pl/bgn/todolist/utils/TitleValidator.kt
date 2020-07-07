package pl.bgn.todolist.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import pl.bgn.todolist.R

class TitleValidator(
    private val context: Context,
    private val titleLabel: TextView,
    private val editText: EditText
) {
    var isValid = false

    fun validate() {
        isValid = validateTitle(editText.text.toString())
    }

    private val titleInputWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            isValid = validateTitle(p0.toString())
        }
    }

    init {
        editText.addTextChangedListener(titleInputWatcher)
    }

    private fun validateTitle(input: String) : Boolean {
        return when {
            input.isEmpty() -> {
                titleLabel.text = context.resources.getString(R.string.error_title_empty)
                titleLabel.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
                false
            }
            input.length > 30 -> {
                titleLabel.text = context.resources.getString(R.string.error_title_to_long)
                titleLabel.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
                return false
            }
            else -> {
                titleLabel.text = context.resources.getString(R.string.title)
                titleLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                true
            }
        }
    }
}