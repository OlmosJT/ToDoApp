package uz.gita.todoapp.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applandeo.materialcalendarview.EventDay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import uz.gita.todoapp.databinding.ScreenCalendarViewBinding
import uz.gita.todoapp.databinding.ScreenCreateTaskBinding
import uz.gita.todoapp.domain.usecase.AppUseCase
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CalendarSheet: BottomSheetDialogFragment() {
    private lateinit var binding: ScreenCalendarViewBinding
    private val listEvents: List<EventDay> = ArrayList()
    private val calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenCalendarViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val calendarSheetBehaviour = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.back.setOnClickListener { dismiss() }

    }


}