package uz.gita.todoapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.todoapp.data.local.dao.TasksDao
import uz.gita.todoapp.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TasksDatabase: RoomDatabase() {

    abstract fun tasksDao(): TasksDao

}