package pl.bgn.todolist.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import pl.bgn.todolist.R

class DescriptionValidator(
    private val context: Context,
    private val descriptionLabel: TextView,
    private val editText: EditText
) {
    var isValid = false

    fun validate() {
        isValid = validateDescription(editText.text.toString())
    }

    private val descriptionInputWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            isValid = validateDescription(p0.toString())
        }
    }

    init {
        editText.addTextChangedListener(descriptionInputWatcher)
    }

    private fun validateDescription(input: String) : Boolean {
        return when {
            input.isEmpty() -> {
                descriptionLabel.text = context.resources.getString(R.string.error_description_empty)
                descriptionLabel.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
                false
            }
            input.length > 200 -> {
                descriptionLabel.text = context.resources.getString(R.string.error_description_to_long)
                descriptionLabel.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
                return false
            }
            else -> {
                descriptionLabel.text = context.resources.getString(R.string.descritption)
                descriptionLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                true
            }
        }
    }
}