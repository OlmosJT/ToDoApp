package uz.gita.todoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uz.gita.todoapp.data.local.entity.TaskEntity

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>

    @Insert
    fun addTask(taskEntity: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteTaskById(id: Int)

    @Query("SELECT * FROM tasks WHERE id=:id")
    fun getTaskById(id: Int): TaskEntity

    @Update
    fun updateTask(taskEntity: TaskEntity)

}

/**
 *  @Query("SELECT EXISTS (SELECT * FROM events)")
    fun isInitialized(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInitializedEvents(events: List<EventsEntity>)

    @Query("SELECT * FROM events")
    fun getAllEvents(): List<EventsEntity>

    @Query("UPDATE events SET state = 1 WHERE id=:id")
    fun enableEvent(id: Int)

    @Query("UPDATE events SET state = 0 WHERE id=:id")
    fun disableEvent(id: Int)
 */