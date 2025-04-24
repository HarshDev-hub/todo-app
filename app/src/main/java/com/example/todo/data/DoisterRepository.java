package com.example.todo.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.todo.model.Task;
import com.example.todo.util.TaskRoomDatabase;

import java.util.List;

public class DoisterRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<Integer> totalTaskCount; // Declare as instance variable
    private final LiveData<Integer> completedTaskCount; // Declare as instance variable
    private final LiveData<Integer> nonCompletedTaskCount; // Declare as instance variable

    public DoisterRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getTasks();

        // Initialize instance variables
        totalTaskCount = taskDao.getTaskCount();
        completedTaskCount = taskDao.getCompletedTaskCount();
        nonCompletedTaskCount = taskDao.getNonCompletedTaskCount();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> taskDao.insertTask(task));
    }

    public LiveData<Task> get(long id) {
        return taskDao.get(id);
    }

    public void update(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> taskDao.delete(task));
    }

    public LiveData<List<Task>> searchTasks(String searchQuery) {
        return taskDao.searchTasks("%" + searchQuery + "%"); // Add "%" for LIKE query
    }

    public LiveData<Integer> getTotalTaskCount() {
        return totalTaskCount; // Return the instance variable
    }

    public LiveData<Integer> getCompletedTaskCount() {
        Log.d("DoisterRepository", "Completed Task Count: " + completedTaskCount.getValue());

        return completedTaskCount; // Return the instance variable
    }

    public LiveData<Integer> getNonCompletedTaskCount() {
        Log.d("DoisterRepository", "Non-Completed Task Count: " + nonCompletedTaskCount.getValue());

        return nonCompletedTaskCount; // Return the instance variable
    }
}