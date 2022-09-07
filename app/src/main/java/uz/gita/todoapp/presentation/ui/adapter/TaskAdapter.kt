package uz.gita.todoapp.presentation.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todoapp.R
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.data.local.model.TaskModel
import uz.gita.todoapp.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter : ListAdapter<TaskModel, TaskAdapter.ViewHolder>(TasksDiffUtil) {
    private var onItemMoreClickListener: ((TaskModel, view: View, position: Int) -> Unit) ?= null

    fun setOnItemMoreClickListener(block: (TaskModel, view: View, position: Int) -> Unit) {
        onItemMoreClickListener = block
    }

    private object TasksDiffUtil : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem == newItem

    }

    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.options.setOnClickListener {
                val position = absoluteAdapterPosition
                onItemMoreClickListener!!.invoke(getItem(position), it, position)
            }
        }

        fun bind() {
            val task = getItem(absoluteAdapterPosition)
            binding.title.text = task.title
            binding.description.text = task.description
            binding.time.text = task.time
            if (task.isComplete) {
                binding.status.text = "Completed"
                binding.status.setTextColor(Color.parseColor("#2ba84a"))
            } else {
                binding.status.text = "UnCompleted"
                binding.status.setTextColor(Color.RED)
            }

            try {
                val inputDate = SimpleDateFormat("dd-M-yyyy", Locale.US).parse(task.date)
                val outputDate = inputDate?.let { SimpleDateFormat("EE dd MMM yyyy", Locale.US).format(it) }
                val dateInArr = outputDate?.split(" ")
                binding.day.text = dateInArr?.get(0) ?: "Unk"
                binding.date.text = dateInArr?.get(1) ?: "00"
                binding.month.text = dateInArr?.get(2) ?: "Unk"
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemTaskBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}