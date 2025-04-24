package com.example.todo.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.model.Task;
import com.example.todo.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Task> taskList;
    private  final OnTodoClickListener todoClickListener;
    private Button completedButton;

    public RecyclerViewAdapter(List<Task> taskList, OnTodoClickListener onTodoClickListener) {
        this.taskList = taskList;
        this.todoClickListener = onTodoClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        String formatted = Utils.formatDate(task.getDueDate());
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },
                new int[]{
                        Color.LTGRAY, //disabled
                        Utils.priorityColor(task)
        });

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);
        holder.completedButton.setChecked(task.isDone());
        holder.itemView.setTag(task);// Set the task as a tag to the item view

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateList(List<Task> newList) {
        taskList.clear();
        taskList.addAll(newList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatRadioButton radioButton;
        public AppCompatRadioButton completedButton;
        public AppCompatTextView task;
        public Chip todayChip;

        OnTodoClickListener onTodoClickListener;
        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            completedButton = itemView.findViewById(R.id.todo_complete_radio_butoon);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);

            this.onTodoClickListener = todoClickListener;


            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
            completedButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Task currTask = (Task) view.getTag();//taskList.get(getAdapterPosition());
            int id = view.getId();
            if(id == R.id.todo_row_layout){

                onTodoClickListener.onTodoClick(currTask);
            } else if (id == R.id.todo_radio_button) {
                currTask = taskList.get(getAdapterPosition());
                onTodoClickListener.onTodoRadioButtonClick(currTask);

            } else if (id == R.id.todo_complete_radio_butoon) {
                currTask = taskList.get(getAdapterPosition());
                onTodoClickListener.onTodoCompleteClick(currTask);
            }
        }
    }
}

