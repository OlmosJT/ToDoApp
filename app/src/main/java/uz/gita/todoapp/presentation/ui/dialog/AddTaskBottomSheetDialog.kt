package uz.gita.todoapp.presentation.ui.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.todoapp.data.local.model.TaskModel
import uz.gita.todoapp.databinding.ScreenCreateTaskBinding
import uz.gita.todoapp.presentation.viewmodel.MainViewModel
import uz.gita.todoapp.presentation.viewmodel.impl.MainViewModelImpl
import java.util.*

class AddTaskBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: ScreenCreateTaskBinding
    private var addTaskButtonListener: ((data: TaskModel, calendar: Calendar) -> Unit) ?= null
    private val calendar = Calendar.getInstance()

    fun setAddTaskButtonListener(block: (TaskModel, Calendar) -> Unit) {
        addTaskButtonListener = block
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTaskButton.setOnClickListener {
            if (checkAllFieldsValidation()) {
                binding.apply {
                    addTaskButtonListener!!.invoke(TaskModel(
                        0,
                        addTaskTitle.text.toString(),
                        taskDate.text.toString(),
                        addTaskDescription.text.toString(),
                        false,
                        taskTime.text.toString(),
                        taskEvent.text.toString()
                    ), calendar)
                }
                Log.d("TTT", "Successfully added!")
                dismiss()
            }
        }

        binding.taskDate.setOnTouchListener { view, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH)
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { datePicker, year, month, day ->
                        binding.taskDate.setText("$day-${month+1}-$year")

                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, day)

                        //datePickerDialog.dismiss()
                    },
                    mYear,
                    mMonth,
                    mDay
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            true
        }

        binding.taskTime.setOnTouchListener { view, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                calendar.timeInMillis = System.currentTimeMillis()
                val mHour = calendar.get(Calendar.HOUR_OF_DAY)
                val mMinute = calendar.get(Calendar.MONTH)

                val timerPickerDialog = TimePickerDialog(
                    requireContext(),
                    { timePicker, hourOfDay, minute ->
                        binding.taskTime.setText("$hourOfDay:$minute")
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                    },
                    mHour, mMinute, false
                )
                timerPickerDialog.show()
            }
            true
        }


    }

    /*
     taskDate.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            taskDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            return true;
        });
     */

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    fun checkAllFieldsValidation(): Boolean = with(binding) {
        if (addTaskTitle.text.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter a valid title", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addTaskDescription.text.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter a valid description", Toast.LENGTH_SHORT).show()
            return false
        }
        if (taskDate.text.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter a date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (taskTime.text.isEmpty()) {
            Toast.makeText(requireActivity(), "PLease enter a time", Toast.LENGTH_SHORT).show()
            return false
        }
        if (taskEvent.text.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter an event", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}