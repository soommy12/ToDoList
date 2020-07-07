package pl.bgn.todolist.utils

import android.content.Context
import android.util.TypedValue


fun toDP(context: Context, value: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),context.resources.displayMetrics).toInt()
}