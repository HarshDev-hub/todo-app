package com.example.todo.adapter;

import android.view.View;

import com.example.todo.model.Task;

public interface OnTodoClickListener {
    void onTodoClick( Task task);
    void onTodoRadioButtonClick(Task task);

    void onTodoCompleteClick(Task task);

}
