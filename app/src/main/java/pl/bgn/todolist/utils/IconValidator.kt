package pl.bgn.todolist.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import pl.bgn.todolist.R
import java.util.regex.Pattern

class IconValidator(
    private val context: Context,
    private val iconLabel: TextView,
    private val editText: EditText
) {

    var isValid = false

    fun validate() {
        isValid = validateIconLink(editText.text.toString())
    }

    private val iconInputWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            isValid = validateIconLink(p0.toString())
        }
    }

    init {
        editText.addTextChangedListener(iconInputWatcher)
    }

    private fun validateIconLink(input: String) : Boolean {
        return if(input.isNotEmpty() && !LINK_PATTERN.matcher(input).matches()) {
            iconLabel.text = context.resources.getString(R.string.error_icon_not_link)
            iconLabel.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            false
        } else {
            iconLabel.text = context.resources.getString(R.string.icon_url)
            iconLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            return true
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("(http(s?):/)(/[^/]+)+\\.(?:jpg|png)")
    }
}