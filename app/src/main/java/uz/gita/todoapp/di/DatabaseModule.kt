package uz.gita.todoapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.todoapp.data.local.dao.TasksDao
import uz.gita.todoapp.data.local.database.TasksDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @[Provides Singleton]
    fun getDatabase(@ApplicationContext context: Context): TasksDatabase =
        Room.databaseBuilder(context, TasksDatabase::class.java, "Tasks.db")
            .fallbackToDestructiveMigration()
            .build()

    @[Provides Singleton]
    fun getTasksDao(database: TasksDatabase): TasksDao = database.tasksDao()

}