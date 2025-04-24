package com.example.todo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todo.data.DoisterRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private static DoisterRepository repository;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<Integer> totalTaskCount;
    private final LiveData<Integer> completedTaskCount;
    private final LiveData<Integer> nonCompletedTaskCount;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new DoisterRepository(application);
        allTasks = repository.getAllTasks();

        totalTaskCount = repository.getTotalTaskCount();
        completedTaskCount = repository.getCompletedTaskCount();
        nonCompletedTaskCount = repository.getNonCompletedTaskCount();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public static void insert(Task task) {
        repository.insert(task);
    }

    public LiveData<Task> get(long id) {
        return repository.get(id);
    }

    public static void update(Task task) {
        repository.update(task);
    }

    public static void delete(Task task) {
        repository.delete(task);
    }

    public LiveData<List<Task>> searchTasks(String query) {
        return repository.searchTasks(query);
    }
    public LiveData<Integer> getTotalTaskCount(){
        return repository.getTotalTaskCount();
    }

    public LiveData<Integer> getCompletedTaskCount(){
        return repository.getCompletedTaskCount();
    }

    public LiveData<Integer> getNonCompletedTaskCount(){
        return repository.getNonCompletedTaskCount();
    }



}

