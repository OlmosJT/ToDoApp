package uz.gita.todoapp.presentation.ui.screen

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.todoapp.R
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.data.local.model.TaskModel
import uz.gita.todoapp.databinding.ScreenMainBinding
import uz.gita.todoapp.presentation.ui.adapter.TaskAdapter
import uz.gita.todoapp.presentation.ui.broadcast.AlarmReceiver
import uz.gita.todoapp.presentation.ui.dialog.AddTaskBottomSheetDialog
import uz.gita.todoapp.presentation.ui.dialog.CalendarSheet
import uz.gita.todoapp.presentation.viewmodel.MainViewModel
import uz.gita.todoapp.presentation.viewmodel.impl.MainViewModelImpl
import uz.gita.todoapp.utils.cancelNotification
import java.util.*

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private val adapter: TaskAdapter = TaskAdapter()

    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerView()
        createChannel(
            getString(R.string.task_notification_channel_id),
            getString(R.string.task_notification_channel_name)
        )

        binding.calendar.setOnClickListener {
            val calDialog = CalendarSheet()
            calDialog.show(requireActivity().supportFragmentManager, "calendarSheetTag")
        }

        binding.addTask.setOnClickListener {

            val dialog = AddTaskBottomSheetDialog()

            dialog.setAddTaskButtonListener { taskModel, calendar ->
                viewModel.addTask(taskModel)
                /**
                 * SET ALARM RECEIVER MAKE NOTIFICATION
                 */
                setNotificationAlarmAt(taskModel, calendar)
                viewModel.getAllTasks()
            }

            dialog.show(requireActivity().supportFragmentManager, "newTaskTag")

        }

        viewModel.allTasksLiveData.observe(viewLifecycleOwner, Observer<List<TaskModel>> {
            adapter.submitList(it)
        })
        viewModel.emptyLiveData.observe(viewLifecycleOwner, Observer<Unit> {
            binding.imgEmptyData.visibility = View.VISIBLE
        })
        viewModel.nonEmptyLiveData.observe(viewLifecycleOwner, Observer<Unit> {
            binding.imgEmptyData.visibility = View.GONE
        })

        viewModel.successLiveData.observe(viewLifecycleOwner, Observer<Unit> {
            viewModel.getAllTasks()

        })

    }

    private fun setNotificationAlarmAt(taskModel: TaskModel, calendar: Calendar) {
        val alarmIntent: Intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmIntent.putExtra("dataDesc", taskModel.description)
        alarmIntent.putExtra("dataId", taskModel.id)
        val alarmPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            taskModel.id,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        if (alarmIntent != null && alarmManager != null) {
            alarmManager.cancel(alarmPendingIntent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmPendingIntent)
            } else {
                alarmManager?.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmPendingIntent)
            }
        } else {
            alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmPendingIntent)
        }
    }

    private fun setRecyclerView() {
        binding.taskRecycler.adapter = adapter
        binding.taskRecycler.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemMoreClickListener { taskModel, view, position ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuDelete -> {
                        val dialog = AlertDialog.Builder(requireContext(), com.google.android.material.R.style.MaterialAlertDialog_MaterialComponents_Title_Text)
                        dialog
                            .setTitle(R.string.delete_confirmation)
                            .setMessage(R.string.sureToDelete)
                            .setPositiveButton(R.string.yes) { dialog, which ->
                                Log.d("TTT", "Task will delete soon!")
                                viewModel.deleteTaskById(taskModel.id)
                                val notificationManager =
                                    requireContext().let {
                                        ContextCompat.getSystemService(
                                            it,
                                            NotificationManager::class.java
                                        )
                                    } as NotificationManager
                                notificationManager.cancelNotification(taskModel.id)
                            }
                            .setNegativeButton(R.string.no) { dialog, which ->
                                dialog.cancel()
                            }
                            .show()
                    }
                    R.id.menuUpdate -> {}
                    R.id.menuComplete -> {
                        val dialog = AlertDialog.Builder(requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3_Title_Text)
                        dialog
                            .setTitle(R.string.confirmation)
                            .setMessage(R.string.sureToMarkAsComplete)
                            .setPositiveButton(R.string.yes) { dialog, which ->
                                taskModel.apply {
                                    viewModel.updateTask(
                                        TaskEntity(
                                            id = id,
                                            title = title,
                                            date = date,
                                            description = description,
                                            isComplete = true,
                                            alarmTime = time,
                                            event = event
                                        )
                                    )
                                }
                                showCompleteDialog(taskModel)
                                adapter.notifyItemChanged(position)
                            }
                            .setNegativeButton(R.string.no) { dialog, which ->
                                dialog.cancel()
                            }
                            .show()
                    }
                    else -> {}
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }
    }

    private fun showCompleteDialog(taskModel: TaskModel) {
        val dialog = Dialog(requireContext(), R.style.AppTheme_Dialog)
        dialog.setContentView(R.layout.dialog_completed_theme)
        val closeButton: AppCompatButton = dialog.findViewById(R.id.closeButton) as AppCompatButton
        closeButton.setOnClickListener {
            // delete task from id. or put it on Broadcast
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply { setShowBadge(false) }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Task Deadline"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}