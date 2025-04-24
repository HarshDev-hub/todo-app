package com.example.todo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getTasks();




    @Query("SELECT * FROM task_table WHERE task_id = :id")
    LiveData<Task> get(long id);
    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task_table WHERE task LIKE :searchQuery")
    LiveData<List<Task>> searchTasks(String searchQuery);

    // query for pie chart data
    @Query("SELECT COUNT(*) FROM task_table")
    LiveData<Integer> getTaskCount(); // TOTAL TASK COUNT

    @Query("SELECT COUNT(*) FROM task_table WHERE is_done = 1")
    LiveData<Integer> getCompletedTaskCount(); // COMPLETED TASK COUNT

    @Query("SELECT COUNT(*) FROM task_table WHERE is_done = 0")
    LiveData<Integer> getNonCompletedTaskCount(); // UNCOMPLETED TASK COUNT


}