package me.nomi.urdutyper.presentation.utils.extensions.common

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import androidx.fragment.app.Fragment
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.AlertDialogeDesignBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Fragment.newDialog(
    message: String,
    init: (DialogBuilder.() -> Unit)? = null
) = requireActivity().newDialog(message, init)

fun Context.newDialog(
    message: String,
    init: (DialogBuilder.() -> Unit)? = null
) = DialogBuilder(this).apply {
    message(message)
    if (init != null) init()
}

fun Fragment.newDialog(
    message: Int,
    init: (DialogBuilder.() -> Unit)? = null
) = requireActivity().newDialog(message, init)

fun Context.newDialog(
    message: Int,
    init: (DialogBuilder.() -> Unit)? = null
) = DialogBuilder(this).apply {
    message(message)
    if (init != null) init()
}


fun Fragment.newDialog(init: DialogBuilder.() -> Unit): DialogBuilder =
    requireActivity().newDialog(init)

fun Context.newDialog(init: DialogBuilder.() -> Unit) = DialogBuilder(this).apply { init() }

fun Fragment.dialog(
    message: String,
    init: (DialogBuilder.() -> Unit)? = null
) = requireActivity().dialog(message, init)

fun Context.dialog(
    message: String,
    init: (DialogBuilder.() -> Unit)? = null
) = DialogBuilder.getInstance(this).apply {
    message(message)
    if (init != null) init()
}

fun Fragment.dialog(
    message: Int,
    init: (DialogBuilder.() -> Unit)? = null
) = requireActivity().dialog(message, init)

fun Context.dialog(
    message: Int,
    init: (DialogBuilder.() -> Unit)? = null
) = DialogBuilder.getInstance(this).apply {
    message(message)
    if (init != null) init()
}


fun Fragment.dialog(init: DialogBuilder.() -> Unit): DialogBuilder =
    requireActivity().dialog(init)

fun Context.dialog(init: DialogBuilder.() -> Unit) =
    DialogBuilder.getInstance(this).apply { init() }


fun Fragment.datePickerDialog(init: DatePickerDialogBuilder.() -> Unit): DatePickerDialogBuilder =
    requireActivity().datePickerDialog(init)

fun Context.datePickerDialog(init: DatePickerDialogBuilder.() -> Unit) =
    DatePickerDialogBuilder(this).apply { init() }

fun Fragment.timePickerDialog(init: TimePickerDialogBuilder.() -> Unit): TimePickerDialogBuilder =
    requireActivity().timePickerDialog(init)

fun Context.timePickerDialog(init: TimePickerDialogBuilder.() -> Unit) =
    TimePickerDialogBuilder(this).apply { init() }

class DialogBuilder(private val ctx: Context) {

    companion object {
        /**
        the DialogBuilder instance stored in the dialog property can be garbage collected
        when it is no longer needed because it is only being referenced by the dialog property,
        which can be set to null or to a new DialogBuilder instance. We are not keeping
        the context in our custom object.
         */
        @SuppressLint("StaticFieldLeak")
        private var dialog: DialogBuilder? = null
        fun getInstance(context: Context): DialogBuilder {
            return if (dialog?.ctx == context) {
                dialog!!
            } else {
                synchronized(this) {
                    DialogBuilder(context).also { dialog = it }
                }
            }
        }
    }

    private val binding = AlertDialogeDesignBinding.inflate(LayoutInflater.from(ctx))
    private val dialog: Dialog = Dialog(ctx)
    private var isPositiveSet = false
    private var isNegativeSet = false
    private var isMessageSet = false
    private var isCustomViewSet = false

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (!isCustomViewSet)
            dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setWindowAnimations(R.style.dialogStyle)
        dialog.setCancelable(false)
    }

    fun setLayout(layoutResId: Int, layoutHeight: Int): DialogBuilder {
        dialog.window?.setLayout(layoutResId, layoutHeight)
        return this
    }

    fun setBackgroundDrawable(drawable: Drawable): DialogBuilder {
        dialog.window?.setBackgroundDrawable(drawable)
        return this
    }

    fun setWindowAnimations(windowAnimations: Int): DialogBuilder {
        dialog.window?.attributes?.windowAnimations = windowAnimations
        return this
    }

    fun setGravity(gravity: Int): DialogBuilder {
        dialog.window?.setGravity(gravity)
        return this
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun show(): DialogBuilder {
        if (!isCustomViewSet) {
            if (!isPositiveSet) {
                positiveButton { dismiss() }
                binding.positive.visibility = VISIBLE
            }
            if (!isNegativeSet) binding.negative.visibility =
                GONE else binding.negative.visibility = VISIBLE
            if (!isMessageSet) binding.message.visibility = GONE
        } else {
            val exceptions = mutableListOf<String>()
            if (isMessageSet) exceptions.add("Message")
            if (isPositiveSet) exceptions.add("Positive button")
            if (isNegativeSet) exceptions.add("Negative button")
            if (exceptions.isNotEmpty()) {
                throw Exception("${exceptions.joinToString(", ")} cannot be set when custom view is set")
            }
        }
        if (binding.message.text.toString()
                .contains("Unable to resolve host") || binding.message.text.toString()
                .contains("Failed to connect")
        )
            binding.message.text = "Unable to Connect"
        if (!dialog.isShowing)
            dialog.show()
        return this
    }

    fun message(title: CharSequence) {
        isMessageSet = true
        binding.message.text = title
    }

    fun message(resource: Int) {
        isMessageSet = true
        binding.message.text = ctx.getString(resource)
    }

    fun cancellable(value: Boolean = true) {
        dialog.setCancelable(value)
    }

    fun onCancel(onCancelClick: () -> Unit) {
        dialog.setOnCancelListener { onCancelClick() }
    }

    fun positiveButton(textResource: Int = android.R.string.ok, onPositiveClick: () -> Unit) {
        isPositiveSet = true
        binding.positive.text = ctx.getString(textResource)
        binding.positive.setOnClickListener {
            isNegativeSet = false
            onPositiveClick()
        }
    }

    fun positiveButton(title: String, onPositiveClick: () -> Unit) {
        isPositiveSet = true
        binding.positive.text = title
        binding.positive.setOnClickListener {
            isNegativeSet = false
            onPositiveClick()
        }
    }

    fun negativeButton(
        textResource: Int = android.R.string.cancel,
        onNegativeClick: () -> Unit = { dismiss() }
    ) {
        isNegativeSet = true
        binding.negative.text = ctx.getString(textResource)
        binding.negative.setOnClickListener {
            isNegativeSet = false
            onNegativeClick()
        }
    }

    fun negativeButton(title: String, onNegativeClick: () -> Unit = { dismiss() }) {
        isNegativeSet = true
        binding.negative.text = title
        binding.negative.setOnClickListener {
            isNegativeSet = false
            onNegativeClick()
        }
    }

    fun contentView(view: View) {
        isCustomViewSet = true
        cancellable(true)
        dialog.setContentView(view)
    }

    fun contentView(resource: Int) {
        isCustomViewSet = true
        dialog.setContentView(resource)
    }
}


class DatePickerDialogBuilder(private val ctx: Context) {
    private val calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private val selectedDate = Calendar.getInstance()
    private var date: String? = null
    private var onDateSelected: ((String) -> Unit)? = null

    fun onDateSelected(onDateSelected: (String) -> Unit): DatePickerDialogBuilder {
        this.onDateSelected = onDateSelected
        return this
    }

    fun setDefaultDate(date: String): DatePickerDialogBuilder {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val parsedDate = dateFormat.parse(date)
        if (parsedDate != null) {
            calendar.time = parsedDate
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }
        return this
    }

    fun show() {
        val datePickerDialog =
            DatePickerDialog(ctx, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                date = DateFormat.format("EEEE, d MMMM yyyy", selectedDate).toString()
                onDateSelected?.invoke(date!!)
            }, year, month, day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}

class TimePickerDialogBuilder(private val ctx: Context) {
    private val calendar = Calendar.getInstance()
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)
    private val selectedTime = Calendar.getInstance()
    private var time: String? = null
    private var onTimeSelected: ((String) -> Unit)? = null

    fun onTimeSelected(onTimeSelected: (String) -> Unit): TimePickerDialogBuilder {
        this.onTimeSelected = onTimeSelected
        return this
    }

    fun setDefaultTime(time: String): TimePickerDialogBuilder {
        val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val parsedTime = timeFormat.parse(time)
        if (parsedTime != null) {
            calendar.time = parsedTime
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
        }
        return this
    }

    fun show() {
        val timePickerDialog =
            TimePickerDialog(ctx, { _, selectedHour, selectedMinute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                selectedTime.set(Calendar.MINUTE, selectedMinute)
                time = DateFormat.format("hh:mm aa", selectedTime).toString()
                onTimeSelected?.invoke(time!!)
            }, hour, minute, false)
        timePickerDialog.show()
    }
}

